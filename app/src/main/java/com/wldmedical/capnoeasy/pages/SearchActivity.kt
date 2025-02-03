package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceType
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.TypeSwitch
import com.wldmedical.capnoeasy.kits.unkownName

/***
 * 搜素列表
 */
class SearchActivity : BaseActivity() {
    override val pageScene = PageScene.DEVICES_LIST_PAGE

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    override fun Content() {
        val selectedIndex = if (viewModel.connectType.value == null) 0 else DeviceTypes.indexOfFirst { type -> viewModel.connectType.value == type }
        val discoveredPeripherals = viewModel.discoveredPeripherals.collectAsState()

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
            devices = discoveredPeripherals.value,
            onSearch = {
                viewModel.updateDiscoveredPeripherals(null, true)
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "开始搜索${it.name ?: unkownName}"
                    )
                )
                blueToothKit.searchDevices()
            },
            onDeviceClick = {
                blueToothKit.connectDevice(device = it)
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "开始链接设备${it.name ?: unkownName}"
                    )
                )
            }
        )
    }
}