package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.HistoryList
import com.wldmedical.capnoeasy.components.Patient
import com.wldmedical.capnoeasy.components.Record
import com.wldmedical.capnoeasy.components.formatter
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.UUID

/***
 * 历史记录列表页
 */
class HistoryRecordsActivity : BaseActivity() {
    override val pageScene = PageScene.HISTORY_LIST_PAGE

    @Composable
    override fun Content() {
        val patient = Patient(
            name = "病人A",
            age = 90,
            gender = GENDER.MALE,
            id = UUID.randomUUID()
        )
        var startTime: LocalDateTime = LocalDateTime.now()
        var endTime: LocalDateTime = LocalDateTime.now()
        val startTimeString = "2025年1月29日18:00:03"
        val endTimeString = "2025年1月01日18:00:03"

        try {
            startTime = LocalDateTime.parse(startTimeString, formatter) // 将字符串解析为 LocalDateTime 对象
            endTime = LocalDateTime.parse(endTimeString, formatter) // 将字符串解析为 LocalDateTime 对象
        } catch (e: DateTimeParseException) {
            println("Invalid date time format: ${e.message}") // 捕获并处理解析异常
        }

        val records = listOf(
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
            Record(patient = patient, startTime = startTime, endTime = endTime),
        )

        HistoryList(records = records)
    }
}