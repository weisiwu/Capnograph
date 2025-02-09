package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        CapnoEasyTheme {
            EtCo2LineChart(
                blueToothKit = blueToothKit,
                viewModel = viewModel
            )

            EtCo2Table(
                viewModel = viewModel,
                blueToothKit = blueToothKit
            )
        }
    }
}