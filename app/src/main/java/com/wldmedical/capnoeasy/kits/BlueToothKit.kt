package com.wldmedical.capnoeasy.kits

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_CLASSIC
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.PAIRED_DEVICE_KEY
import com.wldmedical.capnoeasy.USER_PREF_NS
import com.wldmedical.capnoeasy.WF_SPEED
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.HotmeltPinter
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.WeakHashMap
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.concurrent.withLock
import kotlin.math.pow
import kotlin.math.roundToInt

// 对 BluetoothDevice 进行扩展，下面是附加类
class BLEDeviceExtra {
    var gatt: BluetoothGatt? = null
    var sendDataService: BluetoothGattService? = null
    var sendDataCharacteristic: BluetoothGattCharacteristic? = null
    var receiveDataService: BluetoothGattService? = null
    var receiveDataCharacteristic: BluetoothGattCharacteristic? = null
    var antiHijackService: BluetoothGattService? = null
    var antiHijackCharacteristic: BluetoothGattCharacteristic? = null
    var antiHijackNotifyCharacteristic: BluetoothGattCharacteristic? = null
    var moduleParamsService: BluetoothGattService? = null
    var catchCharacteristic: MutableList<BluetoothGattCharacteristic?> = mutableListOf()
    var receivedArray: BlockingQueue<Byte> = LinkedBlockingQueue()
    val taskQueue = BluetoothTaskQueue()
    val sendArray = mutableListOf<Byte>()
    val receivedCO2WavedData: MutableList<DataPoint>? = mutableListOf()
}

// 反劫持传
val antiHijackStr = "301001301001"
val antiHijackData = antiHijackStr.toByteArray(Charsets.UTF_8)

val maxXPoints = 500

// 统一设备模型
enum class BluetoothType {
    BLE,
    CLASSIC,
    ALL
}

// 接收到的数据
data class DataPoint(val index: Int, val value: Float)

const val CHECK_SUCCESS = 0
const val NOT_SUPPORT_BLUETOOTH = 1
const val REQUEST_ENABLE_BT = 2
const val REQUEST_BLUETOOTH_CONNECT_PERMISSION = 3
const val REQUEST_BLUETOOTH_SCAN_PERMISSION = 4

/***
 * 蓝牙模块
 * 1、通过BLE机型扫描
 * 2、通过经典蓝牙进行扫描
 * 3、链接指定蓝牙设备
 * 4、保存扫描到的所有蓝牙设备
 * 5、保留配对蓝牙设备
 */
