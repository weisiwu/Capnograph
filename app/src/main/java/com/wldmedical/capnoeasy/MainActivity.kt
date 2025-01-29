package com.wldmedical.capnoeasy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.wldmedical.capnoeasy.components.ActionBar
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.TypeSwitch
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.GENDER
import com.wldmedical.capnoeasy.components.HistoryList
import com.wldmedical.capnoeasy.components.Loading
import com.wldmedical.capnoeasy.components.NavBar
import com.wldmedical.capnoeasy.components.NavBarComponentState
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.Patient
import com.wldmedical.capnoeasy.components.Record
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.components.formatter
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mid = remember { mutableStateOf(1) }
            val historypage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.HISTORY_LIST_PAGE)) }

            val devices = listOf(
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
            )
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

            val modelProducer = remember { CartesianChartModelProducer() }
            LaunchedEffect(Unit) {
                modelProducer.runTransaction {
                    lineSeries {
                        series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11)
                    }
                }
            }

            Scaffold { innerPadding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    Loading(text = "搜索设备中")
                    CapnoEasyTheme {
                        NavBar(
                            state = historypage,
                            onRightClick = {
                                // 处理点击事件的逻辑
                                Log.d("TAG", "Button clicked")
                            }
                        )

                        EtCo2LineChart(modelProducer)
//                        HistoryList(records = records)
//                        WheelPicker(co2UnitsObj)
//                        EtCo2Table()
//                        DeviceList(devices = devices)
//                        DeviceTypeSwitch()
//                        ActionBar(
//                            selectedIndex = mid,
//                            onTabClick = {}
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CapnoEasyTheme {
        Greeting("Android")
    }
}