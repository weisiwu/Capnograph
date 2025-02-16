package com.wldmedical.capnoeasy.pages

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.ActionModal
import com.wldmedical.capnoeasy.components.AlertModal
import com.wldmedical.capnoeasy.components.BaseLayout
import com.wldmedical.capnoeasy.components.ConfirmModal
import com.wldmedical.capnoeasy.components.Loading
import com.wldmedical.capnoeasy.components.Toast
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.kits.BlueToothKit
import com.wldmedical.capnoeasy.kits.BlueToothKitManager
import com.wldmedical.capnoeasy.kits.LocalStorageKit
import com.wldmedical.capnoeasy.kits.LocalStorageKitManager
import com.wldmedical.capnoeasy.kits.PrintProtocalKitManager
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.hotmeltprint.HotmeltPinter
import dagger.hilt.android.AndroidEntryPoint

/***
 * 所有页面基类
 */
@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {
    /***
     * 相关渲染函数
     */
    // 通用加载中
    @Composable
    open fun ShowLoading() {
        val loadingData = viewModel.loadingData.value
        if (loadingData != null) {
            Loading(
                data = loadingData,
                onClick = {
                    if (loadingData.cancelable) {
                        viewModel.updateLoadingData(null)
                    }
                },
                onTimeout = {
                    viewModel.updateLoadingData(null)
                }
            )
        }
    }

    // 通用alert提示
    @Composable
    open fun ShowAlert() {
        val alertData = viewModel.alertData.value
        if (alertData != null) {
            AlertModal(
                data = alertData
            )
        }
    }

    // 通用confirm提示
    @Composable
    open fun ShowConfirm() {
        val confirmData = viewModel.confirmData.value
        if (confirmData != null) {
            ConfirmModal(
                data = confirmData
            )
        }
    }

    // 通用Toast提示
    @Composable
    open fun ShowToast() {
        val toastData = viewModel.toastData.value
        if (toastData != null) {
            Toast(
                data = toastData,
                onClick = {
                    viewModel.updateToastData(null)
                },
                onTimeout = {
                    viewModel.updateToastData(null)
                }
            )
        }
    }

        // 通用ActionModal弹框
        @Composable
        open fun ShowActionModal() {
            val showActionModal = viewModel.showActionModal.value
            if (showActionModal) {
                ActionModal(
                    viewModel = viewModel,
                    onCancelClick = {  },
                    onPrintPDFClick = {  },
                    onPrintTicketClick = {  }
                )
            }
        }

    @Composable
    open fun Content() {}

    /***
     * 逻辑相关函数
     */
    lateinit var viewModel: AppStateModel

    lateinit var blueToothKit: BlueToothKit

    lateinit var printProtocalKit: HotmeltPinter

    lateinit var localStorageKit: LocalStorageKit

    open var pageScene = PageScene.HOME_PAGE

    open fun updatePageScene() {
        this.viewModel.updateCurrentPage(this.pageScene)
    }

    private fun onScanFind(device: BluetoothDevice) {
        viewModel.updateDiscoveredPeripherals(device)
    }

    open fun onTabClick(index: Int): Boolean {
        viewModel.updateCurrentTab(index)
        return true
    }

    open fun onNavBarRightClick() {}

    // 检查是否已经链接上CannoEasy
    public fun checkHasConnectDevice(cb: (() -> Unit)? = null) {
        if (blueToothKit.connectedCapnoEasy.value != null) {
            cb?.invoke()
        } else {
            viewModel.updateToastData(
                ToastData(
                    text = "未连接设备，请链接后再试",
                    showMask = false,
                    duration = 1000,
                )
            )
        }
    }

    /***
     * 生命周期函数
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[AppStateModel::class.java]

        BlueToothKitManager.initialize(this, viewModel)
        blueToothKit = BlueToothKitManager.blueToothKit

        PrintProtocalKitManager.initialize()
        printProtocalKit = PrintProtocalKitManager.printProtocalKit

        LocalStorageKitManager.initialize(this, (application as CapnoEasyApplication))
        localStorageKit = LocalStorageKitManager.localStorageKit

        // mock数据，本地测试时候打开
        // lifecycleScope.launch {
        //     localStorageKit.mock()
        // }

        enableEdgeToEdge()
        setContent {
            updatePageScene()
            BaseLayout(
                context = this,
                float = {
                    ShowToast()

                    ShowAlert()

                    ShowConfirm()

                    ShowLoading()

                    ShowActionModal()
                },
                onTabClick = { onTabClick(it) },
                onNavBarRightClick = { onNavBarRightClick() }
            ) {
                Content()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            updatePageScene()
        }
    }

    // 上级页返回后，重置
    override fun onResume() {
        super.onResume()
        updatePageScene()
        pageScene = viewModel.currentPage.value.currentPage
    }
}