class BlueToothKit @Inject constructor(
    @ActivityContext private val activity: Activity,
    private val appState: AppStateModel
) {
    // 扩展低功耗蓝牙设备属性，全局存储（WeakHashMap 自动管理内存）
    private val deviceExtras = WeakHashMap<BluetoothDevice, BLEDeviceExtra>()

    // 扩展属性，下面为使用示例
    // val gatt = device.connectGatt(context, false, gattCallback)
    // device.extra.gatt = gatt // 保存 GATT
    val BluetoothDevice.extra: BLEDeviceExtra
        get() = deviceExtras.getOrPut(this) { BLEDeviceExtra() }

    /******************* 属性 *******************/
    // 获取主线程的 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val sendPeriod = 100L // 指令发送间隔，查询设备信息需要多条指令配置，互不依赖
    private var checkJob: Job? = null

    // 获取设备信息指令，由于该指令失败概率高，所以会周期性发送，直到成功
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchDeviceInfo() {
        checkJob?.cancel() // 取消之前的任务
        checkJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                println("wswTest 轮训设备信息外层的值 在循环")
                if (sHardwareVersion.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        taskQueue.addTask(
                            Runnable {
                                // ISB=21 硬件信息
                                sendArray.add(SensorCommand.Settings.value.toByte())
                                sendArray.add(0x02)
                                sendArray.add(ISBState84H.GetHardWareRevision.value.toByte())
                                sendSavedData()
                            }
                        )
                        taskQueue.executeTask()
                    }
                    delay(sendPeriod)
                }

                if (oemId.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        taskQueue.addTask(
                            Runnable {
                                // ISB=19 OEM ID
                                sendArray.add(SensorCommand.Settings.value.toByte())
                                sendArray.add(0x02)
                                sendArray.add(ISBState84H.GetOEMID.value.toByte())
                                sendSavedData()
                            }
                        )
                        taskQueue.executeTask()
                    }
                    delay(sendPeriod)
                }

                if (sSerialNumber.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        taskQueue.addTask(
                            Runnable {
                                // ISB=20 序列号
                                sendArray.add(SensorCommand.Settings.value.toByte())
                                sendArray.add(0x02)
                                sendArray.add(ISBState84H.GetSerialNumber.value.toByte())
                                sendSavedData()
                            }
                        )
                        taskQueue.executeTask()
                    }
                    delay(sendPeriod)
                }

                if (deviceName.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        taskQueue.addTask(
                            Runnable {
                                // ISB=18 型号
                                sendArray.add(SensorCommand.Settings.value.toByte())
                                sendArray.add(0x02)
                                sendArray.add(ISBState84H.GetSensorPartNumber.value.toByte())
                                sendSavedData()
                            }
                        )
                        taskQueue.executeTask()
                    }
                    delay(sendPeriod)
                }

                if (sSoftwareVersion.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        taskQueue.addTask(
                            Runnable {
                                sendArray.add(SensorCommand.GetSoftwareRevision.value.toByte())
                                sendArray.add(0x02)
                                sendArray.add(0x00)
                                sendSavedData()
                            }
                        )
                        taskQueue.executeTask()
                    }
                    delay(sendPeriod)
                }

                if (
                    sHardwareVersion.value.isNotEmpty() &&
                    oemId.value.isNotEmpty() &&
                    sSerialNumber.value.isNotEmpty() &&
                    deviceName.value.isNotEmpty() &&
                    sSoftwareVersion.value.isNotEmpty()
                ) {
                    break
                }
            }
        }
    }

    val taskQueue = BluetoothTaskQueue()

    private val audioIns = AudioPlayer(activity)

    private val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    public val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    @SuppressLint("MissingPermission", "NewApi")
    private fun writeDataToDevice(
        data: ByteArray? = sendArray.toByteArray(),
        type: Int = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE,
        characteristic: BluetoothGattCharacteristic? = currentSendDataCharacteristic
    ) {
        val result = currentGatt?.writeCharacteristic(
            characteristic!!,
            data!!,
            type
        )
        if (result == 4) {
            connectedCapnoEasy.value = null
            taskQueue.executeAllTasks()
        }
        println("wswTest result $result")
    }

    // 是否正在扫描BLE蓝牙设备
    private var isBLEScanning = false

    // 是否正在扫描经典蓝牙设备
    private var isClassicScanning = false

    private val SCAN_PERIOD: Long = 5000

    // BLE蓝牙-扫描回调
    private val leScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            if (device != null && !discoveredPeripherals.contains(device)) {
                if (device.name != null) {
                    discoveredPeripherals.add(device)
                    scanFind?.invoke(device)
                }
            }
        }

        // 扫描失败
        override fun onScanFailed(errorCode: Int) {}
    }

    private var scanFind: ((BluetoothDevice)-> Unit)? = null

    private var onDeviceConnectSuccess: ((BluetoothDevice?)-> Unit)? = null

    // BLE蓝牙-连接设备回调
    private val gattCallback = object : BluetoothGattCallback() {
        // 设备是否成功链接
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            println("wswTest 设备新装填 $newState")
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    println("wswTest 设备断开链接")
                    // 设备已断开连接
                    connectedCapnoEasy.value = null
                    taskQueue.executeAllTasks()
                }
                else -> {
                    // 其他状态
                    Log.d("GATT", "GATT state changed to $newState")
                }
            }
        }

        // 发现设备服务，集中处理service和characteristic
        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 设备成功连接，开始注册服务、特征值
                connectedCapnoEasyGATT.value = gatt

                val services = gatt.services
                val sortedServices = mutableListOf<BluetoothGattService>()

                // 根据 Service 的 UUID 进行排序
                for (uuid in sortedBLEServersUUID) {
                    val service = services.find { it.uuid == uuid }
                    if (service != null) {
                        sortedServices.add(service)
                    }
                }

                // 宣召有安歇service
                for (service in sortedServices) {
                    when(service.uuid) {
                        // 反劫持
                        BLEServersUUID.BLEAntihijackSer.value -> {
                            antiHijackService.add(service)
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 接受数据
                        BLEServersUUID.BLEReceiveDataSer.value -> {
                            receiveDataService.add(service)
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 发送数据
                        BLEServersUUID.BLESendDataSer.value -> {
                            sendDataService.add(service)
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 模块
                        BLEServersUUID.BLEModuleParamsSer.value -> {
                            moduleParamsService.add(service)
                            catchCharacteristic.addAll(service.characteristics)
                        }
                    }
                }
            
                // 有需要订阅的服务，寻找服务的特征值
                var filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEAntihijackCha.value }
                // 反劫持
                if (filterList.isNotEmpty()) {
                    antiHijackCharacteristic.add(filterList[0])
                    currentGatt!!.writeCharacteristic(filterList[0], antiHijackData, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                }

                filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEAntihijackChaNofi.value }
                // 监听反劫持广播
                if (filterList.isNotEmpty()) {
                    antiHijackNotifyCharacteristic.add(filterList[0])
                    currentGatt!!.setCharacteristicNotification(filterList[0], true)
                }

                filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value }
                // 接受数据
                if (filterList.isNotEmpty()) {
                    receiveDataCharacteristic.add(filterList[0])
                    currentGatt!!.setCharacteristicNotification(filterList[0], true)
                }

                // 所有初始化完成，执行后续操作
                sendContinuous()
                // 返回首页&&保存配对设备信息到本地
                onDeviceConnectSuccess?.invoke(connectedCapnoEasy.value)

                initCapnoEasyConection()
            }
        }

        // 用于接收设备发送的通知数据。
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            // 收到通知数据
            if (characteristic?.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value) {
                receivedArray.addAll(characteristic.value.toList())

                CoroutineScope(Dispatchers.Default).launch {
                    if (receivedArray.size >= 20) {
                        val firstArray = getCMDDataArray()
                        if (firstArray != null) {
                            getSpecificValue(firstArray.toByteArray());
                        }
                    }
                }
            }
        }

        // 处理特征值写入操作的结果，包括设置订阅状态
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            println("wswTEst 是否成功了 ${status}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                taskQueue.executeTask()
            }
        }
    }

    // 经典蓝牙-扫描回调
    private var discoveryReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && !discoveredPeripherals.contains(device)) {
                        if (device.name == null) { return }
                        // GP蓝牙打印机存在问题，
                        if (isGPPrinterName(device.name)) {
                            autoConnectPrinter(device)
                        }
                    }
                }
            }
        }
    }

    // 扫描的设备、服务、特征
    var sendDataService = mutableListOf<BluetoothGattService?>()
    
    var sendDataCharacteristic = mutableListOf<BluetoothGattCharacteristic?>()
    
    var receiveDataService = mutableListOf<BluetoothGattService?>()
    
    var receiveDataCharacteristic = mutableListOf<BluetoothGattCharacteristic?>()

    var antiHijackService = mutableListOf<BluetoothGattService?>()
    
    var antiHijackCharacteristic = mutableListOf<BluetoothGattCharacteristic?>()

    var antiHijackNotifyCharacteristic = mutableListOf<BluetoothGattCharacteristic?>()

    var moduleParamsService = mutableListOf<BluetoothGattService?>()

    // 蓝牙启动后需要监听的特征值列表
    val catchCharacteristic = mutableListOf<BluetoothGattCharacteristic>()

    // 周围设备列表
    public val discoveredPeripherals = mutableListOf<BluetoothDevice>()

    // 已链接设备-CapnoEasy
    public val connectedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 已链接设备-CapnoEasy GATT
    public var connectedCapnoEasyGATT = mutableStateOf<BluetoothGatt?>(null)

    // 当前正在展示的capnoEasy设备的序号
    public var connectedCapnoEasyIndex: Int = 0

    /***
     * 这些是跟随connectedCapnoEasyIndex变化一起变化的设备属性。对设备的操作、数据读取，都是通过这些属性
     */
    // 当前链接设备地址
    public val currentDeviceMacAddress: String?
        get() {
            val connectDevice = connectedCapnoEasy.value
            return connectDevice?.address
        }

    // 当前gatt
    public val currentGatt: BluetoothGatt?
        get() {
            return connectedCapnoEasyGATT.value
        }

    val currentSendDataService: BluetoothGattService?
        get() {
            if (currentGatt != null) {
                return currentGatt?.services?.find { it.uuid == BLEServersUUID.BLESendDataSer.value }
            }
            return null
        }

    val currentSendDataCharacteristic: BluetoothGattCharacteristic?
        get() {
            if (currentSendDataService != null) {
                return currentSendDataService?.characteristics?.find { it.uuid == BLECharacteristicUUID.BLESendDataCha.value }
            }
            return null
        }

    val currentReceiveDataService: BluetoothGattService?
        get() {
            if (currentGatt != null) {
                return currentGatt?.services?.find { it.uuid == BLEServersUUID.BLEReceiveDataSer.value }
            }
            return null
        }

    val currentReceiveDataCharacteristic: BluetoothGattCharacteristic?
        get() {
            if (currentReceiveDataService != null) {
                return currentReceiveDataService?.characteristics?.find { it.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value }
            }
            return null
        }

    val currentAntiHijackService: BluetoothGattService?
        get() {
            if (currentGatt != null) {
                return currentGatt?.services?.find { it.uuid == BLEServersUUID.BLEAntihijackSer.value }
            }
            return null
        }

    val currentAntiHijackCharacteristic: BluetoothGattCharacteristic?
        get() {
            if (currentAntiHijackService != null) {
                return currentAntiHijackService?.characteristics?.find { it.uuid == BLECharacteristicUUID.BLEAntihijackCha.value }
            }
            return null
        }

    val currentModuleParamsService: BluetoothGattService?
        get() {
            if (currentGatt != null) {
                return currentGatt?.services?.find { it.uuid == BLEServersUUID.BLEModuleParamsSer.value }
            }
            return null
        }

    // 已链接设备-打印机
    public val connectedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 佳博打印机SKD
    public val gpPrinterManager = HotmeltPinter()

    // 历史配对设备-CapnoEasy
    public val pairedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 历史配对设备-打印机
    public val pairedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 已经接收到的数据 - 已经解析出来的
    val receivedCO2WavedDataMap = HashMap<String, MutableList<DataPoint>>()

    private val receivedCO2WavedData: MutableList<DataPoint>?
        get() {
            return receivedCO2WavedDataMap[currentDeviceMacAddress]
        }

    // 内部值可变
    private val _dataFlow = MutableStateFlow<List<DataPoint>>(emptyList())

    // 对外值不可变
    val dataFlow: StateFlow<List<DataPoint>> = _dataFlow

    // 更细内部的值，触发数据更新
    fun updateReceivedData(newData: DataPoint) {
        val currentList = _dataFlow.value.toMutableList() // 获取当前列表并创建新的 MutableList

        // 更新 Flow 的值
        if (_dataFlow.value.size >= maxXPoints) {
            currentList.removeAt(0) // 删除头部元素
        }

        currentList.add(newData)
        _dataFlow.value = currentList // 更新 StateFlow
    }

    // 已经接收到的波次数据 - 原始数据，解析出后清空
    public var receivedArray: BlockingQueue<Byte> = LinkedBlockingQueue()

    // 准备发送给设备的命令数据 - 发送后清空
    public val sendArray = mutableListOf<Byte>()

    // 当前CO2值
    public var currentCO2: MutableState<Float> = mutableStateOf(0f)

    // 当前ETCO2值
    public var currentETCO2: MutableState<Float> = mutableStateOf(0f)

    // 当前呼吸率
    public var currentRespiratoryRate: MutableState<Int> = mutableStateOf(0)

    // 当前FiCO2
    public var currentFiCO2 = 0f

    // 当前是否还在呼吸
    public var currentBreathe: Boolean = false

    // 设置窒息时间
    public var asphyxiationTime: Int = 20
    
    // 设置氧气补偿
    public var oxygenCompensation: Float = 20f

    // 电池电量低
    public var isLowerEnergy: Boolean = false

    // 需要校零
    public var isNeedZeroCorrect: Boolean = false

    // 无适配器
    public var isAdaptorInvalid: Boolean = false

    // 适配器污染
    public var isAdaptorPolluted: Boolean = false

    // 是否正在校零
    public var isCorrectZero: Boolean = false

    // 是否窒息
    public var isAsphyxiation: Boolean = false

    // 设备名称
    public var deviceName: MutableState<String> = mutableStateOf("")

    // 设备制造商ID
    public var oemId: MutableState<String> = mutableStateOf("")

    // 设备序列号
    public var sSerialNumber: MutableState<String> = mutableStateOf("")

    // 大气压
    public var airPressure: MutableState<Float> = mutableStateOf(760f)

    // 设备硬件版本
    public var sHardwareVersion: MutableState<String> = mutableStateOf("")

    // 设备软件版本
    public var sSoftwareVersion: MutableState<String> = mutableStateOf("")

    // 生产日期
    public var productionDate: MutableState<String> = mutableStateOf("")

    private var correctZeroCallback: (() -> Unit)? = null

    /******************* 方法 *******************/
    // 判断蓝牙状态
    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBluetoothStatus(): Int {
        if (bluetoothAdapter == null) {
            return NOT_SUPPORT_BLUETOOTH
        }

        // 未开启蓝牙
        if (!bluetoothAdapter.isEnabled) {
            // 蓝牙未启用，请求用户启用蓝牙
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return REQUEST_ENABLE_BT
        }

        // 是否有链接权限
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 未获得授权，向用户申请权限
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_BLUETOOTH_CONNECT_PERMISSION
            )
            return REQUEST_BLUETOOTH_CONNECT_PERMISSION
        }

        // 是否有扫描权限
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 未获得扫描授权，向用户申请权限
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                REQUEST_BLUETOOTH_SCAN_PERMISSION
            )
            return REQUEST_BLUETOOTH_SCAN_PERMISSION
        }

        // 设备支持蓝牙
        return CHECK_SUCCESS
    }

    // 扫描低功耗蓝牙设备
    // https://developer.android.com/develop/connectivity/bluetooth/find-bluetooth-devices?hl=zh-cn
    @SuppressLint("MissingPermission")
    private fun scanBleDevices() {
        if (isBLEScanning) { return }

        isBLEScanning = true
        bluetoothLeScanner?.startScan(leScanCallback)
    }

    // 停止扫描低功耗蓝牙设备
    @SuppressLint("MissingPermission")
    private fun stopScanBleDevices() {
        if (!isBLEScanning) { return }

        isBLEScanning = false
        bluetoothLeScanner?.stopScan(leScanCallback)
    }

    // 扫描经典蓝牙
    @SuppressLint("MissingPermission")
    private fun scanClassicDevices() {
        if (isClassicScanning) { return }

        isClassicScanning = true
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(activity, discoveryReceiver, filter, ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS)
        isReceiverRegistered = true // 注册成功后，将标志位设置为 true

        bluetoothAdapter?.startDiscovery()
    }

    // 停止扫描经典蓝牙
    @SuppressLint("MissingPermission")
    private fun stopScanClassicDevices() {
        if (!isClassicScanning) { return }

        isClassicScanning = false
        if (isReceiverRegistered) { // 检查是否已注册
            try {
                activity.unregisterReceiver(discoveryReceiver)
                isReceiverRegistered = false // 取消注册成功后，将标志位设置为 false
            } catch (e: Exception) {
                Log.e("wswTest", "取消注册广播失败", e)
            }
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    private var isReceiverRegistered = false // 用于跟踪注册状态的标志位

    // 扫描流程集合
    @RequiresApi(Build.VERSION_CODES.S)
    public fun searchDevices(
        type: BluetoothType = BluetoothType.ALL,
        scanFinish: (() -> Unit)? = null, // 扫描结束
        scanFind: ((BluetoothDevice) -> Unit)? = null, // 扫描结束
        checkBlueToothFail: (() -> Unit)? = null
    ) {
        this.scanFind = scanFind
        // 检查蓝牙状态是否正确
        if (checkBluetoothStatus() != CHECK_SUCCESS) {
            checkBlueToothFail?.invoke()
            return
        }

        // 10秒后取消扫描
        handler.postDelayed({
            when(type) {
                BluetoothType.CLASSIC -> stopScanClassicDevices()
                BluetoothType.BLE -> stopScanBleDevices()
                else -> {
                    stopScanBleDevices()
                    stopScanClassicDevices()
                }
            }
            scanFinish?.invoke()
        }, SCAN_PERIOD)

        if (type == BluetoothType.ALL || type == BluetoothType.BLE) {
            // 开始扫描低功耗蓝牙
            scanBleDevices()
        }

        if (type == BluetoothType.ALL || type == BluetoothType.CLASSIC) {
            // 开始扫描经典蓝牙
            scanClassicDevices()
        }
    }

    // 链接低功耗蓝牙
    @SuppressLint("MissingPermission")
    private fun connectBleDevice(device: BluetoothDevice) {
        device.connectGatt(activity, false, gattCallback)
    }

    // 保存已经配对的设备
    public fun savePairedBLEDevice(context: Activity, device: BluetoothDevice?) {
        if (device == null) { return }
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PAIRED_DEVICE_KEY, device.address)
        editor.apply()
    }

    // 读取已配对设备
    public fun getSavedBLEDeviceAddress(context: Activity): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(USER_PREF_NS, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PAIRED_DEVICE_KEY, null)
    }

    /***
     * 自动连接打印机，目前按照值链接佳博打印机，按照名字做匹配
     */
    private fun autoConnectPrinter(device: BluetoothDevice) {
        if (connectedPrinter.value != null) { return }
        connectedPrinter.value = bluetoothAdapter?.getRemoteDevice(device.address)
        gpPrinterManager.connect(device.address)
    }

    // 判断设备名称是否满足佳博规则
    private fun isGPPrinterName(name: String): Boolean {
        val regex = Regex("^GP-(.+)_([\\w\\d\\p{Punct}]+)$")
        return regex.matches(name)
    }

    // 链接上CapnoEasy后，需要尽快发送初始化信息，否则会断开
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initCapnoEasyConection(getSystemInfo: Boolean = false) {
        // 写服务注册就绪，开始发送设备初初始化命令
        val filterList = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLESendDataCha.value }
        // 发送数据
        if (filterList.isNotEmpty()) {
            sendDataCharacteristic.add(filterList[0])
            if (getSystemInfo) {
                taskQueue.addTasks(
                    listOf(
                        // 读取单位前，必须要先停止设置
                        Runnable { sendStopContinuous() },
                        // 拉取设备信息
                        Runnable {
                            // ISB=1 大气压
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.AirPressure.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=21
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetHardWareRevision.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=20
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetSerialNumber.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=18
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetSensorPartNumber.value.toByte())
                            sendSavedData()
                        },
                        // 获取软件版本
                        Runnable {
                            sendArray.add(SensorCommand.GetSoftwareRevision.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(0x00)
                            sendSavedData()
                        },
                        // 设置结束后，开始尝试接受数据
                        Runnable { sendContinuous() },
                    )
                )
                taskQueue.executeTask()
            } else {
                taskQueue.addTasks(
                    listOf(
                        // 读取单位前，必须要先停止设置
                        Runnable { sendStopContinuous() },
                        // 显示设置
                        Runnable { updateCO2Unit(CO2_UNIT.MMHG) },
                        Runnable { updateCO2Scale(CO2_SCALE.MIDDLE) },
                        // 模块设置
                        Runnable { updateNoBreath(asphyxiationTime) },
                        Runnable { updateGasCompensation(oxygenCompensation) },
                        // 报警设置
                        Runnable { innerUpdateAlertRange(10f,20f, 10, 20) },
                        // 拉取设备信息
                        Runnable {
                            // ISB=1 大气压
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.AirPressure.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=21
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetHardWareRevision.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=20
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetSerialNumber.value.toByte())
                            sendSavedData()
                        },
                        Runnable {
                            // ISB=18
                            sendArray.add(SensorCommand.Settings.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(ISBState84H.GetSensorPartNumber.value.toByte())
                            sendSavedData()
                        },
                        // 获取软件版本
                        Runnable {
                            sendArray.add(SensorCommand.GetSoftwareRevision.value.toByte())
                            sendArray.add(0x02)
                            sendArray.add(0x00)
                            sendSavedData()
                        },
                        // 设置结束后，开始尝试接受数据
                        Runnable { sendContinuous() },
                    )
                )
            }
            // 间隔轮训设备信息
            fetchDeviceInfo()
        }
    }

    // 计算发送数据的校验和
    private fun calculateCKS(arr: ByteArray): Byte {
        var cks: Int = 0
        for (i in arr.indices) {
            cks += arr[i].toInt() and 0xFF // 使用 and 0xFF 确保转换为无符号整数
        }

        cks = cks.inv() + 1
        cks = cks and 0x7f

        return cks.toByte()
    }

    // 将cks放到发送数据的队尾
    private fun appendCKS() {
        sendArray.add(calculateCKS(sendArray.toByteArray()));
    }

    // 清空发送数据队列
    private fun resetSendData() {
        sendArray.clear()
    }

    // 发送接受波形数据请求
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun sendContinuous() {
        sendArray.add(SensorCommand.CO2Waveform.value.toByte())
        sendArray.add(0x02)
        sendArray.add(0x00)
        sendSavedData()
    }

    // 发送停止接受波形数据请求
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    private fun sendStopContinuous() {
        sendArray.add(SensorCommand.StopContinuous.value.toByte())
        sendArray.add(0x01)
        sendSavedData()
    }

    // 发送当前存储数据
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun sendSavedData() {
        if (connectedCapnoEasyGATT.value == null || currentSendDataCharacteristic == null) {
            resetSendData()
            return
        }
        appendCKS()
        writeDataToDevice()
        resetSendData()
    }

    /** 发送关机指令 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun shutdown(callback: (() -> Unit)? = null) {
        taskQueue.addTasks(
            listOf(
                Runnable { sendStopContinuous() },
                Runnable {
                    sendArray.add(SensorCommand.Reset.value.toByte())
                    sendArray.add(0x01)
                    sendSavedData()
                },
                Runnable { sendContinuous() },
                Runnable { callback?.invoke() },
            )
        )
        taskQueue.executeTask()
    }

    /** 发送校零指令 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun correctZero(callback: (() -> Unit)? = null) {
        correctZeroCallback = callback
        taskQueue.addTasks(
            listOf(
                Runnable { sendStopContinuous() },
                Runnable {
                    sendArray.add(SensorCommand.Zero.value.toByte())
                    sendArray.add(0x01)
                    sendSavedData()
                },
                Runnable { sendContinuous() },
            )
        )
        taskQueue.executeTask()
    }

    /** 更新CO2单位/CO2Scale */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateCO2UnitScale(
        co2Unit: CO2_UNIT? = null,
        co2Scale: CO2_SCALE? = null,
        wfSpeed: WF_SPEED? = null,
        callback: (() -> Unit)? = null
    ) {
        taskQueue.addTasks(
            listOf(
                Runnable { sendStopContinuous() },
                Runnable { updateCO2Unit(co2Unit) },
                Runnable { updateCO2Scale(co2Scale) },
                Runnable { sendContinuous() },
                Runnable { callback?.invoke() }
            )
        )
        taskQueue.executeTask()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateCO2Unit(
        co2Unit: CO2_UNIT? = null,
    ) {
        if (co2Unit != null) {
            // 设置CO2单位
            sendArray.add(SensorCommand.Settings.value.toByte())
            sendArray.add(0x03)
            sendArray.add(ISBState84H.SetCO2Unit.value.toByte())

            // TODO: 这里需要监听如果其他配置变化了，这块需要重置
            if (co2Unit == CO2_UNIT.MMHG) {
                sendArray.add(0x00)
            } else if (co2Unit == CO2_UNIT.KPA) {
                sendArray.add(0x01)
            } else if (co2Unit == CO2_UNIT.PERCENT) {
                sendArray.add(0x02)
            }
            sendSavedData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateCO2Scale(
        co2Scale: CO2_SCALE? = null,
    ) {
        if (co2Scale != null) {
            // 设置CO2Scale
            sendArray.add(SensorCommand.Expand.value.toByte())
            sendArray.add(0x03)
            sendArray.add(ISBStateF2H.CO2Scale.value.toByte())
            if (
                listOf(
                    CO2_SCALE.MIDDLE,
                    CO2_SCALE.KPA_MIDDLE,
                    CO2_SCALE.PERCENT_MIDDLE
                ).contains(co2Scale)
            ) {
                sendArray.add(0x00)
            } else if (
                listOf(
                    CO2_SCALE.SMALL,
                    CO2_SCALE.KPA_SMALL,
                    CO2_SCALE.PERCENT_SMALL
                ).contains(co2Scale)
            ) {
                sendArray.add(0x01)
            } else if (
                listOf(
                    CO2_SCALE.LARGE,
                    CO2_SCALE.KPA_LARGE,
                    CO2_SCALE.PERCENT_LARGE
                ).contains(co2Scale)
            ) {
                sendArray.add(0x02)
            }
            sendSavedData()
        }
    }

    /** 调整ETCO2/RR的报警范围 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateAlertRange(
        co2Low: Float = 0f,
        co2Up: Float = 0f,
        rrLow: Int = 0,
        rrUp: Int = 0,
        callback: (() -> Unit)? = null
    ) {
        taskQueue.addTasks(
            listOf(
                Runnable { sendStopContinuous() },
                Runnable { innerUpdateAlertRange(co2Low, co2Up, rrLow, rrUp) },
                Runnable { sendContinuous() },
                Runnable { callback?.invoke() }
            )
        )
        taskQueue.executeTask()
    }

    /** 调整ETCO2/RR的报警范围 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun innerUpdateAlertRange(
        co2Low: Float = 0f,
        co2Up: Float = 0f,
        rrLow: Int = 0,
        rrUp: Int = 0
    ) {
        if (co2Low.coerceAtLeast(co2Up) <= 0 && rrLow.coerceAtLeast(rrUp) <= 0) {
            return
        }
        // 准备发送数据到蓝牙设备上
        sendArray.add(SensorCommand.Expand.value.toByte())
        val _etCo2Upper = (co2Up * 10).roundToInt()
        val _etCo2Lower = (co2Low * 10).roundToInt()
        sendArray.add(0x0A)
        sendArray.add(0x2A) // ISB=42
        sendArray.add((_etCo2Upper shr 7).toByte())
        sendArray.add((_etCo2Upper and 0x7f).toByte())
        sendArray.add((_etCo2Lower shr 7).toByte())
        sendArray.add((_etCo2Lower and 0x7f).toByte())
        sendArray.add((rrUp shr 7).toByte())
        sendArray.add((rrUp and 0x7f).toByte())
        sendArray.add((rrLow shr 7).toByte())
        sendArray.add((rrLow and 0x7f).toByte())
        sendSavedData()
    }

    /** 设置模块参数: 窒息时间/氧气补偿 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateNoBreathAndCompensation(
        newAsphyxiationTime: Int = 0,
        newOxygenCompensation: Float = 0f,
        callback: (() -> Unit)? = null
    ) {
        taskQueue.addTasks(
            listOf(
                Runnable { sendStopContinuous() },
                Runnable { updateNoBreath(newAsphyxiationTime) },
                Runnable { updateGasCompensation(newOxygenCompensation) },
                Runnable { sendContinuous() },
                Runnable { callback?.invoke() }
            )
        )
        taskQueue.executeTask()
    }

    /** 设置模块参数: 窒息时间 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateNoBreath(
        newAsphyxiationTime: Int = 0,
    ) {
        // 设置窒息时间
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x03.toByte())
        sendArray.add(ISBState84H.NoBreaths.value.toByte())
        sendArray.add(newAsphyxiationTime.toByte())
        sendSavedData()
    }

    /** 设置模块参数: 氧气补偿 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateGasCompensation(
        newOxygenCompensation: Float = 0f
    ) {
        // 设置氧气补偿
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x06.toByte())
        sendArray.add(ISBState84H.GasCompensation.value.toByte())
        sendArray.add(newOxygenCompensation.toInt().toByte())
        sendArray.add(0x00)
        sendArray.add(0x00)
        sendArray.add(0x00)
        sendSavedData()
    }

    // 链接经典耗蓝牙
    private fun connectClassicDevice(device: BluetoothDevice) {}

    // 链接蓝牙设备
    @SuppressLint("MissingPermission")
    public fun connectDevice(
        device: BluetoothDevice?,
        onSuccess: ((BluetoothDevice?) -> Unit)? = null
    ) {
        if (device == null) {
            return
        }
        onDeviceConnectSuccess = onSuccess

        // 已连接设备中，存在相同mac地址的，放弃保存
        val isSaved = connectedCapnoEasy.value?.address == device.address

        if (isSaved) {
            return
        }

        // 链接后，需要将设备存到不同的属性中
        // 这里做了兼容：我的一加手机会把BLE设备偶尔识别为未知类型
        if (device.type == DEVICE_TYPE_LE || device.type == DEVICE_TYPE_UNKNOWN) {
            connectBleDevice(device)
            connectedCapnoEasy.value = device
            receivedCO2WavedDataMap[device.address] = mutableListOf()
        } else if (device.type == DEVICE_TYPE_CLASSIC) {
            connectClassicDevice(device)
            connectedCapnoEasy.value = device
            receivedCO2WavedDataMap[device.address] = mutableListOf()
        }
    }

    fun getCMDDataArray(): (List<Byte>)? {
        val getArray: MutableList<Byte> = mutableListOf() // 使用 MutableList 存储数据
        val command: Int = receivedArray.peek()?.toUByte()?.toInt() ?: 0 // 获取队首元素，如果队列为空则返回0

        // 从接受到数据头部不停的向后移动，直到获取指令类型
        while (receivedArray.isNotEmpty() && !supportCMDs.contains(command)) {
            receivedArray.poll() // 移除队首元素
        }

        // 数据长度为空，排除
        if (receivedArray.isEmpty()) {
            return null
        }

        // 根据NBF获取所有数据: 第二位是NBF长度，但是整体需要加上额外两位(指令位、NBF位)
        // 当长度为1的时候 有CMD，连NBF都没有，属于异常响应
        if (receivedArray.size == 1) {
            return null
        }

        val tmpList = receivedArray.toList()
        if (tmpList.size <= 1) {
            return  null
        }
        // 取此段指令所有数据: CMD + NBF + DB + CKS
        val nbf: Int = tmpList[1].toUByte().toInt() ?: 0
        val endIndex = nbf + 2

        if (receivedArray.size >= endIndex) {
            val getedArray = receivedArray.take(endIndex)
            removeFromBlockingQueue(receivedArray, endIndex)
            return getedArray
        }

        return null
    }

    private fun removeFromBlockingQueue(queue: BlockingQueue<Byte>, endIndex: Int) {
        val iterator = queue.iterator()
        var count = 0

        // 使用锁保证线程安全
        val lock = ReentrantLock()
        lock.withLock {
            while (iterator.hasNext() && count < endIndex) {
                iterator.next() // 获取元素，但不移除
                iterator.remove() // 移除当前元素
                count++
            }
        }
    }

    /** 从蓝牙返回数据中解析返回值 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getSpecificValue(firstArray: ByteArray) {
        if (firstArray.size < 2) {
            return
        }
        // 使用 ByteBuffer 直接访问内存，更安全高效
        val commandM = firstArray[0].toUByte().toInt()
        val NBFM = firstArray[1].toUByte().toInt() // 转换为无符号整数
        val cksM = firstArray.last().toUByte().toInt()

        if (!supportCMDs.contains(commandM)) {
            return
        }

        if (NBFM != firstArray.size - 2) {
            println("内存中长度不对 commandM:$commandM NBFM:$NBFM Total:${firstArray.size - 2}")
            return
        }

        val cks = calculateCKS(firstArray.dropLast(1).toByteArray()) // 注意 toByteArray() 的使用
        if (cks.toInt() != cksM) {
            println("checksum校验失败 cks:$cks 接收到的cks:$cksM")
            return
        }

        when (commandM) {
            SensorCommand.CO2Waveform.value -> {
                val result = kotlin.runCatching {
                    handleCO2Waveform(firstArray, NBFM)
                }
                result.onFailure {
                    println("wswTest 解析co2波形数据，发生异常 ${it.message} ")
                    it.printStackTrace()
                }
            }
            SensorCommand.Settings.value -> handleSettings(firstArray)
            SensorCommand.GetSoftwareRevision.value -> handleSofrWareVersion(firstArray)
            SensorCommand.Expand.value -> handleSystemExpand(firstArray)
            else -> println("wswTest 未知指令 $commandM")
        }
    }

    /** 处理CO2波形数据 */
    private fun handleCO2Waveform(data: ByteArray, NBFM: Int) {
        val DPIM = data[5].toUByte().toInt()
        var isValidETCO2 = true
        var isValidRR = true
        currentCO2.value = (128 * data[3].toUByte().toInt() + (data[4].toUByte().toInt()) - 1000).toFloat() / 100f

        if (NBFM > 4) {
            when (DPIM) {
                ISBState80H.CO2WorkStatus.value -> {
                    handleCO2Status(data, NBFM)
                    isNeedZeroCorrect = (data[7].toUByte().toInt() and 0x0C) == 0x0C
                    isAdaptorInvalid = (data[6].toUByte().toInt() and 0x02) == 0x02
                    isAdaptorPolluted = currentCO2.value < 0 && (data[6].toUByte().toInt() and 0x01) == 0x01
                }
                ISBState80H.ETCO2Value.value -> {
                    currentETCO2.value = (data[6].toUByte().toFloat() * 128 + (data[7].toUByte().toFloat())) / 10f
                    // 检测到呼吸。才能计算是否异常。
                    isValidETCO2 = !currentBreathe || (
                        currentETCO2.value <= appState.alertETCO2Range.value.start
                        && currentETCO2.value >= appState.alertETCO2Range.value.endInclusive
                    )
                                    println("wswTest【报警功能调试】 isValidETCO2 ${isValidETCO2}")
                }
                ISBState80H.RRValue.value -> {
                    currentRespiratoryRate.value = data[6].toUByte().toInt() * 128 + (data[7].toUByte().toInt())
                    isValidRR = !currentBreathe || (
                        currentRespiratoryRate.value <= appState.alertRRRange.value.start
                        && currentRespiratoryRate.value >= appState.alertRRRange.value.endInclusive
                    )
                    println("wswTest【报警功能调试】 isValidRR ${isValidRR}")
                }
                ISBState80H.FiCO2Value.value -> {
                    currentFiCO2 = ((data[6].toUByte().toInt() and 0xFF * 128 + (data[7].toUByte().toInt() and 0xFF)).toFloat() / 10)
                }
                ISBState80H.DetectBreath.value -> currentBreathe = true
                else -> println("CO2Waveform DPI 不匹配")
            }

            if (!isAsphyxiation
                && isValidETCO2
                && isValidRR
                && !isLowerEnergy
                && !isNeedZeroCorrect
                && !isAdaptorInvalid
                && !isAdaptorPolluted
            ) {
                audioIns.stopAudio()
            } else if (isAsphyxiation
                || !isValidETCO2
                || !isValidRR
                || isLowerEnergy
            ) {
                audioIns.playAlertAudio(AlertAudioType.MiddleLevelAlert)
            } else if (isNeedZeroCorrect
                || isAdaptorInvalid
                || isAdaptorPolluted
            ) {
                audioIns.playAlertAudio(AlertAudioType.LowLevelAlert)
            }
        }

        val currentPoint = DataPoint(
            index = receivedCO2WavedData?.size ?: 0,
            value = currentCO2.value,
        )
        // 用于在app主页展示的波形数据，是比最终保存的数据更完整
        // 但是只存在于应用运行期间
        receivedCO2WavedDataMap[currentDeviceMacAddress]?.add(currentPoint)

        // 保存到pdf中的数据和最终打印出来的数据
        // 是否保存这个数据，按照用户是否点击了开始记录开始算
        appState.updateTotalCO2WavedData(
            CO2WavePointData(
                co2 = currentCO2.value,
                RR = currentRespiratoryRate.value,
                ETCO2 = currentETCO2.value,
                FiCO2 = currentFiCO2,
                index = appState.totalCO2WavedData.size
            )
        )

        if ((receivedCO2WavedData?.size ?: 0) >= maxXPoints) {
            receivedCO2WavedDataMap[currentDeviceMacAddress]?.removeAt(0)
        }

        updateReceivedData(currentPoint)
    }

    /** 处理校零，校零结果会在80h中获取，DPI=1 */
    private fun handleCO2Status(data: ByteArray, NBFM: Int) {
        if (NBFM <= 1) {
            return
        }

        val ZSBM = data[7].toUByte().toInt() and 0x0C // 使用 & 0x0C 提取相关位

        when (ZSBM) {
            ZSBState.NOZeroning.value -> {
                if (isCorrectZero) {
                    correctZeroCallback?.invoke()
                    isCorrectZero = false
                }
            }
            ZSBState.Resetting.value -> isCorrectZero = true
            ZSBState.NotReady.value,
            ZSBState.NotReady2.value -> isCorrectZero = false
            else -> isCorrectZero = false
        }

        isAsphyxiation = (data[6].toUByte().toInt() and 0x40) == 0x40 // 检查是否置位
        println("wswTest【报警功能调试】 是否窒息 ${isAsphyxiation} ")
    }

    /** 处理设置： 序列号、硬件版本、设备名称 */
    @SuppressLint("DefaultLocale")
    private fun handleSettings(data: ByteArray) {
        when (data[2].toUByte().toInt() and 0xFF) { // 使用 Int 类型进行比较
            ISBState84H.GetSensorPartNumber.value -> {
                deviceName.value = ""
                for (i in 3..12) {
                    val uScalar = (data[i].toUByte().toInt()) and 0xFF
                    deviceName.value += uScalar.toChar().toString() // 使用 toChar() 转换为字符
                }
            }
            ISBState84H.GetOEMID.value -> {
                oemId.value = ""
                val uScalar = data[3].toUByte().toInt()
                oemId.value = uScalar.toString(16).uppercase()
            }
            ISBState84H.GetSerialNumber.value -> {
                val DB1 = data[3].toUByte().toInt().toDouble() * 2.0.pow(28)
                val DB2 = data[4].toUByte().toInt().toDouble() * 2.0.pow(21)
                val DB3 = data[5].toUByte().toInt().toDouble() * 2.0.pow(14)
                val DB4 = data[6].toUByte().toInt().toDouble() * 2.0.pow(7)
                val DB5 = data[7].toUByte().toInt().toDouble()
                val sNum = DB1 + DB2 + DB3 + DB4 + DB5
                sSerialNumber.value = String.format("%.0f", sNum)
            }
            ISBState84H.GetHardWareRevision.value -> {
                val DB1 = data[3].toUByte().toInt()
                val DB2 = data[4].toUByte().toInt()
                val DB3 = data[5].toUByte().toInt()
                sHardwareVersion.value = "${DB1.toChar()}.${DB2.toChar()}.${DB3.toChar()}"
            }
            ISBState84H.AirPressure.value -> {
                val DB1 = data[3].toUByte().toFloat()
                val DB2 = data[4].toUByte().toFloat()
                airPressure.value = 128 * DB1 + DB2
            }
            else -> println("模块参数设置 未知ISB")
        }
    }

    /** 获取生产日期、软件版本 */
    private fun handleSofrWareVersion(data: ByteArray) {
        val NBFM = data[1].toUByte().toInt() and 0xFF
        var rawSoftWareVersion = ""

        // 最后一位是cks，不取，前两位是cmd和nbf，同样不取
        for (i in 2 until NBFM + 1) {
            val uScalar = data[i].toUByte().toInt() and 0xFF
            rawSoftWareVersion += uScalar.toChar().toString()
        }

        try {
            val pattern = "(\\d{2})\\s(\\d{2}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2})"
            val regex = Pattern.compile(pattern)
            val matcher = regex.matcher(rawSoftWareVersion)

            if (matcher.find()) {
                val yearString = matcher.group(1)
                val dateTimeString = matcher.group(2)
                val fullTimeString = "$yearString $dateTimeString"

                sSoftwareVersion.value = rawSoftWareVersion.replace(matcher.group(), "").trim()
                sSoftwareVersion.value = sSoftwareVersion.value.replace(Regex("[\\p{Cc}]"), "") // Remove control characters
                sSoftwareVersion.value = sSoftwareVersion.value.replace(Regex("-+$"), "") // Remove trailing hyphens

                val formatter = SimpleDateFormat("yy MM/dd/yy HH:mm")
                try {
                    val date = formatter.parse(fullTimeString)
                    formatter.applyPattern("yyyy/MM/dd HH:mm:ss")
                    productionDate.value = formatter.format(date)
                    println("wswTEst Formatted Time: $productionDate")

                } catch (e: Exception) {
                    println("wswTEst Failed to parse date: ${e.message}")
                }
            } else {
                println("No match found")
            }
        } catch (e: Exception) {
            println("Invalid regex: ${e.message}")
        }
    }

    /** 处理系统扩展 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleSystemExpand(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)

        when (data[2].toUByte().toInt().toInt() and 0xFF) {
            ISBStateF2H.CO2Scale.value -> {
                sendContinuous()
            }
            ISBStateF2H.EnergyStatus.value -> {
                isLowerEnergy = (data[6].toUByte().toInt().toInt() and 0xFF) == 1
                println("查询是否低电量的DB ${data[3].toUByte().toInt()}-${data[4].toUByte().toInt()}-${data[5].toUByte().toInt()}-${data[6].toUByte().toInt()}")
            }
            else -> println("扩展指令未知场景")
        }
    }
}

object BlueToothKitManager {
    @SuppressLint("StaticFieldLeak")
    lateinit var blueToothKit: BlueToothKit

    fun initialize(
        activity: Activity,
        appState: AppStateModel,
        reInit: Boolean = false
    ) {
        if (::blueToothKit.isInitialized && !reInit) {
            return
        }
        blueToothKit = BlueToothKit(
            activity = activity,
            appState = appState,
        )
    }
}
