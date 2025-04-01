package com.wldmedical.capnoeasy.kits

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.mikephil.charting.charts.LineChart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.DATABASE_NS
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.LanguageTypes
import com.wldmedical.capnoeasy.USER_PREF_NS
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.PrintSetting
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

// 波形图记录下每条数据chunk最多点数
//val maxRecordDataChunkSize = 10000
// TODO: 临时测试
val maxRecordDataChunkSize = 100

// 对List<Record>进行扩展
// 将CO2WavePointData类型数据转换为原始二进制数组
fun List<CO2WavePointData>.compress(): ByteArray {
    val json = Gson().toJson(this)
    val outputStream = ByteArrayOutputStream()
    GZIPOutputStream(outputStream).use { gzipStream ->
        gzipStream.write(json.toByteArray())
    }
    return outputStream.toByteArray()
}

// 将二进制数据转换为List<CO2WavePointData>
fun ByteArray.decompressToCO2WavePointData(): List<CO2WavePointData> {
    val inputStream = ByteArrayInputStream(this)
    val json = GZIPInputStream(inputStream).bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<CO2WavePointData>>() {}.type
    return Gson().fromJson(json, type) // 将 JSON 字符串转换回 List<CO2WavePointData>
}

@Entity(tableName = "patients")
data class Patient(
    val name: String,
    val gender: GENDER = GENDER.MALE,
    val age: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Serializable

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = false) var id: UUID = UUID.randomUUID(),
    val patient: Patient,
    val startTime: LocalDateTime,
    var endTime: LocalDateTime,
    val dateIndex: Int = 0,
    val patientIndex: String = "",
    val isGroupTitle: Boolean = false,
    var pdfFilePath: String? = null,
    // 保留字段，但是不再设置值，防止App更新后，数据库表字段不一，导致报错
    var previewPdfFilePath: String? = null,
    val groupTitle: String = "",
): Serializable

@Entity(
    tableName = "co2_data",
    foreignKeys = [
        ForeignKey(
            entity = Record::class,
            parentColumns = ["id"],
            childColumns = ["recordId"],
            onDelete = ForeignKey.CASCADE // 如果删除 Record，则删除相关的 CO2Data
        )
    ],
    indices = [Index(value = ["recordId", "chunkIndex"], unique = true)] // 索引提高查询效率
)
data class CO2Data(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // 自增主键
    val recordId: UUID, // 外键，关联到 Record 表
    val chunkIndex: Int, //  表示这是第几个 6000 对象块
    @ColumnInfo(name = "data") val data: ByteArray // 存储压缩后的 List<CO2WavePointData>
)

enum class GROUP_BY {
    ALL,
    PATIENT,
    DATE
}

data class Group(
    val type: GROUP_BY = GROUP_BY.ALL,
    val name: String,
)

@Dao
interface PatientDao {
    @Insert
    fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patients")
    fun getAllPatients(): List<Patient>
}

// Patient类型转换器
class PatientConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromPatient(patient: Patient?): String? {
        return patient?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPatient(patJson: String?): Patient? {
        return patJson?.let { gson.fromJson(it, Patient::class.java) }
    }
}

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 使用 REPLACE 策略，避免主键冲突
    fun insertRecord(record: Record)

    @Query("SELECT * FROM records WHERE id = :recordId")
    fun getRecordById(recordId: UUID): Record?

    @Query("SELECT * FROM records")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM records WHERE id = :id")
    fun queryRecordById(id: UUID): Record?

    // 新增：流式获取所有记录
    @Query("SELECT * FROM records")
    fun getAllRecordsFlow(): Flow<List<Record>>

    // 新增：分页获取记录
    @Query("SELECT * FROM records ORDER BY startTime DESC LIMIT :limit OFFSET :offset") // 假设按 startTime 排序
    fun getRecordsPaged(limit: Int, offset: Int): List<Record>

    // 新增：获取记录总数
    @Query("SELECT COUNT(*) FROM records")
    fun getRecordCount(): Int
}

@Dao
interface CO2DataDao {
    // 插入压缩后的数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCO2Data(co2Data: CO2Data): Long

    // 根据 recordId 获取所有数据块 (按 chunkIndex 排序)
    @Query("SELECT * FROM co2_data WHERE recordId = :recordId ORDER BY chunkIndex")
    fun getCO2DataByRecordId(recordId: UUID): List<CO2Data>

    // 根据 recordId 获取所有数据块 (按 chunkIndex 排序)
    @Query("SELECT * FROM co2_data WHERE recordId = :recordId ORDER BY chunkIndex")
    fun getCO2DataByRecordIdFlow(recordId: UUID): Flow<List<CO2Data>>

