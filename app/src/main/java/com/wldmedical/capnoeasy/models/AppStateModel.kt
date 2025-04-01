package com.wldmedical.capnoeasy.models

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.charts.LineChart
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.LanguageTypes
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.WF_SPEED
import com.wldmedical.capnoeasy.components.AlertData
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceType
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.NavBarComponentState
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.kits.DataPoint
import com.wldmedical.capnoeasy.kits.maxRecordDataChunkSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

// 记录波形数据用格式
data class CO2WavePointData(
    val co2: Float = 0f,
    val RR: Int = 0,
    val ETCO2: Float = 0f,
    val FiCO2: Float = 0f,
    var index: Int = 0
): Serializable

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

    // 通用ActionModal弹框是否展示
    val showActionModal: MutableState<Boolean> = mutableStateOf(false)

    // 是否保持屏幕常量
    val isKeepScreenOn = mutableStateOf(false)

    // 蓝牙设备列表 - 保存从蓝牙模块读取来的设备列表
    var devices: MutableList<Device> = mutableListOf()

    // 搜索方式AKA链接设备方式
    val connectType: MutableState<DeviceType?> = mutableStateOf(null)

    // 是否正在记录波形数据
    val isRecording: MutableState<Boolean> = mutableStateOf(false)

    /***
     * 主页表格相关数据
     */
    // RR
    val rr: MutableState<Float> = mutableFloatStateOf(0f)

    // ETCO2
    val etCO2: MutableState<Float> = mutableFloatStateOf(0f)

    // 本次记录的所有capnoeasy波形数据, 此数据和app状态相关，所以放在这里。
    // 这个数据是用于绘制图表的
    var totalCO2WavedData = mutableListOf<CO2WavePointData>()

    // 本次记录的蓝牙数据流，这是用户点击开始记录后的数据全集
    val totalCO2WavedDataFlow = MutableStateFlow<List<CO2WavePointData>>(emptyList())

    /***
     * 设置相关
     */
    // 报警ETCO2范围
    val alertETCO2Range: MutableState<ClosedRange<Float>> = mutableStateOf(25f..50f)

    // 报警呼吸率范围
    val alertRRRange: MutableState<ClosedRange<Float>> = mutableStateOf(5f..30f)

    // CO2单位
    val CO2Unit: MutableState<CO2_UNIT> = mutableStateOf(CO2_UNIT.MMHG)

    // CO2 在主页刻度的最大范围
    val CO2Scale: MutableState<CO2_SCALE> = mutableStateOf(CO2_SCALE.MIDDLE)

    // 当前co2 scale
    var co2Scales: MutableList<CO2_SCALE> = mutableListOf(CO2_SCALE.SMALL, CO2_SCALE.MIDDLE, CO2_SCALE.LARGE)

    // WF Speed
    val WFSpeed: MutableState<WF_SPEED> = mutableStateOf(WF_SPEED.MIDDLE)

    // 窒息时间
    val asphyxiationTime: MutableState<Int> = mutableIntStateOf(10)

    // 氧气补偿
    val o2Compensation: MutableState<Float> = mutableFloatStateOf(16f)

    // 大气压
    val airPressure: MutableState<Float> = mutableFloatStateOf(760f)

    /***
     * 系统相关设置
     */
    // 语言
    val language: MutableState<LanguageTypes> = mutableStateOf(LanguageTypes.CHINESE)

    // app版本
    val appVersion: MutableState<String> = mutableStateOf("")

    // 是否在导出的PDF报告中展示趋势图
    val showTrendingChart: MutableState<Boolean> = mutableStateOf(true)

    // 固件版本
    val firmVersion: MutableState<String> = mutableStateOf("--")

    // 硬件版本
    val hardwareVersion: MutableState<String> = mutableStateOf("--")

    // 软件版本
    val softwareVersion: MutableState<String> = mutableStateOf("--")

    // 生产日期
    val productDate: MutableState<String> = mutableStateOf("--")

    // 序列号
    val serialNumber: MutableState<String> = mutableStateOf("--")

    // 模块名称
    val moduleName: MutableState<String> = mutableStateOf("CapnoGraph")

    /***
     * 打印相关数据
     */
    // 医院名称
    val pdfHospitalName: MutableState<String> = mutableStateOf("")

    // 报告名称
    val pdfReportName: MutableState<String> = mutableStateOf("")

    // 是否以PDF格式输出
    val isPDF: MutableState<Boolean> = mutableStateOf(true)

    // 病人相关数据都会出现在打印结果中
    // 病人姓名
    val patientName: MutableState<String?> = mutableStateOf(null)

    // 性别
    val patientGender: MutableState<GENDER?> = mutableStateOf(null)

    // 年龄
    val patientAge: MutableState<Int?> = mutableStateOf(null)

    // ID
    val patientID: MutableState<String?> = mutableStateOf(null)

    // 科室
    val patientDepartment: MutableState<String?> = mutableStateOf(null)

    // 病床号
    val patientBedNumber: MutableState<String?> = mutableStateOf(null)

    /***
     * 实时蓝牙设备数据
     */
    // 周围设备列表
    var discoveredPeripherals = MutableStateFlow<List<BluetoothDevice>>(emptyList())
}

