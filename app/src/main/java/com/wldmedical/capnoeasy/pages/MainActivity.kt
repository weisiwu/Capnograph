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
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme

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
        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                lineSeries {
                    series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11)
                }
            }
        }

        CapnoEasyTheme {
            EtCo2LineChart(modelProducer)
        }

        EtCo2Table(
            viewModel = viewModel
        )
    }
}