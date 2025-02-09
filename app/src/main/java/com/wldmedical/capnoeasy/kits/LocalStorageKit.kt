package com.wldmedical.capnoeasy.kits

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.components.formatter
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.LocalDateTime
import javax.inject.Inject
import java.io.Serializable
import java.time.format.DateTimeParseException
import java.util.UUID

data class Patient(
    val name: String,
    val gender: GENDER = GENDER.MALE,
    val age: Int
): Serializable

data class Record(
    var id: UUID = UUID.randomUUID(),
    val patient: Patient,
    val startTime: LocalDateTime,
    var endTime: LocalDateTime,
    val dateIndex: Int = 0,
    val patientIndex: String = "",
    val isGroupTitle: Boolean = false,
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

/**
 * 数据本地存储
 * 1、存用户ETCO2数据
 * 2、保存用户APP设置: 链接方式、配对设置、打印设置
 * 3、启动时读取配置
 */
class LocalStorageKit @Inject constructor(
    @ActivityContext private val activity: Activity,
) {

    var patients: MutableList<Patient> = mutableListOf()

    var records: MutableList<Record> = mutableListOf()

    val state = mutableStateOf(GROUP_BY.ALL)

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
    fun savePatient(
        name: String = "",
        gender: GENDER,
        age: Int = 0
    ) {
        patients.add(Patient(name, gender, age))
    }

    /***
     * 保存病人ETCO2记录
     * 在主页保存记录时候调用
     */
    fun saveRecord(
        patient: Patient,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        ) {
        records.add(Record(
            patient = patient,
            startTime = startTime,
            endTime = endTime,
            dateIndex = generateDateIndex(startTime),
            patientIndex = generatePatientIndex(patient),
        ))
    }

    /***
     * 将当前数据存储到本地
     */
    fun saveToLocal() {}

    /***
     * 从本地读取数据
     */
    fun readFromLocal() {}

    /***
     * 生成记录的病人索引：姓名+性别+年龄
     */
    private fun generatePatientIndex(patient: Patient): String {
        return patient.name + patient.gender.title + patient.age.toString()
    }

    /***
     * 生成记录的时间索引：姓名+性别+年龄
     */
    private fun generateDateIndex(startTime: LocalDateTime): Int {
        val year = startTime.year
        val month = startTime.monthValue
        val day= startTime.dayOfMonth
        return year * 10000 + month * 100 + day
    }

    fun mock() {
        // 添加两个病人
        savePatient(name = "病人A", age = 90, gender = GENDER.MALE)
        savePatient(name = "病人B", age = 80, gender = GENDER.FORMALE)

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
        activity: Activity
    ) {
        if (::localStorageKit.isInitialized) {
            return
        }
        localStorageKit = LocalStorageKit(
            activity = activity,
        )
        // TODO: 后续删除
        localStorageKit.mock()
    }
}
