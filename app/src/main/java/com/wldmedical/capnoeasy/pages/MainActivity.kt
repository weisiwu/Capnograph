package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.kits.DataPoint
import com.wldmedical.capnoeasy.kits.RandomCurveGenerator
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random


/***
 * 主页
 */
class MainActivity : BaseActivity() {
    override val pageScene = PageScene.HOME_PAGE

    override fun onNavBarRightClick() {
        val isRecording = viewModel.isRecording.value
        viewModel.updateToastData(
            ToastData(
                text = if (isRecording) "停止记录" else "启动记录",
                duration = 600,
                showMask = false,
                type = ToastType.SUCCESS,
                callback = {
                    // TODO: 记录数据相关操作
                    viewModel.updateIsRecording(!isRecording)
                    if(isRecording) {
                        viewModel.updateConfirmData(
                            ConfirmData(
                                title = "记录保存成功",
                                text = "保存的记录可在设置>历史记录中查看",
                                confirm_btn_text = "确认",
                                onClick = {
                                    viewModel.updateConfirmData(null)
                                }
                            )
                        )
                    }
                },
            )
        )
    }

    @Composable
    override fun Content() {
        // 处理折线图动画效果
        val modelProducer = remember { CartesianChartModelProducer() }
        val emptyList = Array(size = maxXPoints, init = { 0 })
        // TODO: 随机500个点曲线
//        val emptyList = RandomCurveGenerator(
//            updateGraph = { blueToothKit.updateReceivedData(it.map { DataPoint(index = 1, value = it) }) }
//        )

        LaunchedEffect(blueToothKit.dataFlow) { // 监听 bluetoothKit 实例的变化
            // TODO: 这里可能有问题，如果只收集最新的，会不会丢数据？
            blueToothKit.dataFlow.collectLatest { newData -> // 收集 Flow 的数据
                if (newData.isNotEmpty()) {
                    val restLen = (maxXPoints - newData.size).coerceAtMost(0)
                    val restList = List(size = restLen, init = { 0f })
                    val floatList = newData.map { it.value }
                    val resList = (floatList + restList).takeLast(maxXPoints)
                    // 处理数据更新，重绘表格
                    modelProducer.runTransaction {
                        lineSeries { series(resList) }
                    }
                } else {
                    modelProducer.runTransaction {
                        lineSeries { series(emptyList.toList()) }
                    }
                }
            }
        }

        CapnoEasyTheme {
            EtCo2LineChart(
                modelProducer,
                maxY = viewModel.CO2Scale.value.value.toDouble()
            )

            EtCo2Table(
                viewModel = viewModel,
            )
        }
    }
}