@HiltViewModel
class AppStateModel @Inject constructor(
    private val appState: AppState,
): ViewModel() {
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

    // 通用ActionModal弹框是否展示
    val showActionModal: State<Boolean> = appState.showActionModal
    fun updateShowActionModal(newVal: Boolean = false) {
        clearXData()
        appState.showActionModal.value = newVal
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

    // 搜索方式AKA链接设备方式
    val connectType = appState.connectType
    fun updateConnectType(newVal: DeviceType?) {
        appState.connectType.value = newVal
    }

    // 是否正在记录波形数据
    val isRecording = appState.isRecording
    fun updateIsRecording(newVal: Boolean) {
        appState.isRecording.value = newVal
    }

    /***
     * 主页表格相关数据
     */
    // RR
    val rr = appState.rr
    fun updateRR(newVal: Float) {
        appState.rr.value = newVal
    }

    // ETCO2
    val etCO2 = appState.etCO2
    fun updateETCO2(newVal: Float) {
        appState.etCO2.value = newVal
    }

    // 病人姓名
    val patientName = appState.patientName
    fun updatePatientName(newVal: String) {
        appState.patientName.value = newVal
    }

    // 性别
    val patientGender = appState.patientGender
    fun updatePatientGender(newVal: GENDER) {
        appState.patientGender.value = newVal
    }

    // 年龄
    val patientAge = appState.patientAge
    fun updatePatientAge(newVal: Int) {
        appState.patientAge.value = newVal
    }

    // ID
    val patientID = appState.patientID
    fun updatePatientID(newVal: String) {
        appState.patientID.value = newVal
    }

    // 科室
    val patientDepartment = appState.patientDepartment
    fun updatePatientDepartment(newVal: String) {
        appState.patientDepartment.value = newVal
    }

    // 床号
    val patientBedNumber = appState.patientBedNumber
    fun updatePatientBedNumber(newVal: String) {
        appState.patientBedNumber.value = newVal
    }

    // 本次记录的蓝牙波形数据全集，从点击开始记录算起，到结束记录
    val totalCO2WavedData = appState.totalCO2WavedData
    val totalCO2WavedDataFlow: StateFlow<List<CO2WavePointData>> = appState.totalCO2WavedDataFlow
    fun updateTotalCO2WavedData(newVal: CO2WavePointData? = null) {
        // 有值且在记录中
       if (appState.isRecording.value && newVal != null) {
            appState.totalCO2WavedData.add(newVal)
            // 更新 StateFlow 的值
            appState.totalCO2WavedDataFlow.value = appState.totalCO2WavedData.toList()
        }
        // 未传入值且停止记录了，对数据做清空（本次记录已经完成）
       if (!appState.isRecording.value && newVal == null) {
            appState.totalCO2WavedData.clear()
            appState.totalCO2WavedDataFlow.value = appState.totalCO2WavedData.toList()
        }
    }
    // 对内存中存储的全部波形数据做取chunk操作
    fun delSavedCO2WavedDataChunk() {
        appState.totalCO2WavedData.subList(0, maxRecordDataChunkSize.coerceAtLeast(appState.totalCO2WavedData.size - 1)).clear()
        appState.totalCO2WavedDataFlow.value = appState.totalCO2WavedData.toList()
    }

    /***
     * 设置相关
     */
    // 报警ETCO2范围
    val alertETCO2Range = appState.alertETCO2Range
    fun updateAlertETCO2Range(start: Float, end: Float) {
        appState.alertETCO2Range.value = start..end
    }

    // 报警呼吸率范围
    val alertRRRange = appState.alertRRRange
    fun updateAlertRRRange(start: Float, end: Float) {
        appState.alertRRRange.value = start..end
    }

    // 设置CO2单位
    val CO2Unit = appState.CO2Unit
    fun updateCO2Unit(newVal: CO2_UNIT) {
        appState.CO2Unit.value = newVal
    }

    // 设置CO2 Scale（主页线图的纵坐标范围）
    val CO2Scale = appState.CO2Scale
    fun updateCO2Scale(newVal: CO2_SCALE) {
        appState.CO2Scale.value = newVal
    }

    // 当前co2 scale
    val co2Scales = appState.co2Scales
    fun updateCo2Scales(newVal: MutableList<CO2_SCALE>) {
        appState.co2Scales = newVal
    }

    // WF Speed
    val WFSpeed = appState.WFSpeed
    fun updateWFSpeed(newVal: WF_SPEED) {
        appState.WFSpeed.value = newVal
    }

    // 窒息时间
    val asphyxiationTime = appState.asphyxiationTime
    fun updateAsphyxiationTime(newVal: Int) {
        appState.asphyxiationTime.value = newVal
    }

    // 氧气补偿
    val o2Compensation = appState.o2Compensation
    fun updateO2Compensation(newVal: Float) {
        appState.o2Compensation.value = newVal
    }

    // 大气压
    val airPressure = appState.airPressure
    fun updateAirPressure(newVal: Float) {
        appState.airPressure.value = newVal
    }

    // 大气压
    val appVersion = appState.appVersion
    fun updateAppVersion(newVal: String) {
        appState.appVersion.value = newVal
    }

    // 语言
    val language = appState.language
    fun updateLanguage(newVal: LanguageTypes, context: Activity) {
        if (newVal == appState.language.value) {
            return
        }
        appState.language.value = newVal
        val languageCode = if (newVal == LanguageTypes.CHINESE) "zh" else "en"
        // 更新语言的同时，切换整个APP语言设置
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val localeList = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(localeList)
        // 更新Configuration
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        context.recreate()
    }

    val showTrendingChart = appState.showTrendingChart
    fun updateShowTrendingChart(newVal: Boolean) {
        appState.showTrendingChart.value = newVal
    }

    // 固件版本
    val firmVersion = appState.firmVersion
    fun updateFirmVersion(newVal: String) {
        appState.firmVersion.value = newVal
    }

    // 硬件版本
    val hardwareVersion = appState.hardwareVersion
    fun updateHardwareVersion(newVal: String) {
        appState.hardwareVersion.value = newVal
    }

    // 软件版本
    val softwareVersion = appState.softwareVersion
    fun updateSoftwareVersion(newVal: String) {
        appState.softwareVersion.value = newVal
    }

    // 生产日期
    val productDate = appState.productDate
    fun updateProductDate(newVal: String) {
        appState.productDate.value = newVal
    }

    // 序列号
    val serialNumber = appState.serialNumber
    fun updateSerialNumber(newVal: String) {
        appState.serialNumber.value = newVal
    }

    // 模块名称
    val moduleName = appState.moduleName
    fun updateModuleName(newVal: String) {
        appState.moduleName.value = newVal
    }

    // pdf设置-医院名称
    val pdfHospitalName = appState.pdfHospitalName
    fun updatePdfHospitalName(newVal: String) {
        appState.pdfHospitalName.value = newVal
    }

    // pdf设置-报告名称
    val pdfReportName = appState.pdfReportName
    fun updatePdfReportName(newVal: String) {
        appState.pdfReportName.value = newVal
    }

    // pdf设置-是否以PDF格式输出
    val isPDF = appState.isPDF
    fun updateIsPDF(newVal: Boolean) {
        appState.isPDF.value = newVal
    }

    // 附近蓝牙设备-扫描结果
    val discoveredPeripherals: StateFlow<List<BluetoothDevice>> = appState.discoveredPeripherals
    fun updateDiscoveredPeripherals(item: BluetoothDevice?, isClear: Boolean = false) {
        val currentList = discoveredPeripherals.value.orEmpty().toMutableList()
        if (item != null && !currentList.contains(item)) {
            currentList.add(item)
            appState.discoveredPeripherals.value = currentList.toList()
        } else if(isClear) {
            appState.discoveredPeripherals.value = mutableListOf()
        }
    }

    // 折线图节点，保存PDF使用
    @SuppressLint("StaticFieldLeak")
    var lineChart: LineChart? = null
}
