package com.wldmedical.capnoeasy.kits

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.DATABASE_NS
import com.wldmedical.capnoeasy.USER_PREF_NS
import io.github.weisiwu.slstracker.SlsLogTracker.Level
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption.ATOMIC_MOVE
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

private const val IS_FIRST_LAUNCH = "is_wld_medical_app_first_install"
internal const val BACKUP_FAILURE_COUNT = "backup_failure_count"
private const val MAX_BACKUP_FAILURES = 3

class DatabaseBackupHelper(private val context: Context) {
    private val backupDirectory = "${Environment.DIRECTORY_DOWNLOADS}/CapnoEasyApplicationDatabaseBackup/"
    private val prefs: SharedPreferences = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)

    fun startWork(database: AppDatabase, isRestore: Boolean = false): Boolean {
        val firstLaunch = prefs.getBoolean(IS_FIRST_LAUNCH, true)
        val live = context.getDatabasePath(DATABASE_NS)
        if (firstLaunch || isRestore) {
            prefs.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val backup = File(File(downloads, "CapnoEasyApplicationDatabaseBackup"), DATABASE_NS)
            return runBlocking(Dispatchers.IO) {
                var databaseReleased = false
                try {
                    restoreFromBackup(live, backup, expected = isRestore && live.exists()) {
                        databaseReleased = true
                        closeDatabase(database)
                    }
                } finally {
                    if (databaseReleased) refreshDatabase()
                }
            }
        }

        if (!live.exists()) return false
        if (prefs.getInt(BACKUP_FAILURE_COUNT, 0) >= MAX_BACKUP_FAILURES) {
            ErrorReporter.diagnostic(Level.WARN, "DatabaseBackupHelper.backup_suspended", MAX_BACKUP_FAILURES)
            Toast.makeText(context, "自动备份失败，请稍后再试", Toast.LENGTH_SHORT).show()
            return false
        }
        return runBlocking(Dispatchers.IO) {
            runAutomaticBackup {
                val wasOpen = database.isOpen
                try {
                    if (wasOpen) closeDatabase(database)
                    backupDatabase(live)
                } finally {
                    if (wasOpen) refreshDatabase()
                }
            }
        }
    }

    internal fun runAutomaticBackup(backup: () -> Unit): Boolean {
        return try {
            backup()
            prefs.edit().putInt(BACKUP_FAILURE_COUNT, 0).apply()
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val failureCount = prefs.getInt(BACKUP_FAILURE_COUNT, 0) + 1
            prefs.edit().putInt(BACKUP_FAILURE_COUNT, failureCount).apply()
            val metadata = mapOf("failure_count" to failureCount)
            val stage = when (e) {
                is BackupEntryUnavailableException -> "DatabaseBackupHelper.backup_entry_unavailable"
                is BackupStreamUnavailableException -> "DatabaseBackupHelper.backup_stream_unavailable"
                else -> "DatabaseBackupHelper.backup_database"
            }
            ErrorReporter.report(e, stage, metadata)
            if (failureCount == MAX_BACKUP_FAILURES) {
                ErrorReporter.report(e, "DatabaseBackupHelper.backup_on_start", metadata)
            }
            false
        }
    }

    private fun backupDatabase(databaseFile: File) {
        val resolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val files = listOf(databaseFile, File("${databaseFile.path}-wal"), File("${databaseFile.path}-shm"))
        for (source in files) {
            val existing = resolver.query(
                collection,
                arrayOf(MediaStore.MediaColumns._ID),
                "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?",
                arrayOf(source.name, backupDirectory),
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) ContentUris.withAppendedId(collection, cursor.getLong(0)) else null
            }
            if (source != databaseFile && !source.exists()) {
                if (existing != null && resolver.delete(existing, null, null) == 0) {
                    throw IOException("Could not remove stale backup sidecar")
                }
                continue
            }
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, source.name)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                put(MediaStore.MediaColumns.RELATIVE_PATH, backupDirectory)
                if (existing == null) put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val destination = existing ?: resolver.insert(collection, values)
            try {
                writeBackupContents(source, destination) { uri -> resolver.openOutputStream(uri, "wt") }
                if (existing == null && destination != null) {
                    values.clear()
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    if (resolver.update(destination, values, null, null) == 0) {
                        throw IOException("Could not publish backup file")
                    }
                }
            } catch (e: Exception) {
                if (existing == null && destination != null) {
                    try {
                        resolver.delete(destination, null, null)
                    } catch (cleanupError: Exception) {
                        e.addSuppressed(cleanupError)
                    }
                }
                throw e
            }
        }
    }

    internal fun restoreFromBackup(
        live: File,
        backup: File,
        expected: Boolean,
        beforeReplace: () -> Unit
    ): Boolean {
        if (!backup.isFile) {
            if (expected) ErrorReporter.diagnostic(Level.WARN, "DatabaseBackupHelper.restore_backup_missing")
            return false
        }

        var staging: File? = null
        var stage = "DatabaseBackupHelper.copy_backup_to_database"
        return try {
            val parent = checkNotNull(live.parentFile)
            Files.createDirectories(parent.toPath())
            val directory = Files.createTempDirectory(parent.toPath(), "capno-restore-").toFile()
            staging = directory
            val candidate = File(directory, live.name)
            backup.copyTo(candidate)
            val backupWal = File("${backup.path}-wal")
            if (backupWal.isFile) backupWal.copyTo(File("${candidate.path}-wal"))
            validateCandidate(candidate)

            stage = "DatabaseBackupHelper.restore_database"
            beforeReplace()
            // Replacing the main file while SQLite can still replay a journal from the old
            // database would mix two databases. A failed precondition leaves the live files intact.
            for (suffix in listOf("-wal", "-shm", "-journal")) {
                if (File("${live.path}$suffix").exists()) throw IOException("Live database sidecar still exists")
            }
            Files.move(candidate.toPath(), live.toPath(), ATOMIC_MOVE, REPLACE_EXISTING)
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ErrorReporter.report(e, stage)
            false
        } finally {
            staging?.deleteRecursively()
        }
    }

    private fun validateCandidate(candidate: File) {
        if (candidate.length() == 0L) throw IOException("Backup database is empty")
        // The framework's default corruption handler can delete a malformed file and
        // Room can then create an empty database at that path. Reject it before Room opens it.
        SQLiteDatabase.openDatabase(
            candidate.absolutePath,
            null,
            SQLiteDatabase.OPEN_READONLY
        ) { }.use { sqlite ->
            sqlite.rawQuery("PRAGMA quick_check", null).use { result ->
                if (!result.moveToFirst() || result.getString(0) != "ok") {
                    throw IOException("Backup database failed integrity check")
                }
            }
            val tables = if (sqlite.version == 1) listOf("patients", "records")
            else listOf("patients", "records", "co2_data")
            val placeholders = tables.joinToString(",") { "?" }
            sqlite.rawQuery(
                "SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name IN ($placeholders)",
                tables.toTypedArray()
            ).use { result ->
                if (!result.moveToFirst() || result.getInt(0) != tables.size) {
                    throw IOException("Backup database is missing CapnoEasy tables")
                }
            }
        }
        val database = AppDatabase.buildDatabase(context, candidate.absolutePath)
        try {
            val sqlite = database.openHelper.writableDatabase
            sqlite.query("PRAGMA wal_checkpoint(TRUNCATE)").use { result ->
                if (!result.moveToFirst() || result.getInt(0) != 0) {
                    throw IOException("Backup database checkpoint failed")
                }
            }
        } finally {
            database.close()
        }
    }

    private fun closeDatabase(database: AppDatabase) {
        database.close()
        AppDatabase.clearInstance()
    }

    private fun refreshDatabase() {
        (context.applicationContext as? CapnoEasyApplication)?.let { app ->
            app.database = AppDatabase.getDatabase(context)
        }
    }
}

private class BackupEntryUnavailableException : IOException("MediaStore did not create a backup entry")
private class BackupStreamUnavailableException : IOException("MediaStore did not open the backup stream")

internal fun writeBackupContents(source: File, destination: Uri?, openOutput: (Uri) -> OutputStream?) {
    if (destination == null) throw BackupEntryUnavailableException()
    source.inputStream().use { input ->
        val output = openOutput(destination) ?: throw BackupStreamUnavailableException()
        output.use { input.copyTo(it) }
    }
}

object DatabaseBackupHelperManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var dbBackupHelperKit: DatabaseBackupHelper

    fun initialize(application: CapnoEasyApplication) {
        if (!::dbBackupHelperKit.isInitialized) dbBackupHelperKit = DatabaseBackupHelper(application)
    }
}
