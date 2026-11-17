package com.wldmedical.capnoeasy.kits

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.ContextWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteFullException
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tencent.bugly.crashreport.CrashReport
import com.wldmedical.capnoeasy.models.CO2WavePointData
import io.github.weisiwu.slstracker.SlsLogTracker.Level
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DiagnosticFaultInjectionTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test
    fun recordingBackupBleAndAudioFaults() {
        val runId = InstrumentationRegistry.getArguments().getString("slsRunId").orEmpty()
        require(runId.isEmpty() || Regex("[a-z0-9_]{1,40}").matches(runId))
        CrashReport.closeBugly()
        reportRunBoundary(runId, "start")
        val root = Files.createTempDirectory(context.cacheDir.toPath(), "diagnostic-fault-").toFile()
        try {
            runBlocking { recordingFaults(root) }
            backupFaults(root)
            gattFaults()
            audioFaults()
        } finally {
            root.deleteRecursively()
        }
        reportRunBoundary(runId, "end")
    }

    private suspend fun recordingFaults(root: File) {
        val db = AppDatabase.buildDatabase(context, File(root, "recording.db").absolutePath)
        try {
            val record = seedRecord(db, "recording fixture")
            val points = List(maxRecordDataChunkSize) { CO2WavePointData(index = it) }
            assertFalse(saveWaveformSegment(record.id, points, isTail = false, database = db) {
                throw SQLiteFullException("Injected waveform write failure")
            })
            assertTrue(db.co2DataDao().getCO2DataByRecordId(record.id).isEmpty())

            val tail = points + CO2WavePointData(index = points.size)
            assertFalse(saveWaveformSegment(record.id, tail, isTail = true, database = db) { chunk ->
                if (chunk.chunkIndex == 0) insertCO2Data(chunk) else -1L
            })
            assertTrue("A partial tail must roll back", db.co2DataDao().getCO2DataByRecordId(record.id).isEmpty())

            assertTrue(saveWaveformSegment(record.id, tail, isTail = true, database = db))
            val saved = db.co2DataDao().getCO2DataByRecordId(record.id)
            assertEquals(listOf(0, 1), saved.map { it.chunkIndex })
            assertEquals(tail, saved.flatMap { it.data.decompressToCO2WavePointData() })
        } finally {
            db.close()
        }
    }

    private fun backupFaults(root: File) {
        val prefsName = root.name
        val helper = DatabaseBackupHelper(object : ContextWrapper(context) {
            override fun getSharedPreferences(name: String, mode: Int) =
                super.getSharedPreferences(prefsName, mode)
        })
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val original = File(root, "original.db")
        val backup = File(root, "backup.db")
        try {
            createDatabase(original, "original fixture")
            createDatabase(backup, "backup fixture")
            val before = original.readBytes()
            val destination = Uri.parse("content://media/external_primary/file/1")

            assertFalse(helper.runAutomaticBackup {
                writeBackupContents(original, null) { error("No output stream may be opened") }
            })
            assertEquals(1, prefs.getInt(BACKUP_FAILURE_COUNT, 0))
            assertFalse(helper.runAutomaticBackup { writeBackupContents(original, destination) { null } })
            assertEquals(2, prefs.getInt(BACKUP_FAILURE_COUNT, 0))
            assertFalse(helper.runAutomaticBackup { throw IOException("Injected third backup failure") })
            assertEquals(3, prefs.getInt(BACKUP_FAILURE_COUNT, 0))
            assertTrue(helper.runAutomaticBackup {
                val copy = File(root, "output.db")
                writeBackupContents(original, destination) { copy.outputStream() }
                assertArrayEquals(before, copy.readBytes())
            })
            assertEquals(0, prefs.getInt(BACKUP_FAILURE_COUNT, 0))

            var replacementStarted = false
            val beforeReplace = { replacementStarted = true }
            val missing = File(root, "missing.db")
            assertFalse(helper.restoreFromBackup(original, missing, expected = false, beforeReplace = beforeReplace))
            assertFalse(helper.restoreFromBackup(original, missing, expected = true, beforeReplace = beforeReplace))
            val corrupt = File(root, "corrupt.db").apply { writeText("invalid database fixture") }
            assertFalse(helper.restoreFromBackup(original, corrupt, expected = true, beforeReplace = beforeReplace))
            val emptySchema = File(root, "unrelated.db")
            SQLiteDatabase.openOrCreateDatabase(emptySchema, null).use {
                it.execSQL("CREATE TABLE unrelated (id INTEGER PRIMARY KEY)")
            }
            assertFalse(helper.restoreFromBackup(original, emptySchema, expected = true, beforeReplace = beforeReplace))
            assertFalse("Invalid input must be rejected before opening the live database", replacementStarted)
            assertArrayEquals(before, original.readBytes())
            assertFalse(helper.restoreFromBackup(original, backup, expected = true) {
                throw IOException("Injected failure before replacement")
            })
            assertArrayEquals(before, original.readBytes())

            assertTrue(helper.restoreFromBackup(original, backup, expected = true) {})
            val restored = AppDatabase.buildDatabase(context, original.absolutePath)
            try {
                assertEquals("backup fixture", restored.recordDao().getRecordsPaged(1, 0).single().patient.name)
            } finally {
                restored.close()
            }

            val legacy = File(root, "legacy.db")
            val waveform = listOf(CO2WavePointData(index = 1))
            createLegacyDatabase(legacy, waveform)
            assertTrue("A valid version 1 backup should migrate before replacement",
                helper.restoreFromBackup(original, legacy, expected = true) {})
            val migrated = AppDatabase.buildDatabase(context, original.absolutePath)
            try {
                val record = migrated.recordDao().getRecordsPaged(1, 0).single()
                val chunks = migrated.co2DataDao().getCO2DataByRecordId(record.id)
                assertEquals(waveform, chunks.single().data.decompressToCO2WavePointData())
            } finally {
                migrated.close()
            }
        } finally {
            context.deleteSharedPreferences(prefsName)
        }
    }

    private fun gattFaults() {
        assertEquals(BluetoothGatt.GATT_SUCCESS, observeGattWriteStart { BluetoothGatt.GATT_SUCCESS })
        assertEquals(BluetoothGatt.GATT_FAILURE, observeGattWriteStart { BluetoothGatt.GATT_FAILURE })
        assertTrue(gattWriteCompleted(BluetoothGatt.GATT_SUCCESS))
        assertFalse(gattWriteCompleted(BluetoothGatt.GATT_WRITE_NOT_PERMITTED))
        assertTrue(enableGattNotification(GattNotification.WAVEFORM) { true })
        assertFalse(enableGattNotification(GattNotification.WAVEFORM) { false })
        assertFalse(enableGattNotification(GattNotification.HANDSHAKE) {
            throw SecurityException("Injected notification permission failure")
        })
        assertFalse(checkGattDataCharacteristics(receive = null, send = null))
        val receive = BluetoothGattCharacteristic(
            BLECharacteristicUUID.BLEReceiveDataCha.value, BluetoothGattCharacteristic.PROPERTY_NOTIFY, 0
        )
        val send = BluetoothGattCharacteristic(
            BLECharacteristicUUID.BLESendDataCha.value, BluetoothGattCharacteristic.PROPERTY_WRITE, 0
        )
        assertFalse(checkGattDataCharacteristics(receive, send = null))
        assertFalse(checkGattDataCharacteristics(receive = null, send = send))
        assertTrue(checkGattDataCharacteristics(receive, send))
    }

    private fun audioFaults() {
        instrumentation.runOnMainSync {
            val audioManager = context.getSystemService(AudioManager::class.java)
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val created = mutableListOf<MediaPlayer>()
            var creationAttempts = 0
            val audio = AudioPlayer(context) { playerContext, resource ->
                creationAttempts++
                if (creationAttempts == 1) null
                else checkNotNull(MediaPlayer.create(playerContext, resource)).also(created::add)
            }
            try {
                audio.playAlertAudio()
                assertTrue(created.isEmpty())
                audio.playAlertAudio()
                val failed = created.single()
                assertTrue(audio.handlePlaybackError(failed, MediaPlayer.MEDIA_ERROR_SERVER_DIED, MediaPlayer.MEDIA_ERROR_IO))
                audio.playAlertAudio()
                assertEquals(2, created.size)
                audio.handlePlaybackError(failed, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
                audio.playAlertAudio()
                assertEquals("Stale callback must not stop the new player", 2, created.size)
            } finally {
                audio.stopAudio()
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
            }
        }
    }

    private fun createLegacyDatabase(file: File, points: List<CO2WavePointData>) {
        SQLiteDatabase.openOrCreateDatabase(file, null).use { sqlite ->
            sqlite.execSQL(
                "CREATE TABLE patients (name TEXT NOT NULL, gender TEXT NOT NULL, age INTEGER NOT NULL, " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)"
            )
            sqlite.execSQL(
                "CREATE TABLE records (id TEXT NOT NULL PRIMARY KEY, patient TEXT NOT NULL, " +
                    "startTime TEXT NOT NULL, endTime TEXT NOT NULL, dateIndex INTEGER NOT NULL, " +
                    "patientIndex TEXT NOT NULL, isGroupTitle INTEGER NOT NULL, pdfFilePath TEXT, " +
                    "previewPdfFilePath TEXT, groupTitle TEXT NOT NULL, data TEXT NOT NULL)"
            )
            val record = Record(patient = Patient(name = "legacy fixture", age = 0),
                startTime = LocalDateTime.now(), endTime = LocalDateTime.now())
            val gson = Gson()
            sqlite.execSQL(
                "INSERT INTO records VALUES (?, ?, ?, ?, 0, '', 0, null, null, '', ?)",
                arrayOf(record.id.toString(), gson.toJson(record.patient), record.startTime.toString(),
                    record.endTime.toString(), gson.toJson(points))
            )
            sqlite.version = 1
        }
    }

    private fun createDatabase(file: File, label: String) {
        val database = AppDatabase.buildDatabase(context, file.absolutePath)
        try {
            seedRecord(database, label)
        } finally {
            database.close()
        }
    }

    private fun seedRecord(database: AppDatabase, label: String): Record {
        val now = LocalDateTime.now()
        val record = Record(patient = Patient(name = label, age = 0), startTime = now, endTime = now)
        database.recordDao().insertRecord(record)
        return record
    }

    private fun reportRunBoundary(runId: String, boundary: String) {
        if (runId.isEmpty()) return
        val requestId = CapnoCloudLog.publish(Level.INFO, "sls_fault_${runId}_$boundary")
            ?.get(45, TimeUnit.SECONDS)
        assertFalse("Missing SLS receipt for the $boundary marker", requestId.isNullOrBlank())
    }
}
