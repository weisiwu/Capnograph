package com.wldmedical.capnoeasy.kits

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
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
import com.wldmedical.capnoeasy.components.formatter
import com.wldmedical.capnoeasy.models.CO2WavePointData
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

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

val Groups = listOf(
    Group(name = "全部", type = GROUP_BY.ALL),
    Group(name = "病人", type = GROUP_BY.PATIENT),
    Group(name = "时间", type = GROUP_BY.DATE),
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

    var patients: MutableList<Patient> = mutableListOf()

    var records: MutableList<Record> = mutableListOf()

    val state = mutableStateOf(GROUP_BY.ALL)

    val database = application.database

    // 将字符串解析为 LocalDateTime 对象
    fun handleTime(str: String): LocalDateTime? {
        try {
            return LocalDateTime.parse(str, formatter)
        } catch (e: DateTimeParseException) {
            println("Invalid date time format: ${e.message}") // 捕获并处理解析异常
            return null
        }
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
        data: List<CO2WavePointData> = listOf(),
        endTime: LocalDateTime,
        lineChart: LineChart? = null
    ) {
        withContext(Dispatchers.IO) {
            val dateIndex = generateDateIndex(startTime)
            val patientIndex = generatePatientIndex(patient)
            var pdfFilePath: String? = null

            if (context != null) {
                pdfFilePath = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "${patientIndex}_${dateIndex}.pdf"
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

            if (pdfFilePath != null && lineChart != null) {
                saveChartToPdfInBackground(
                    lineChart = lineChart,
                    data = data,
                    segmentSize = maxXPoints,
                    filePath = pdfFilePath,
                )
                println("wswTest 保存pdf文件的地址是什么 ${pdfFilePath}")
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
     * 生成记录的时间分组索引：姓名+性别+年龄
     */
    private fun generateDateIndex(startTime: LocalDateTime): Int {
        val year = startTime.year
        val month = startTime.monthValue
        val day= startTime.dayOfMonth
        return year * 10000 + month * 100 + day
    }

    /***
     * 将用户保存在本地的数据，持久化存储到本地。防止App升级，更新导致丢失
     */
    public fun saveUserDataPersistent() {  }

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
