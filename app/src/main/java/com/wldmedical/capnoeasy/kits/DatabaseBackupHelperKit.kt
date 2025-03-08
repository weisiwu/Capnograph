import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.DATABASE_NS
import com.wldmedical.capnoeasy.USER_PREF_NS
import com.wldmedical.capnoeasy.kits.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

const val IS_FIRST_LAUNCH = "is_wld_medical_app_first_install"
const val DATA_LATEST_VERSION = "last_data_version"

class DatabaseBackupHelper(private val context: Context) {

    private val databaseName = DATABASE_NS
    // 备份目录名称
    private val backupDirectoryName = "CapnoEasyApplicationDatabaseBackup"
    // 数据版本，用来判断是否需要备份
    private var lastDataVersion: Int = -1
    private lateinit var roomDatabase: AppDatabase
    val prefs: SharedPreferences = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)

    // 恢复进程开始工作,按照如下流程
    // 1、判断是否为首次安装
    // 2、如是，则判断是否有历史数据库文件，如有，开始恢复进程
    // 3、非首次安装，判断本地数据和历史数据是否相同，并开始备份
    @RequiresApi(Build.VERSION_CODES.Q)
    fun startWork(
        context: Context,
        database: AppDatabase
    ) {
        val isFirstLaunch = prefs.getBoolean(IS_FIRST_LAUNCH, true)
        lastDataVersion = prefs.getInt(DATA_LATEST_VERSION, -1)
        roomDatabase = database

        runBlocking {
            println("wswTest 是否首次安装: ${isFirstLaunch} ")
            // 首次安装，修改标志为false，然后退出
            if (isFirstLaunch) {
                prefs.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
                try {
                    println("wswTest 开始恢复数据")
                    val databaseFile = context.getDatabasePath(databaseName)
                    // 如果没有历史数据库文件，则退出
                    if (!databaseFile.exists()) {
                        Log.d("DatabaseBackupHelper", "Database file does not exist.")
                    } else {
                        // 开始恢复
                        restoreDatabase()
                    }
                } catch (e: IOException) {
                    Log.e("DatabaseBackupHelper", "恢复数据过程中遇到问题: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                try {
                    val databaseFile = context.getDatabasePath(databaseName)
                    println("wswTest 开始备份数据")
                    // 如果没有历史数据库文件，则退出
                    if (!databaseFile.exists()) {
                        Log.d("DatabaseBackupHelper", "Database file does not exist.")
                    } else {
                        // 开始备份
                        backupDatabase(databaseFile)
                    }
                } catch (e: IOException) {
                    Log.e("DatabaseBackupHelper", "备份数据遇到异常")
                }
            }
        }
    }

    // 备份数据库到共享存储
    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun backupDatabase(databaseFile: File) {
        withContext(Dispatchers.IO) {
            try {
                // 获取当前的 data_version
//                val currentDataVersion = roomDatabase
//                    .openHelper
//                    .readableDatabase
//                    .compileStatement("PRAGMA data_version")
//                    .simpleQueryForLong()
//                    .toInt()
//                println("wswTest 备份前对比已备份数据和数据库数据差别: 数据库数据版本:${currentDataVersion} 已备份数据版本:${lastDataVersion}")

                // 关闭数据库为后续操作做准备
                roomDatabase.close()

//                // 如果当前的 data_version 与 lastDataVersion 相同，则表示数据库没有变化，直接返回
//                if (currentDataVersion == lastDataVersion) {
//                    println("wswTest 数据没有变化,不重复备份")
//                    Log.d("DatabaseBackupHelper", "Database has not changed since last backup.")
//                } else {
//                }

                // TODO:(wsw) 先将数据放到download目录下，迁移路径的事情后续说
                val contentResolver: ContentResolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, databaseName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName
                    )
                }

                val uri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    // 开始复制数据库
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        FileInputStream(databaseFile).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    // 复制预写日志文件
                    val walFile = File(databaseFile.parent, "$databaseName-wal")
                    if (walFile.exists()) {
                        val walContentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, "$databaseName-wal")
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                            put(
                                MediaStore.MediaColumns.RELATIVE_PATH,
                                Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName
                            )
                        }
                        val walUri: Uri? = contentResolver.insert(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            walContentValues
                        )
                        if (walUri != null) {
                            contentResolver.openOutputStream(walUri)?.use { outputStream ->
                                FileInputStream(walFile).use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }

                    // 复制共享内存索引
                    val shmFile = File(databaseFile.parent, "$databaseName-shm")
                    if (shmFile.exists()) {
                        val shmContentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, "$databaseName-shm")
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName)
                        }
                        val shmUri: Uri? = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, shmContentValues)
                        if (shmUri != null) {
                            contentResolver.openOutputStream(shmUri)?.use { outputStream ->
                                FileInputStream(shmFile).use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }

                    // 备份完成后，更新 lastDataVersion 的值为当前的 data_version 值
//                    lastDataVersion = currentDataVersion
//                    prefs.edit().putInt(DATA_LATEST_VERSION, currentDataVersion).apply()
                    println("wswTest 备份完成，修改本地最新数据版本")

                    Log.d("DatabaseBackupHelper", "Database backed up successfully.")
                } else {
                    Log.e("DatabaseBackupHelper", "Failed to create backup file in shared storage.")
                }
            } catch (e: IOException) {
                Log.e("DatabaseBackupHelper", "Error backing up database", e)
            }
        }
    }

    // 从共享存储恢复数据库
    @RequiresApi(Build.VERSION_CODES.Q)
    fun restoreDatabase() {
        try {
            println("wswTest 开始恢复数据库---执行道路及中去了")
            val databaseFile = context.getDatabasePath(databaseName)
            println("wswTest 数据库databaseFile ${databaseFile.absolutePath}")
            val contentResolver: ContentResolver = context.contentResolver
            val projection = arrayOf(
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH,
                MediaStore.MediaColumns._ID
            )
            val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?"
            val selectionArgs = arrayOf(databaseName)
            val query = contentResolver.query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            query?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val displayNameIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    val relativePathIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
                    val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)

                    val displayName = cursor.getString(displayNameIndex)
                    val relativePath = cursor.getString(relativePathIndex)
                    val id = cursor.getLong(idIndex)

                    Log.d("DatabaseBackupHelper", "Found backup file: $displayName in $relativePath")

                    val backupFileUri =
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI.buildUpon()
                            .appendPath(id.toString())
                            .build()

                    // Close the database before copying
                    val roomDatabase = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        databaseName
                    ).build()
                    roomDatabase.close()

                    contentResolver.openInputStream(backupFileUri)?.use { inputStream ->
                        FileOutputStream(databaseFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    // Restore WAL and SHM files if they exist
                    restoreAuxiliaryFile(databaseName, "-wal", contentResolver, databaseFile)
                    restoreAuxiliaryFile(databaseName, "-shm", contentResolver, databaseFile)

                    Log.d("DatabaseBackupHelper", "Database restored successfully.")
                } else {
                    Log.d("DatabaseBackupHelper", "No backup file found.")
                }
            }
        } catch (e: IOException) {
            Log.e("DatabaseBackupHelper", "Error restoring database", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun restoreAuxiliaryFile(
        databaseName: String,
        suffix: String,
        contentResolver: ContentResolver,
        databaseFile: File
    ) {
        val auxiliaryFileName = "$databaseName$suffix"
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.RELATIVE_PATH,
            MediaStore.MediaColumns._ID
        )
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf(auxiliaryFileName)
        val query = contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val relativePathIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)

                val displayName = cursor.getString(displayNameIndex)
                val relativePath = cursor.getString(relativePathIndex)
                val id = cursor.getLong(idIndex)

                Log.d("DatabaseBackupHelper", "Found auxiliary file: $displayName in $relativePath")

                val auxiliaryFileUri =
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendPath(id.toString())
                        .build()
                val auxiliaryFile = File(databaseFile.parent, auxiliaryFileName)
                contentResolver.openInputStream(auxiliaryFileUri)?.use { inputStream ->
                    FileOutputStream(auxiliaryFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }
}

object DatabaseBackupHelperManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var dbBackupHelperKit: DatabaseBackupHelper

    fun initialize(
        application: CapnoEasyApplication,
//        context: Context,
    ) {
        if (::dbBackupHelperKit.isInitialized) {
            return
        }

        dbBackupHelperKit = DatabaseBackupHelper(
            context = application,
//            context = context
        )
    }
}
