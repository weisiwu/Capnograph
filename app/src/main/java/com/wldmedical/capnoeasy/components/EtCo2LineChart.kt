package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.wldmedical.capnoeasy.kits.BlueToothKit
import com.wldmedical.capnoeasy.kits.BlueToothKitManager.blueToothKit
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel
import kotlinx.coroutines.flow.collectLatest

fun getLatest500Entries(entries: SnapshotStateList<Entry>): SnapshotStateList<Entry> {
    val size = entries.size
    if (size <= maxXPoints) {
        return entries
    }
    return SnapshotStateList<Entry>().apply {
        addAll(entries.subList(size - maxXPoints, size))
    }
}


/**
 * App底部导航条
 * 所有一级页和二级页使用
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EtCo2LineChart(
    blueToothKit: BlueToothKit,
    viewModel: AppStateModel
) {
    val chart: MutableState<LineChart?> = remember { mutableStateOf(null) }
    val shownCapnoGraph: MutableState<BluetoothDevice?> = remember { mutableStateOf(null) }
    val currentDeviceEntries = remember { mutableStateListOf<Entry>() }
    val pagerState = rememberPagerState()
    viewModel.lineChart = chart.value

    LaunchedEffect(blueToothKit.currentCapnoGraph) {
        val entiesName = blueToothKit.currentCapnoGraph?.address

        if (entiesName != null) {
//            viewModel.entriesMap.getOrPut(entiesName) { // 使用 getOrPut 初始化
//                SnapshotStateList<Entry>().apply {
//                    repeat(maxXPoints) {
//                        add(Entry(0f, 0f))
//                    }
//                }
//            }

            var curentEntries = viewModel.entriesMap.getOrPut(entiesName) { // 使用 getOrPut 初始化
                SnapshotStateList<Entry>().apply {
                    repeat(maxXPoints) {
                        add(Entry(0f, 0f))
                    }
                }
            }
            currentDeviceEntries.clear()
            curentEntries = getLatest500Entries(curentEntries)
            currentDeviceEntries.addAll(curentEntries)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collectLatest { page ->
            // 页面切换后的回调
            if (blueToothKit.connectedCapnoEasy.size > page) {
                val nextDevice = blueToothKit.connectedCapnoEasy[page]

                // 修改设备的时候，同时修改渲染变量
                if (nextDevice != null) {
                    val entiesName = nextDevice.address
                    var curentEntries = viewModel.entriesMap.getOrPut(entiesName) { // 使用 getOrPut 初始化
                        SnapshotStateList<Entry>().apply {
                            repeat(maxXPoints) {
                                add(Entry(0f, 0f))
                            }
                        }
                    }
                    blueToothKit.currentCapnoGraph = nextDevice
                    shownCapnoGraph.value = nextDevice

                    currentDeviceEntries.clear()
                    curentEntries = getLatest500Entries(curentEntries)
                    currentDeviceEntries.addAll(curentEntries)

                    // 添加设备后，开始接受数据流
                    blueToothKit.updateDataFlow { it ->
                        val entiesName = blueToothKit.currentCapnoGraph?.address
                        val curIndex = viewModel.entriesIndexMap[entiesName] ?: 0f
                        if (currentDeviceEntries.size >= maxXPoints) {
                            val len = currentDeviceEntries.size - maxXPoints + 1
                            currentDeviceEntries.removeRange(0, len) // 删除头部元素
                        }

                        if (it.isNotEmpty()) {
                            it.last().let {
                                println("wswTest 数据接受 ${blueToothKit.currentCapnoGraph?.name}_${it.value}_${curIndex + 1f}====${currentDeviceEntries.size}")
                                currentDeviceEntries.add(Entry(curIndex + 1f, it.value))
                                if (entiesName != null) {
                                    viewModel.entriesIndexMap[entiesName] = curIndex.plus(1f)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    println("wswTest 当前的index  ${viewModel.entriesIndexMap}")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp)
            .background(Color.Transparent)
    ) {
        Text(
            text = "实时ETCO2",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff1D2129),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalPager(
            count = blueToothKit.connectedCapnoEasy.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) { page ->
            if (blueToothKit.connectedCapnoEasy.size > 0) {
                Text(
                    text = shownCapnoGraph.value?.name ?: "未知设备",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(top = 20.dp)
            ) {
                AndroidView(
                    factory = {
                        LineChart(it).apply {
                            // 设置图表大小
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                300.dp.value.toInt()
                            )

                            // 设置图表背景颜色
                            setBackgroundColor(Color.Transparent.value.toInt())

                            // 设置 X 轴位置
                            xAxis.position = XAxis.XAxisPosition.BOTTOM

                            // 禁用右侧 Y 轴
                            axisRight.isEnabled = false

                            // 隐藏 DescriptionLabel
                            description.isEnabled = false

                            // 设置 Y 轴值范围
                            axisLeft.axisMinimum = 0f
                            axisLeft.axisMaximum = viewModel.CO2Scale.value.value

                            // 设置 X 轴格式化器
                            xAxis.valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String { return "" }
                            }

                            val handler = Handler(Looper.getMainLooper())
                            val runnable = object : Runnable {
                                override fun run() {
                                    chart.value?.invalidate()
                                    handler.postDelayed(this, 20) // 1000ms / 50次 = 20ms
                                }
                            }
                            handler.post(runnable)


                            chart.value = this
                        }
                    },
                    update = {
                        // 设置数据
                        val dataSet = LineDataSet(currentDeviceEntries, "ETCO2")
                        dataSet.lineWidth = 2f
                        dataSet.setDrawCircles(false) // 不绘制圆点
                        val lineData = LineData(dataSet)
                        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                        chart.value?.data = lineData
                        it.invalidate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EtCo2LineChartPreview() {

    CapnoEasyTheme {
        Box(
            modifier = Modifier.background(Color.White)
        ) {
            EtCo2LineChart(
                blueToothKit,
                viewModel = AppStateModel(appState = AppState())
            )
        }
    }
}