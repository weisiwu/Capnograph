import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.reflect.TypeToken
import com.wldmedical.capnoeasy.kits.compress
import com.wldmedical.capnoeasy.kits.maxRecordDataChunkSize
import com.wldmedical.capnoeasy.models.CO2WavePointData

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        println("wswTEst 是否需要对数据库进行升级")
        // 步骤 1: 重命名旧的 Record 表
        database.execSQL("ALTER TABLE records RENAME TO records_old")

        // 步骤 2: 创建新的 records 表
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `records` (`id` TEXT NOT NULL, `patient` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `dateIndex` INTEGER NOT NULL, `patientIndex` TEXT NOT NULL, `isGroupTitle` INTEGER NOT NULL, `pdfFilePath` TEXT, `previewPdfFilePath` TEXT, `groupTitle` TEXT NOT NULL, PRIMARY KEY(`id`))
        """)

        // 步骤 3: 将旧 Record 表的数据复制到新的 records 表
        database.execSQL("""
            INSERT INTO records (id, patient, startTime, endTime, dateIndex, patientIndex, isGroupTitle, pdfFilePath, previewPdfFilePath, groupTitle)
            SELECT id, patient, startTime, endTime, dateIndex, patientIndex, isGroupTitle, pdfFilePath, previewPdfFilePath, groupTitle
            FROM records_old
        """)

        // 步骤 4: 创建新的 co2_data 表
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `co2_data` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `recordId` TEXT NOT NULL, `chunkIndex` INTEGER NOT NULL, `trendData` TEXT NOT NULL, `data` BLOB NOT NULL, FOREIGN KEY(`recordId`) REFERENCES `records`(`id`) ON DELETE CASCADE, UNIQUE(`recordId`, `chunkIndex`))
        """)

        // 步骤 5: 迁移 CO2WavePointData 到 co2_data 表
        // 注意：这部分需要读取旧表的数据，进行反序列化和序列化，因此需要在代码中完成
        database.query("SELECT * FROM records_old").use { cursor ->
            while (cursor.moveToNext()) {
                val recordId = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val dataString = cursor.getString(cursor.getColumnIndexOrThrow("data"))

                // 反序列化 List<CO2WavePointData>
                val co2WavePointDataList: List<CO2WavePointData> = try {
                    val gson = com.google.gson.Gson()
                    val type = object : TypeToken<List<CO2WavePointData>>() {}.type
                    gson.fromJson<List<CO2WavePointData>>(dataString, type)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }

                // 将 List<CO2WavePointData> 分块并插入到新的 co2_data 表
                co2WavePointDataList.chunked(maxRecordDataChunkSize).forEachIndexed { index, chunk ->
                    // 假设您需要将 chunk 序列化并压缩为 ByteArray
                    val chunkDataBytes: ByteArray = try {
                        chunk.compress()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ByteArray(0)
                    }

                    // 插入到新的 co2_data 表
                    val contentValues = ContentValues().apply {
                        put("recordId", recordId)
                        put("chunkIndex", index)
                        put("trendData", "") // 您可能需要根据您的逻辑生成 trendData
                        put("data", chunkDataBytes)
                    }
                    database.insert("co2_data", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
                }
            }
        }

        // 步骤 6: 删除旧的 records_old 表
        database.execSQL("DROP TABLE records_old")
    }
}