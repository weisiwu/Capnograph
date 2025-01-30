package com.wldmedical.capnoeasy.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wldmedical.capnoeasy.components.AlertData
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.NavBarComponentState
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.ToastData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * app运行时状态
 */
@Singleton
class AppState @Inject constructor() {
    // 当前所在页面
    val currentPage = mutableStateOf(NavBarComponentState(currentPage = PageScene.HOME_PAGE))

    // 当前激活tab index
    val currentTab = mutableIntStateOf(1)

    /**
     * 以下为全屏元素状态，BaseActivity中会通过状态进行判断
     * 比如准备Toast前，会先判断是否存在其他全屏元素
     * 如果存在，则先移除，然后再展示toast
     * 全屏元素包含
     * 1、Toast
     * 2、Alert
     * 3、Confirm
     * 4、Loading
     */
    // Toast数据
    val toastData: MutableState<ToastData?> = mutableStateOf(null)

    // Alert数据
    val alertData : MutableState<AlertData?> = mutableStateOf(null)

    // Confirm数据
    val confirmData: MutableState<ConfirmData?> = mutableStateOf(null)

    // Loading数据
    val loadingData: MutableState<LoadingData?> = mutableStateOf(null)

    // 是否保持屏幕常量
    val isKeepScreenOn = mutableStateOf(false)

    // 蓝牙设备列表 - 保存从蓝牙模块读取来的设备列表
    var devices: MutableList<Device> = mutableListOf()
}

@HiltViewModel
class AppStateModel @Inject constructor(private val appState: AppState): ViewModel() {
    // 当前所在页面
    val currentPage: State<NavBarComponentState> = appState.currentPage
    fun updateCurrentPage(newVal: PageScene) {
        appState.currentPage.value = NavBarComponentState(newVal)
    }

    // 当前激活tab index
    val currentTab: State<Int> = appState.currentTab
    fun updateCurrentTab(newVal: Int) {
        appState.currentTab.intValue = newVal
    }

    // Toast数据
    val toastData: State<ToastData?> = appState.toastData
    fun updateToastData(newVal: ToastData?) {
        clearXData()
        appState.toastData.value = newVal
    }

    // Alert数据
    val alertData: State<AlertData?> = appState.alertData
    fun updateAlertData(newVal: AlertData?) {
        clearXData()
        appState.alertData.value = newVal
    }

    // Confirm数据
    val confirmData: State<ConfirmData?> = appState.confirmData
    fun updateConfirmData(newVal: ConfirmData?) {
        clearXData()
        appState.confirmData.value = newVal
    }

    // Loading数据
    val loadingData: State<LoadingData?> = appState.loadingData
    fun updateLoadingData(newVal: LoadingData?) {
        clearXData()
        appState.loadingData.value = newVal
    }

    // X 指代全局组件
    fun clearXData() {
        val emptyVal = null
        appState.loadingData.value = emptyVal
        appState.confirmData.value = emptyVal
        appState.alertData.value = emptyVal
        appState.toastData.value = emptyVal
    }

    // 是否保持屏幕常量
    val isKeepScreenOn = appState.isKeepScreenOn
    fun updateKeepScreenOn(newVal: Boolean = false) {
        appState.isKeepScreenOn.value = newVal
    }

    // 蓝牙设备列表 - 保存从蓝牙模块读取来的设备列表
    val devices = appState.devices
    fun updateDevices(newVal: MutableList<Device>) {
        appState.devices = newVal
    }
}
