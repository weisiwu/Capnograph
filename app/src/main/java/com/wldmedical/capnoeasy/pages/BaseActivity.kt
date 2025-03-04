package com.wldmedical.capnoeasy.pages

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.ActionModal
import com.wldmedical.capnoeasy.components.AlertData
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
import kotlin.collections.any
import kotlin.collections.filter
import kotlin.collections.isNotEmpty
import kotlin.collections.toTypedArray

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
                onSavePDFClick = { onSavePDFClick() },
                onPrintTicketClick = { onPrintTicketClick() }
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

    // 需要保存的pdf源文件路径
    lateinit var sourceFilePath: String

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

    open fun onSavePDFClick() {}

    open fun onPrintTicketClick() {}

    // 检查是否已经链接上CannoEasy
    public fun checkHasConnectDevice(cb: (() -> Unit)? = null) {
        if (blueToothKit.connectedCapnoEasy.value != null) {
            cb?.invoke()
        } else {
            viewModel.updateToastData(
                ToastData(
                    text = getString(R.string.base_noconnect_msg),
                    showMask = false,
                    duration = 1000,
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBluetoothPermissions(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            // 检查权限是否被拒绝
            val shouldShowRationale = permissionsToRequest.any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }
            if (shouldShowRationale) {
                //引导用户手动开启权限
                viewModel.updateAlertData(
                    AlertData(
                        text = getString(R.string.base_infobl_msg),
                        ok_btn_text = getString(R.string.base_go_open),
                        onOk = {
                            viewModel.updateAlertData(null)
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:$packageName")
                            startActivity(intent)
                        }
                    )
                )
            } else {
                // 3. 如果没有权限，则请求权限
                ActivityCompat.requestPermissions(this, permissionsToRequest, 100)
            }
            return false
        }
        return true
    }

    private fun initializeBlueToothKit() {
        // 5. 延迟初始化 BlueToothKit
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("BaseActivity", "Initializing BlueToothKit...")
            // 已经有设备练级，不再继续初始化
            if (blueToothKit.connectedCapnoEasy.value != null) return@postDelayed
            BlueToothKitManager.initialize(this, viewModel, true)
            blueToothKit = BlueToothKitManager.blueToothKit
            Log.d("BaseActivity", "BlueToothKit initialized.")
        }, 300) // 延迟 1 秒
    }

    /***
     * 生命周期函数
     */
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[AppStateModel::class.java]

        BlueToothKitManager.initialize(this, viewModel)
        blueToothKit = BlueToothKitManager.blueToothKit

        // 重新授权后，初始化
        if (checkBluetoothPermissions()) {
            initializeBlueToothKit()
        }

        PrintProtocalKitManager.initialize()
        printProtocalKit = PrintProtocalKitManager.printProtocalKit

        LocalStorageKitManager.initialize(this, (application as CapnoEasyApplication))
        localStorageKit = LocalStorageKitManager.localStorageKit

        val language = localStorageKit.loadUserLanguageFromPreferences(this)
        viewModel.updateLanguage(language, this)

        println("wswTest 有没有CAPDD ${blueToothKit.connectedCapnoEasy.value}")

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