    // 分页读取数据块
    @Query("SELECT * FROM co2_data WHERE recordId = :recordId ORDER BY chunkIndex LIMIT :limit OFFSET :offset")
    fun getCO2DataByRecordIdPaged(recordId: UUID, limit: Int, offset: Int): List<CO2Data>

    // 获取特定范围内的数据块
    @Query("SELECT * FROM co2_data WHERE recordId = :recordId AND chunkIndex BETWEEN :startChunkIndex AND :endChunkIndex ORDER BY chunkIndex")
    fun getCO2DataByRecordIdAndRange(recordId: UUID, startChunkIndex: Int, endChunkIndex: Int): List<CO2Data>

    // 获取数据块总数
    @Query("SELECT COUNT(*) FROM co2_data WHERE recordId = :recordId")
    fun getCO2DataCountByRecordId(recordId: UUID): Int

    // 获取特定数据块
    @Query("SELECT * FROM co2_data WHERE recordId = :recordId AND chunkIndex = :chunkIndex")
    fun getCO2DataByRecordIdAndChunkIndex(recordId: UUID, chunkIndex: Int): CO2Data?
}

class UUIDConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuidString: String?): UUID? {
        return uuidString?.let { UUID.fromString(it) }
    }
}

class LocalDateTimeConverters {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, formatter) }
    }
}

