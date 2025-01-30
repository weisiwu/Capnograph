package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wldmedical.capnoeasy.components.AlertData
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.Loading
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.components.TypeSwitch
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 搜素列表
 */
class SearchActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.DEVICES_LIST_PAGE)
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

        TypeSwitch(
            types = DeviceTypes,
            modifier = Modifier.fillMaxWidth()
        )

        DeviceList(
            devices = devices,
            onSearch = {
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "开始搜索${it.name}"
                    )
                )
            },
            onDeviceClick = {
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "开始链接设备${it.name}"
                    )
                )

//                viewModel.updateAlertData(
//                    AlertData(
//                        text = "开始链接设备${it.name}",
//                        ok_btn_text = "ok",
//                        cancel_btn_text = "cancel"
//                    )
//                )

//                viewModel.updateConfirmData(
//                    ConfirmData(
//                        text = "开始链接设备${it.name}",
//                        title = "标题",
//                        confirm_btn_text = "确认按钮",
//                    )
//                )

//                viewModel.updateToastData(
//                    ToastData(
//                        text = "开始链接设备${it.name}",
//                        type = ToastType.FAIL,
//                        showMask = true,
//                    )
//                )
            }
        )
    }
}