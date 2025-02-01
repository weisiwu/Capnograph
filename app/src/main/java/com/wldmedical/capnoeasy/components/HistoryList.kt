package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.UUID

val emptyRecordAlert = "没有历史波形数据"
val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日HH:mm:ss", Locale.CHINA)

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

data class Patient(
    val name: String,
    val id: UUID,
    val gender: GENDER = GENDER.MALE,
    val age: Int
)

data class Record(
    val patient: Patient,
    val startTime: LocalDateTime,
    var endTime: LocalDateTime
)

// TODO: 列表分组还没有加上
/**
 * App 历史列表，内容为设备上记录的整体历史记录数据
 * 分为三组: 全部、病人、时间
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryList(
    records: List<Record>,
    onSearch: ((Group) -> Unit)? = null,
    onItemClick: ((record: Record) -> UInt)? = null,
) {
    val configuration = LocalConfiguration.current
    val thirdScreenWidth = configuration.screenWidthDp.dp / 3
    val minListHeight = 100.dp
    val state = remember { mutableStateOf(GROUP_BY.ALL) }
    val selectedIndex = Groups.indexOfFirst { group ->
        group.type == state.value
    }


    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minListHeight)
    ) {
        if(records.isEmpty()) {
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
                    fontSize = 24.sp,
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
                Groups.forEachIndexed { index, group ->
                    val isSeleted = selectedIndex == index
                    Tab(
                        selected = isSeleted,
                        onClick = {
                            state.value = group.type
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
                                color = if(isSeleted) Color(0xff1677FF) else Color(0xff333333),
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    )
                }
            }
            LazyColumn {
                items(records) { device ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                            .clickable {
                                onItemClick?.invoke(device)
                            }
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Row {
                                Text(
                                    text = device.patient.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                    )
                                Text(
                                    text = "${device.patient.age}岁",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = if(device.patient.gender == GENDER.MALE) "男" else "女",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                text = "${device.startTime.format(formatter)}-${device.endTime.format(formatter)}",
                                fontSize = 12.sp,
                                color = Color(0xff888888),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .widthIn(min = 32.dp))
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListPreview() {
//    val records: List<Device> = listOf()
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
    CapnoEasyTheme {
        HistoryList(records = records)
    }
}