@Database(
    entities = [
        Patient::class,
        Record::class,
        CO2Data::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        UUIDConverters::class,
        PatientConverters::class,
        LocalDateTimeConverters::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDto(): PatientDao
    abstract fun recordDao(): RecordDao
    abstract fun co2DataDao(): CO2DataDao

    companion object {
        @Volatile // 确保多线程环境下的可见性
        private var INSTANCE: AppDatabase? = null

        // 获取数据库实例的静态方法
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NS
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * 数据本地存储
 * 1、存用户ETCO2数据
 * 2、保存用户APP设置: 链接方式、配对设置、打印设置
 * 3、启动时读取配置
 */
class LocalStorageKit @Inject constructor(
    @ActivityContext private val activity: Context,
    application: CapnoEasyApplication
) {
    // 打印设置相关key
    private val KEY_HOSPITAL_NAME = "hospital_name"
    private val KEY_REPORT_NAME = "report_name"
    private val KEY_OUTPUT_PDF = "is_output_pdf"
    private val KEY_PDFDEPART = "pdfDepart"
    private val KEY_PDFBEDNUMBER = "pdfBedNumber"
    private val KEY_PDFIDNUMBER = "pdfIDNumber"
    private val KEY_PATIENT_NAME = "patient_name"
    private val KEY_PATIENT_GENDER = "patient_gender"
    private val KEY_PATIENT_AGE = "patient_age"
    private val KEY_SHOW_TREND_CHART = "show_trend_chart"

    // 当前正在操作的UUID
    var currentRecordId: UUID? = null

    // 用户语言偏好
    private val KEY_LANGUAGE = "userLanguage"

    var patients: MutableList<Patient> = mutableListOf()

    var records: MutableList<Record> = mutableListOf()

    val state = mutableStateOf(GROUP_BY.ALL)

    val database = application.database

    /***
     * 保存病人
     * 在主页填写了病人信息并点击记录时候调用
     */
    suspend fun savePatient(
        patient: Patient
    ) {
        withContext(Dispatchers.IO) {
            database.patientDto().insertPatient(patient)
            patients.add(patient)
        }
    }

    // 向指定记录插入CO2数据帧
    suspend fun insertCO2DataForRecord(
        recordId: UUID,
        dataFlow: Flow<List<CO2WavePointData>>, // 接收数据的 Flow
        co2DataDao: CO2DataDao
    ) {
        val chunkSize = 6000
        var chunkIndex = 0
        dataFlow.collect { dataChunk -> // 收集 Flow 中的数据块
            if (dataChunk.isNotEmpty()) {
                val compressedData = dataChunk.compress()
                val co2Data = CO2Data(recordId = recordId, chunkIndex = chunkIndex, data = compressedData)
                co2DataDao.insertCO2Data(co2Data)
                chunkIndex++
            }
        }
    }

    // 读取数据
    fun getCO2DataForRecord(recordId: UUID, co2DataDao: CO2DataDao): Flow<List<CO2WavePointData>> =
        flow {
            // 从数据库流式读取数据块
            val co2DataChunksFlow = co2DataDao.getCO2DataByRecordIdFlow(recordId) // 需要在 CO2DataDao 中添加相应的方法
            co2DataChunksFlow.collect { co2DataChunks ->
                co2DataChunks.forEach { co2Data ->
                    // 解压缩每个数据块并发出
                    emit(co2Data.data.decompressToCO2WavePointData())
                }
            }
        }

    fun getCO2DataForRecordPaged(
        recordId: UUID,
        co2DataDao: CO2DataDao,
        pageSize: Int = 10 // 每页的数据块数量
    ): Flow<List<CO2WavePointData>> = flow {
        var offset = 0
        while (true) {
            val co2DataChunks = co2DataDao.getCO2DataByRecordIdPaged(recordId, pageSize, offset)
            if (co2DataChunks.isEmpty()) break // 没有更多数据
            val decompressedData = co2DataChunks.flatMap { it.data.decompressToCO2WavePointData() }
            emit(decompressedData)
            offset += pageSize
        }
    }

    /**
     * 停止记录
     * 1、清空当前 currentRecordId
     */
    suspend fun stopRecord(
        remainData: List<CO2WavePointData> = listOf()
    ) {
        // 停止记录时，将不足一个chunk的数据，单独保存起来，避免丢失数据
        currentRecordId?.let { it ->
            val chunkIndex = this.database.co2DataDao().getCO2DataByRecordId(it).size.coerceAtLeast(0)
            val remainCo2Data = CO2Data(
                recordId = it,
                chunkIndex = chunkIndex,
                data = remainData.compress()
            )
           this.database.co2DataDao().insertCO2Data(remainCo2Data)
        }
        currentRecordId = null
    }

    /***
     * 保存病人ETCO2记录
     * 在主页保存记录时候调用
     */
    suspend fun saveRecord(
        context: Context? = null,
        patient: Patient,
        startTime: LocalDateTime,
        recordName: String = "",
        endTime: LocalDateTime,
        maxETCO2: Float = 0f,
        lineChart: LineChart? = null,
        currentETCO2: Float = 0f,
        showTrendingChart: Boolean = true,
        currentRR: Int = 0,
    ) {
        withContext(Dispatchers.IO) {
            val dateIndex = generateDateIndex(startTime)
            val patientIndex = generatePatientIndex(patient)
            var pdfFilePath: String? = null
            val printSetting: PrintSetting? = context?.let { loadPrintSettingFromPreferences(it) }

            val formatter = DateTimeFormatter.ofPattern("yyyyMd_HHmmss")
            if (context != null) {
                pdfFilePath = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "${recordName}_${patient.name}_${patient.gender.title}_${patient.age}_${startTime.format(formatter)}.pdf"
                ).absolutePath
            }

            // 创建记录
            val record = Record(
                patient = patient,
                startTime = startTime,
                endTime = endTime,
                dateIndex = dateIndex,
                patientIndex = patientIndex,
                pdfFilePath = pdfFilePath,
            )
            // 记录当前正在操作的记录id
            currentRecordId = record.id

            if (pdfFilePath != null && lineChart != null && context != null) {
                saveChartToPdfInBackground(
                    lineChart = lineChart,
                    // TODO: 等待时机解决，目前存在问题 
                    data = listOf(),
//                    data = data,
                    filePath = pdfFilePath,
                    record = record,
                    maxETCO2 = maxETCO2,
                    currentETCO2 = currentETCO2,
                    currentRR = currentRR,
                    printSetting = printSetting,
                    showTrendingChart = showTrendingChart,
                    context = context
                )
            }

            database.recordDao().insertRecord(record)
            records.add(record)
        }
    }

    /***
     * 从本地读取历史记录数据
     */
//    suspend fun readRecordsFromLocal(): List<Record> {
//        return withContext(Dispatchers.IO) {
//            database.recordDao().getAllRecords().toList().firstOrNull() ?: emptyList()
//        }
//    }

    /***
     * 从本地读取历史病人数据
     */
//    suspend fun readPatientsFromLocal(): List<Patient> {
//        return withContext(Dispatchers.IO) {
//            database.patientDto().getAllPatients()
//        }
//    }

    /***
     * 生成记录的病人分组索引：姓名+性别+年龄
     */
    private fun generatePatientIndex(patient: Patient): String {
        return patient.name + patient.gender.title + patient.age.toString()
    }

    /***
     * 生成记录的时间分组索引：年月日
     */
    private fun generateDateIndex(startTime: LocalDateTime): Int {
        val year = startTime.year
        val month = startTime.monthValue
        val day= startTime.dayOfMonth
        return year * 10000 + month * 100 + day
    }

    /***
     * 生成记录的时间分组索引：时分秒
     */
//    private fun generateTimeIndex(startTime: LocalDateTime): Int {
//        val hour = startTime.hour
//        val minute = startTime.minute
//        val second= startTime.second
//        return hour * 10000 + minute * 100 + second
//    }

    /***
     * 保存用户语言选择
     */
    fun saveUserLanguageToPreferences(
        context: Context,
        language: String = "zh",
    ) {
        val prefs = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        prefs.edit().apply {
            language?.let { putString(KEY_LANGUAGE, it) } ?: remove(KEY_LANGUAGE)
        }.apply()
    }

    // 读取用户语言
    fun loadUserLanguageFromPreferences(context: Context): LanguageTypes {
        val prefs = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        val language = prefs.getString(KEY_LANGUAGE, null)

        if (language == "en") {
            return LanguageTypes.ENGLISH
        }
        return LanguageTypes.CHINESE
    }

    /***
     * 用户打印偏好，保存到本地
     */
    fun saveUserPrintSettingToPreferences(
        context: Context,
        hospitalName: String? = null,
        reportName: String? = null,
        isPDF: Boolean = true,
        depart: String? = null,
        bedNumber: String? = null,
        idNumber: String? = null,
        name: String? = null,
        gender: String? = null,
        age: Int? = null,
        showTrendingChart: Boolean? = null
    ) {
        val prefs = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        prefs.edit().apply {
            hospitalName?.let { putString(KEY_HOSPITAL_NAME, it) }
            reportName?.let { putString(KEY_REPORT_NAME, it) }
            putBoolean(KEY_OUTPUT_PDF, isPDF)
            depart?.let { putString(KEY_PDFDEPART, it) }
            bedNumber?.let { putString(KEY_PDFBEDNUMBER, it) }
            idNumber?.let { putString(KEY_PDFIDNUMBER, it) }
            name?.let { putString(KEY_PATIENT_NAME, it) }
            gender?.let { putString(KEY_PATIENT_GENDER, it) }
            age?.let { putInt(KEY_PATIENT_AGE, it) }
            showTrendingChart?.let { putBoolean(KEY_SHOW_TREND_CHART, it) }
        }.apply()
    }

    // 读取用户偏好打印设置
    fun loadPrintSettingFromPreferences(context: Context): PrintSetting {
        val prefs = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        PrintSetting.hospitalName = prefs.getString(KEY_HOSPITAL_NAME, null)
        PrintSetting.reportName = prefs.getString(KEY_REPORT_NAME, null)
        PrintSetting.isPDF = prefs.getBoolean(KEY_OUTPUT_PDF, true)
        PrintSetting.pdfDepart = prefs.getString(KEY_PDFDEPART, null)
        PrintSetting.pdfBedNumber = prefs.getString(KEY_PDFBEDNUMBER, null)
        PrintSetting.pdfIDNumber = prefs.getString(KEY_PDFIDNUMBER, null)
        PrintSetting.name = prefs.getString(KEY_PATIENT_NAME, null)
        PrintSetting.gender = prefs.getString(KEY_PATIENT_GENDER, null)
        PrintSetting.age = prefs.getInt(KEY_PATIENT_AGE, 0)
        PrintSetting.showTrendingChart = prefs.getBoolean(KEY_SHOW_TREND_CHART, true)

        return PrintSetting
    }

    suspend fun mock() {
        // 添加两个病人
        savePatient(Patient(name = "病人A", age = 90, gender = GENDER.MALE))
        savePatient(Patient(name = "病人B", age = 80, gender = GENDER.FORMALE))

        val date1: LocalDateTime = LocalDateTime.now()
        val date2: LocalDateTime = LocalDateTime.parse("2007-12-03T10:15:30")

        // 给A病人添加5条记录
        saveRecord(patient = patients[0], startTime = date1, endTime = date1)
        saveRecord(patient = patients[0], startTime = date1, endTime = date1)
        saveRecord(patient = patients[0], startTime = date1, endTime = date1)
        saveRecord(patient = patients[0], startTime = date2, endTime = date2)
        saveRecord(patient = patients[0], startTime = date2, endTime = date2)

        // 给B病人添加2条记录
        saveRecord(patient = patients[1], startTime = date1, endTime = date1)
        saveRecord(patient = patients[1], startTime = date1, endTime = date1)
        saveRecord(patient = patients[1], startTime = date2, endTime = date2)
    }
}

object LocalStorageKitManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var localStorageKit: LocalStorageKit

    fun initialize(
        activity: Context,
        application: CapnoEasyApplication,
    ) {
        if (::localStorageKit.isInitialized) {
            return
        }

        localStorageKit = LocalStorageKit(
            activity = activity,
            application = application
        )
    }
}
