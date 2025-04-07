package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
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
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.kits.BlueToothKit
import com.wldmedical.capnoeasy.kits.BlueToothKitManager.blueToothKit
import com.wldmedical.capnoeasy.kits.CO2Data
import com.wldmedical.capnoeasy.kits.LocalStorageKit
import com.wldmedical.capnoeasy.kits.LocalStorageKitManager.localStorageKit
import com.wldmedical.capnoeasy.kits.compress
import com.wldmedical.capnoeasy.kits.maxRecordDataChunkSize
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * App底部导航条
 * 所有一级页和二级页使用
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EtCo2LineChart(
    blueToothKit: BlueToothKit,
    localStorageKit: LocalStorageKit,
    viewModel: AppStateModel
) {
    val context: Context = LocalContext.current
    var index = 0f
    val chart: MutableState<LineChart?> = remember { mutableStateOf(null) }
    val entries = remember { mutableStateListOf<Entry>() }.apply {
        if (this.size < maxXPoints) {
            repeat(maxXPoints) { add(Entry(it.toFloat(), 0f)) }
        }
    }
    val pagerState = rememberPagerState()
    viewModel.lineChart = chart.value

    // 处理折线图动画效果
    LaunchedEffect(blueToothKit.dataFlow) { // 监听 bluetoothKit 实例的变化
        blueToothKit.dataFlow.collectLatest { newData -> // 收集 Flow 的数据
            if (newData.isNotEmpty()) {
                if (entries.size >= maxXPoints) {
                    entries.removeAt(0) // 删除头部元素
                }
                index += 1f
                entries.add(Entry(index, newData.last().value))
            }
        }
    }

    // 页面初始化的时候，添加对viewModel中全部数据的监听
    LaunchedEffect(viewModel.totalCO2WavedDataFlow) {
        // 如果数据已经足够一个chunk size，则将数据读出来，并存放到数据库中
        // totalCO2WavedData和totalCO2WavedDataFlow数据是相同的
        viewModel.totalCO2WavedDataFlow.collect { co2WavePointDataList ->
            if (viewModel.totalCO2WavedData.size < maxRecordDataChunkSize) {
                return@collect
            }
            println("wswTEst 开始准备处理chunk")
            // 对取出的数据存入数据库
            val currentlRecordId = localStorageKit.currentRecordId
            println("wswTEst 获取到的记录id $currentlRecordId")
            // 有数据id的时候才会存储到数据库中
            currentlRecordId?.let { id ->
                withContext(Dispatchers.IO) {
                    // 如果当前没有寄存在recordId下的chunk数据，那么chunkIndex从0开始
                    val chunkIndex = localStorageKit.database.co2DataDao().getCO2DataByRecordId(id).size.coerceAtLeast(0)
                    // 从头部开始取出 10000 条数据，不改变 totalCO2WavedDataFlow 本身的内容
                    val co2DataChunk = CO2Data(
                        recordId = id,
                        chunkIndex = chunkIndex,
                        data = co2WavePointDataList.take(maxRecordDataChunkSize).compress()
                    )
                    val rowId = localStorageKit.database.co2DataDao().insertCO2Data(co2DataChunk)
                    println("wswTest chunk插入完成，序号为 $rowId")
                    // 如果插入后没有返回数据行号,则插入失败，行号是一个Long类型
                    if (rowId != -1L) {
                        // 对列表数据的修改需要在主线程
                        withContext(Dispatchers.Main) {
                            // 从 totalCO2WavedDataFlow 删除取出的数据
                            // 不直接改变 StateFlow，而是通过修改 appState.totalCO2WavedData
                            viewModel.delSavedCO2WavedDataChunk()
                            println("wswTest 执行删除已经插入数据库的chunk")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp)
            .background(Color.Transparent)
    ) {
        HorizontalPager(
            count = 1,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) { page ->
            blueToothKit.connectedCapnoEasyIndex = pagerState.currentPage

            if (blueToothKit.connectedCapnoEasy.value != null) {
                val currentDevice = blueToothKit.connectedCapnoEasy.value;

                if (currentDevice != null) {
                    Text(
                        text = if(currentDevice.name == null) getString(R.string.etco2linechart_unknown_device, context) else currentDevice.name,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
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

                            chart.value = this
                        }
                    },
                    update = {
                        // 设置数据
                        val dataSet = LineDataSet(entries, "ETCO2")
                        dataSet.lineWidth = 2f
                        dataSet.setDrawCircles(false) // 不绘制圆点
                        dataSet.setDrawValues(false) // 不绘制具体的值
                        val lineData = LineData(dataSet)
                        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                        chart.value?.data = lineData
                        it.invalidate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
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
                localStorageKit = localStorageKit,
                viewModel = AppStateModel(appState = AppState())
            )
        }
    }
}