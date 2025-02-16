package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceType
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.components.TypeSwitch
import com.wldmedical.capnoeasy.kits.unkownName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/***
 * 搜素列表
 */
class SearchActivity : BaseActivity() {
    override var pageScene = PageScene.DEVICES_LIST_PAGE

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    override fun Content() {
        val selectedIndex = if (viewModel.connectType.value == null) 0 else DeviceTypes.indexOfFirst { type -> viewModel.connectType.value == type }
        val discoveredPeripherals = viewModel.discoveredPeripherals.collectAsState()
        val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

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
                        text = "开始搜索${it.name ?: unkownName}",
                        duration = InfinityDuration,
                        cancelable = false
                    )
                )
                blueToothKit.searchDevices(
                    scanFinish = {
                        viewModel.updateToastData(
                            ToastData(
                                text = "搜索结束",
                                type = ToastType.SUCCESS,
                                showMask = false
                            )
                        )
                    },
                    scanFind = {
                        viewModel.updateDiscoveredPeripherals(it)
                    },
                    checkBlueToothFail = {
                        viewModel.updateToastData(
                            ToastData(
                                text = "搜索失败",
                                type = ToastType.FAIL,
                                showMask = false
                            )
                        )
                    }
                )
            },
            onDeviceClick = {
                blueToothKit.connectDevice(
                    device = it,
                    onSuccess = { device ->
                        viewModel.clearXData()
                        viewModel.updateCurrentTab(1)
                        viewModel.updateCurrentPage(PageScene.HOME_PAGE)
                        val intent = Intent(this, MainActivity::class.java)
                        GlobalScope.launch(Dispatchers.Main) { // 使用协程切换到主线程
                            launcher.launch(intent, options)
                        }
                        // 保存配对设备信息到本地
                        blueToothKit.savePairedBLEDevice(this, device)
                    }
                )
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "开始链接设备${it.name ?: unkownName}"
                    )
                )
            }
        )
    }
}