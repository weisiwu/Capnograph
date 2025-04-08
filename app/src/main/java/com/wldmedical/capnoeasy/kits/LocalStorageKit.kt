package com.wldmedical.capnoeasy.kits

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
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
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.USER_PREF_NS
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.PrintSetting
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.util.UUID

// 最多记录数量
val maxRecordsNumber = 100

// 1M字节常量
val spaceSizeUnit = 1024 * 1024

// 单条记录在存储中能占据的最大存储空间(单位: M)
// 28800 * 10 * 36 8个小时总秒数 * 每秒10个点 * 每个点占用的内存空间
val singleRecordMaxSize = 10

val singleRecordMaxPointsNumber = 28800 * 10

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
    val data: List<CO2WavePointData> = listOf(),
    val groupTitle: String = "",
): Serializable

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

// CO2WavePointData类型转换器
class CO2WavePointDataConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromCO2WavePointData(waveData: List<CO2WavePointData>?): String? {
        return waveData?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCO2WavePointData(json: String?): List<CO2WavePointData>? {
        val type = object : TypeToken<List<CO2WavePointData>>() {}.type
        return json?.let { gson.fromJson(it, type) }
    }
}

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(record: Record)

    @Query("SELECT * FROM records")
    fun getAllRecords(): List<Record>

    @Query("SELECT * FROM records WHERE id = :id")
    fun queryRecordById(id: UUID): Record?

    @Query("SELECT * FROM records ORDER BY startTime ASC LIMIT 1")
    fun queryOldestRecord(): Record?

    @Query("DELETE FROM records WHERE id = :recordId")
    fun deleteRecordById(recordId: UUID)

    // 新增：计算 records 表中的记录数量
    @Query("SELECT COUNT(*) FROM records")
    fun getRecordsCount(): Int
}

class RecordConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromRecord(record: Record?): String? {
        return record?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toRecord(recordJson: String?): Record? {
        return recordJson?.let { gson.fromJson(it, Record::class.java) }
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

@Database(entities = [Patient::class, Record::class], version = 1)
@TypeConverters(
    value = [
        PatientConverters::class,
        RecordConverters::class,
        LocalDateTimeConverters::class,
        CO2WavePointDataConverters::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDto(): PatientDao
    abstract fun recordDao(): RecordDao

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

    // 用户语言偏好
    private val KEY_LANGUAGE = "userLanguage"

    var patients: MutableList<Patient> = mutableListOf()

    var records: MutableList<Record> = mutableListOf()

    val state = mutableStateOf(GROUP_BY.ALL)

    val database = application.database

    // 查询设备还剩多少物理存储空间
    fun getAvailableSpace(context: Context?): Long {
        context?.let {
            // 计算应用内部存储空间，room数据库的数据存储在应用目录下，不考虑放到公共存储空间下。
            val stat = StatFs(context.filesDir.path)
            val availableBlocks: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                stat.availableBlocksLong
            } else {
                stat.availableBlocks.toLong()
            }
            val blockSize: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                stat.blockSizeLong
            } else {
                stat.blockSize.toLong()
            }
            return availableBlocks * blockSize / spaceSizeUnit
        }
        return 0L
    }

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

    /***
     * 保存病人ETCO2记录
     * 在主页保存记录时候调用
     */
    suspend fun saveRecord(
        context: Context? = null,
        patient: Patient,
        startTime: LocalDateTime,
        recordName: String = "",
        data: List<CO2WavePointData> = listOf(),
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

            val record = Record(
                patient = patient,
                startTime = startTime,
                endTime = endTime,
                data = data,
                dateIndex = dateIndex,
                patientIndex = patientIndex,
                pdfFilePath = pdfFilePath,
            )

            if (pdfFilePath != null && lineChart != null && context != null) {
                 saveChartToPdfInBackground(
                    lineChart = lineChart,
                    data = data,
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

            context?.let {
                val remainInnerSpace = getAvailableSpace(context = context)
                // 目前策略: 1、如果存储空间不足，toast然后退出  2、如果数据量过多，超过100条，删除一条最久的历史记录继续记录
                // 剩余空间不足
                if (remainInnerSpace < singleRecordMaxSize) {
                    Toast.makeText(context, getString(R.string.localstorage_lackof_memory, context = context), Toast.LENGTH_SHORT).show()
                    return@withContext
                }
            }
            // 多余100个，开始删除最旧历史数据
            if (database.recordDao().getRecordsCount() >= maxRecordsNumber) {
                database.recordDao().queryOldestRecord()?.let {
                    database.recordDao().deleteRecordById(it.id)
                }
            }
            database.recordDao().insertRecord(record)
            records.add(record)
        }
    }

    /***
     * 从本地读取历史记录数据
     */
    suspend fun readRecordsFromLocal(): List<Record> {
        return withContext(Dispatchers.IO) {
            database.recordDao().getAllRecords()
        }
    }

    /***
     * 从本地读取历史病人数据
     */
    suspend fun readPatientsFromLocal(): List<Patient> {
        return withContext(Dispatchers.IO) {
            database.patientDto().getAllPatients()
        }
    }

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
    private fun generateTimeIndex(startTime: LocalDateTime): Int {
        val hour = startTime.hour
        val minute = startTime.minute
        val second= startTime.second
        return hour * 10000 + minute * 100 + second
    }

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
