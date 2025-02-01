package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceType
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.TypeSwitch

/***
 * 搜素列表
 */
class SearchActivity : BaseActivity() {
    override val pageScene = PageScene.DEVICES_LIST_PAGE

    @Composable
    override fun Content() {
        val selectedIndex = if (viewModel.connectType.value == null) 0 else DeviceTypes.indexOfFirst { type -> viewModel.connectType.value == type }
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
            selectedIndex = selectedIndex,
            types = DeviceTypes,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
            onTypeClick = {
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "切换搜索方式为${it.name}",
                        duration = 300,
                    )
                )
                viewModel.updateConnectType(it as DeviceType)
            }
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
//                        duration = 500
//                    )
//                )
            }
        )
    }
}