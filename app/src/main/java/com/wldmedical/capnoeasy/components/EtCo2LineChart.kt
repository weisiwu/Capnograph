package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp)
            .background(Color.Transparent)
    ) {
        HorizontalPager(
            count = blueToothKit.connectedCapnoEasy.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) { page ->
            blueToothKit.connectedCapnoEasyIndex = pagerState.currentPage

            if (blueToothKit.connectedCapnoEasy.size > 0) {
                val currentDevice = blueToothKit.connectedCapnoEasy[blueToothKit.connectedCapnoEasyIndex];

                if (currentDevice != null) {
                    Text(
                        text = if(currentDevice.name == null) "未知设备" else currentDevice.name,
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
                viewModel = AppStateModel(appState = AppState())
            )
        }
    }
}