import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
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
    private val STORAGE_PERMISSION_CODE = 101
    val prefs: SharedPreferences = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)

    // 恢复进程开始工作,按照如下流程
    // 1、判断是否为首次安装
    // 2、如是，则判断是否有历史数据库文件，如有，开始恢复进程
    // 3、非首次安装，判断本地数据和历史数据是否相同，并开始备份
    @RequiresApi(Build.VERSION_CODES.Q)
    fun startWork(
        context: Context,
        database: AppDatabase,
        isRestore: Boolean = false
    ) {
        val isFirstLaunch = prefs.getBoolean(IS_FIRST_LAUNCH, true)
        lastDataVersion = prefs.getInt(DATA_LATEST_VERSION, -1)
        roomDatabase = database

        runBlocking {
            println("wswTest 是否首次安装: ${isFirstLaunch} ")
            // 首次安装，修改标志为false，然后退出
            if (isFirstLaunch || isRestore) {
                prefs.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
                // 无论是否存在数据库文件，都尝试恢复
                try {
                    restoreDatabase()
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
                        println("wswTest 备份失败，没有找到数据库文件")
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

    // 备份DB数据文件
    @RequiresApi(Build.VERSION_CODES.Q)
    fun backupDBFile(
        contentResolver: ContentResolver,
        databaseFile: File
    ) {
        println("wswTest[backupDBFile] 开始备份DB")
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, databaseName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName
            )
        }

        val _uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        // 查询是否有应用的数据存储文件
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(databaseName)
        val query = contentResolver.query(
            _uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        // 查询DB文件是否存在
        var existingDBFileUri: Uri? = null
        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val id = cursor.getLong(idIndex)
                existingDBFileUri = _uri.buildUpon()
                    .appendPath(id.toString())
                    .build()
            }
        }

        // 开始备份DB数据文件
        if (existingDBFileUri == null) {
            val uri: Uri? = contentResolver.insert(_uri, contentValues)

            // 先判断是否可以在Download中进行保存，不可以直接退出
            if (uri == null) {
                return
            }

            println("wswTest[backupDBFile] 开始新建DB文件")
            // 开始复制数据库
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(databaseFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupDBFile] 备份完成-新建立数据库DB文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        } else {
            // 如果文件已经存在，则更新
            contentResolver.update(existingDBFileUri!!, contentValues, null, null)

            println("wswTest[backupDBFile] 开始复制DB文件")
            // 使用 "wt" 模式打开输出流
            contentResolver.openOutputStream(existingDBFileUri!!, "wt")?.use { outputStream ->
                // 将数据复制到输出流
                FileInputStream(databaseFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupDBFile] 备份完成-更新数据库DB文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        }
    }

    // 备份预写日志文件
    @RequiresApi(Build.VERSION_CODES.Q)
    fun backupWALFile(
        contentResolver: ContentResolver,
        databaseFile: File
    ) {
        val walFileName = "$databaseName-wal"
        val walFile = File(databaseFile.parent, walFileName)

        if (!walFile.exists()) {
            println("wswTest[backupWALFile] WAL file does not exist.")
            return
        }

        println("wswTest[backupWALFile] 开始备份WAL")
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, walFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName
            )
        }

        // 是否存在WAL文件
        val _uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(walFileName)
        val query = contentResolver.query(
            _uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        // 查询预写日志文件是否存在
        var existingWALFileUri: Uri? = null
        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val id = cursor.getLong(idIndex)
                existingWALFileUri = _uri.buildUpon()
                    .appendPath(id.toString())
                    .build()
            }
        }

        // 开始备份预写日志文件
        if (existingWALFileUri == null) {
            val uri: Uri? = contentResolver.insert(_uri, contentValues)

            if (uri == null) {
                return
            }

            println("wswTest[backupWALFile] 开始新建文件")
            // 复制预写日志文件
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(walFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupWALFile] 备份完成-新建文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        } else {
            // 复制预写日志文件
            contentResolver.update(existingWALFileUri!!, contentValues, null, null)

            println("wswTest[backupWALFile] 开始复制文件")
            contentResolver.openOutputStream(existingWALFileUri!!, "wt")?.use { outputStream ->
                FileInputStream(walFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupWALFile] 备份完成-更新文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        }
    }

    // 备份共享内存索引文件
    @RequiresApi(Build.VERSION_CODES.Q)
    fun backupSHMFile(
        contentResolver: ContentResolver,
        databaseFile: File
    ) {
        val shmFileName = "$databaseName-shm"
        val shmFile = File(databaseFile.parent, shmFileName)

        if (!shmFile.exists()) {
            println("wswTest[backupSHMFile] SHM file does not exist.")
            return
        }

        println("wswTest[backupSHMFile] 开始备份SHM")
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, shmFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + File.separator + backupDirectoryName
            )
        }

        // 是否存在SHM文件
        val _uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(shmFileName)
        val query = contentResolver.query(
            _uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        // 查询共享内存索引文件是否存在
        var existingSHMFileUri: Uri? = null
        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val id = cursor.getLong(idIndex)
                existingSHMFileUri = _uri.buildUpon()
                    .appendPath(id.toString())
                    .build()
            }
        }

        // 开始备份共享内存索引文件
        if (existingSHMFileUri == null) {
            val uri: Uri? = contentResolver.insert(_uri, contentValues)

            if (uri == null) {
                return
            }

            println("wswTest[backupSHMFile] 开始新建文件")
            // 复制共享内存索引文件
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(shmFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupSHMFile] 备份完成-新建文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        } else {
            // 复制共享内存索引文件
            contentResolver.update(existingSHMFileUri!!, contentValues, null, null)

            println("wswTest[backupSHMFile] 开始复制文件")
            contentResolver.openOutputStream(existingSHMFileUri!!, "wt")?.use { outputStream ->
                FileInputStream(shmFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            println("wswTest[backupSHMFile] 备份完成-更新文件")
            Log.d("DatabaseBackupHelper", "Database backed up successfully.")
        }
    }

    /**
     * 备份数据文件
     * 1、DB数据文件
     * 2、共享内存索引文件
     * 3、预写日志文件
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun backupDatabase(databaseFile: File) {
        withContext(Dispatchers.IO) {
            try {
                // 关闭数据库为后续操作做准备
                roomDatabase.close()

                // TODO:(wsw) 先将数据放到download目录下，迁移路径的事情后续说
                val contentResolver: ContentResolver = context.contentResolver

                // 备份DB文件
                backupDBFile(contentResolver, databaseFile)

                // 备份预写日志文件
                backupWALFile(contentResolver, databaseFile)

                // 备份共享内存索引文件
                backupSHMFile(contentResolver, databaseFile)

            } catch (e: IOException) {
                Log.e("DatabaseBackupHelper", "Error backing up database", e)
            }
        }
    }

    // 从共享存储恢复数据库
    @RequiresApi(Build.VERSION_CODES.Q)
    fun restoreDatabase() {
        try {
            println("wswTest[restoreDatabase] 开始恢复数据库")

            // 1. 获取数据库文件、WAL 文件和 SHM 文件的 File 对象
            val databaseFile = context.getDatabasePath(databaseName)
            val walFile = File(databaseFile.parent, "$databaseName-wal")
            val shmFile = File(databaseFile.parent, "$databaseName-shm")

            // 2. 关闭当前数据库连接
            roomDatabase.close()

            // 3. 删除当前数据库文件、WAL 文件和 SHM 文件（如果存在）
            if (databaseFile.exists()) {
                databaseFile.delete()
            }
            if (walFile.exists()) {
                walFile.delete()
            }
            if (shmFile.exists()) {
                shmFile.delete()
            }
            println("wswTest[restoreDatabase] 删除已有的当前数据库文件，从历史数据中恢复")

            val backupFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$backupDirectoryName/$DATABASE_NS"
            )
            if (backupFile.exists()) {
                // 获取应用数据库目录
                val dbPath = context.getDatabasePath(DATABASE_NS).absolutePath
                val dbFile = File(dbPath)
                try {
                    // 创建目标数据库文件
                    dbFile.parentFile?.mkdirs() // 确保目录存在
                    if (dbFile.exists()) {
                        dbFile.delete() // 如果数据库已经存在，先删除
                    }
                    // 复制备份文件到数据库文件
                    backupFile.copyTo(dbFile, overwrite = true)

                    val walFile = File(backupFile.absolutePath + "-wal")
                    val shmFile = File(backupFile.absolutePath + "-shm")
                    walFile.copyTo(File(dbPath + "-wal"), overwrite = true)
                    shmFile.copyTo(File(dbPath + "-shm"), overwrite = true)

                    Log.d("DatabaseRestore", "wswTest 数据库恢复成功!")
                } catch (e: Exception) {
                    Log.e("DatabaseRestore", "wswTest 恢复数据库时出错: ${e.message}")
                }
            }

            // 9. 重新打开数据库连接
            roomDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                databaseName
            ).build()

            println("wswTest[restoreDatabase] 恢复数据库完成")
        } catch (e: Exception) {
            Log.e("DatabaseBackupHelper", "Error restoring database", e)
        }
    }
}

object DatabaseBackupHelperManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var dbBackupHelperKit: DatabaseBackupHelper

    fun initialize(
        application: CapnoEasyApplication,
    ) {
        if (::dbBackupHelperKit.isInitialized) {
            return
        }

        dbBackupHelperKit = DatabaseBackupHelper(
            context = application,
        )
    }
}
