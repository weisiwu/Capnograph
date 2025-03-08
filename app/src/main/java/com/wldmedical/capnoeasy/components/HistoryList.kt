package com.wldmedical.capnoeasy.components

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.kits.GROUP_BY
import com.wldmedical.capnoeasy.kits.Group
import com.wldmedical.capnoeasy.kits.Patient
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.pages.HistoryRecordDetailActivity
import com.wldmedical.capnoeasy.recordIdParams
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/**
 * App 历史列表，内容为设备上记录的整体历史记录数据
 * 分为三组: 全部、病人、时间
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryList(
    records: MutableList<Record>,
    state: MutableState<GROUP_BY>,
    onSearch: ((Group) -> Unit)? = null,
    onItemClick: ((record: Record) -> UInt)? = null,
    context: ComponentActivity,
    groups: List<Group>
) {
    val emptyRecordAlert = getString(R.string.historylist_no_data, context)
    val formatter = DateTimeFormatter.ofPattern(getString(R.string.historylist_date_format, context), Locale.CHINA)
    val rRecords = remember { records }
    val rState = remember { state }
    val newRecords = mutableListOf<Record>()
    val options = ActivityOptionsCompat.makeCustomAnimation(context, 0, 0)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    val configuration = LocalConfiguration.current
    val thirdScreenWidth = configuration.screenWidthDp.dp / 3
    val selectedIndex = groups.indexOfFirst { group ->
        group.type == rState.value
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(rRecords.isEmpty()) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.empty_device_list),
                    contentDescription = emptyRecordAlert,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = emptyRecordAlert,
                    fontSize = 20.sp,
                    color = Color(0xFF687383)
                )
            }
        } else {
            PrimaryTabRow(
                containerColor = Color.White,
                contentColor = Color.White,
                selectedTabIndex = selectedIndex,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedIndex,
                            matchContentSize = true
                        ),
                        width = thirdScreenWidth,
                        color = Color(0xff1677FF)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                groups.forEachIndexed { index, group ->
                    val isSelected = selectedIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            rState.value = group.type
                            onSearch?.invoke(group)
                        },
                        text = {
                            Text(
                                text = group.name,
                                maxLines = 1,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(0.dp),
                                fontWeight = FontWeight.Bold,
                                color = if(isSelected) Color(0xff1677FF) else Color(0xff333333),
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    )
                }
            }

            // 无分组
            if (rState.value == GROUP_BY.ALL) {
                newRecords.addAll(rRecords)
            } else {
                // 带有分组的情况 - 默认走病人
                var currentGroup = ""
                var groupedById  = rRecords.groupBy { it.patientIndex }

                if (rState.value == GROUP_BY.DATE) {
                    groupedById  = rRecords.groupBy { it.dateIndex.toString() }
                }
                groupedById.forEach { (id, records) ->
                    if (currentGroup != id) {
                        currentGroup = id
                        if (rState.value == GROUP_BY.PATIENT) {
                            val patient = records[0].patient
                            newRecords.add(
                                Record(
                                    patient = patient,
                                    startTime = records[0].startTime,
                                    endTime = records[0].endTime,
                                    isGroupTitle = true,
                                    groupTitle = patient.name + " " + patient.gender.title + " " + patient.age + getString(R.string.historylist_age, context)
                                )
                            )
                        } else {
                            val startDate = records[0].startTime
                            newRecords.add(
                                Record(
                                    patient = records[0].patient,
                                    startTime = startDate,
                                    endTime = records[0].endTime,
                                    isGroupTitle = true,
                                    groupTitle = startDate.year.toString() + getString(R.string.historylist_year, context) + startDate.monthValue + getString(R.string.historylist_month, context) + startDate.dayOfMonth + getString(R.string.historylist_day, context)
                                )
                            )
                        }
                    }
                    newRecords.addAll(records)
                }
            }

            LazyColumn {
                items(newRecords) { record ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                bottom = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                            .clickable {
                                onItemClick?.invoke(record)
                                val intent = Intent(
                                    context,
                                    HistoryRecordDetailActivity::class.java
                                )
                                intent.putExtra(recordIdParams, record.id.toString())
                                launcher.launch(intent, options)
                            }
                    ) {
                        if (record.isGroupTitle) {
                            Text(
                                text = record.groupTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            return@items
                        }

                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Row {
                                Text(
                                    text = record.patient.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "${record.patient.age}${getString(R.string.historylist_age, context)}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = if (record.patient.gender == GENDER.MALE) getString(R.string.historylist_male, context) else getString(R.string.historylist_female, context),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                text = "${record.startTime.format(formatter)}-${
                                    record.endTime.format(
                                        formatter
                                    )
                                }",
                                fontSize = 12.sp,
                                color = Color(0xff888888),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .widthIn(min = 32.dp)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xffCACACA),
                            contentDescription = "ArrowBack"
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color(0xffDFE6E9))
                            .alpha(0.4f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.fillMaxWidth().weight(1f).heightIn(min = 50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListPreview() {
    val patient = Patient(
        name = "病人A",
        age = 90,
        gender = GENDER.MALE,
    )
    var startTime: LocalDateTime = LocalDateTime.now()
    var endTime: LocalDateTime = LocalDateTime.now()
    val startTimeString = "2025年1月29日18:00:03"
    val endTimeString = "2025年1月01日18:00:03"
    val state = remember { mutableStateOf(GROUP_BY.ALL) }
    val context: Context = LocalContext.current
    val groups = listOf(
        Group(name = getString(R.string.localstorage_all, context), type = GROUP_BY.ALL),
        Group(name = getString(R.string.localstorage_patient, context), type = GROUP_BY.PATIENT),
        Group(name = getString(R.string.localstorage_time, context), type = GROUP_BY.DATE),
    )

    try {
        val formatter = DateTimeFormatter.ofPattern(getString(R.string.historylist_date_format, context), Locale.CHINA)
        startTime = LocalDateTime.parse(startTimeString, formatter) // 将字符串解析为 LocalDateTime 对象
        endTime = LocalDateTime.parse(endTimeString, formatter) // 将字符串解析为 LocalDateTime 对象
    } catch (e: DateTimeParseException) {
        println("Invalid date time format: ${e.message}") // 捕获并处理解析异常
    }

    val records = mutableListOf(
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
    CapnoEasyTheme {
        Box (
            modifier = Modifier.background(Color.White)
        ) {
            HistoryList(
                records = records,
                state = state,
                context = ComponentActivity(),
                groups = groups,
            )
        }
    }
}