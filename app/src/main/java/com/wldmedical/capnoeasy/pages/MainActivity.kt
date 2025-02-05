package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import kotlinx.coroutines.flow.collectLatest

/***
 * 主页
 */
class MainActivity : BaseActivity() {
    override val pageScene = PageScene.HOME_PAGE

    override fun onNavBarRightClick() {
        val isReocrding = viewModel.isRecording.value
        viewModel.updateToastData(
            ToastData(
                text = if (isReocrding) "停止记录" else "启动记录",
                duration = 600,
                showMask = false,
                type = ToastType.SUCCESS,
                callback = {
                    // TODO: 记录数据相关操作
                    viewModel.updateIsRecording(!isReocrding)
                    if(isReocrding) {
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
        val modelProducer = remember { CartesianChartModelProducer() }
        val emptyList = Array(size = maxXPoints, init = { 0 })

        LaunchedEffect(blueToothKit.dataFlow) { // 监听 bluetoothKit 实例的变化
            blueToothKit.dataFlow.collectLatest { newData -> // 收集 Flow 的数据
                if (newData.size > 0) {
                    val restLen = maxXPoints - newData.size
                    val restList = List(size = restLen, init = { 0f })
                    val intList = newData.map { it.value }
                    val resList = restList + intList
                    println("wswTest 共仓类似是 ${resList.size}")
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
        }

        EtCo2Table(
            viewModel = viewModel,
        )
    }
}