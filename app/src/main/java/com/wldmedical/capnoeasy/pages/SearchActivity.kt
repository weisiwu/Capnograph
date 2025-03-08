package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityOptionsCompat
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
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
        val discoveredPeripherals = viewModel.discoveredPeripherals.collectAsState()
        val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

        DeviceList(
            devices = discoveredPeripherals.value,
            onSearch = {
                viewModel.updateDiscoveredPeripherals(null, true)
                viewModel.updateLoadingData(
                    LoadingData(
                        text = "${getStringAcitivity(R.string.search_start)}${it.name ?: getStringAcitivity(R.string.bluetooth_unknown_device)}",
                        duration = InfinityDuration,
                        cancelable = false
                    )
                )
                blueToothKit.searchDevices(
                    scanFinish = {
                        viewModel.updateToastData(
                            ToastData(
                                text = getStringAcitivity(R.string.search_finish),
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
                                text = getStringAcitivity(R.string.search_fail),
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
                        cancelable = false,
                        duration = 10000,
                        text = "${getStringAcitivity(R.string.search_start_connect)}${it.name ?: getStringAcitivity(R.string.bluetooth_unknown_device)}"
                    )
                )
            }
        )
    }
}