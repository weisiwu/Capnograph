package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.PageScene
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
            types = DeviceTypes
        )

        DeviceList(
            devices = devices
        )
    }
}