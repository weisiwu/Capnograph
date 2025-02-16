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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.concurrent.withLock
import kotlin.math.pow
import kotlin.math.roundToInt

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

const val unkownName = "未知设备"

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
    /******************* 属性 *******************/
    // 获取主线程的 Handler
    val handler = Handler(Looper.getMainLooper())

    val taskQueue = BluetoothTaskQueue()

    private val audioIns = AudioPlayer(activity)

    private val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    public val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    @SuppressLint("MissingPermission", "NewApi")
    private fun writeDataToDevice(
        data: ByteArray? = sendArray.toByteArray(),
        type: Int = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE,
        characteristic: BluetoothGattCharacteristic? = sendDataCharacteristic
    ) {
        val result = connectedCapnoEasyGATT?.writeCharacteristic(
            characteristic!!,
            data!!,
            type
        )
//        println("wswTest 写入结果 ${result}")
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
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    // 设备已断开连接
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
                connectedCapnoEasyGATT = gatt

                val services = connectedCapnoEasyGATT!!.services
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
                            antiHijackService = service
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 接受数据
                        BLEServersUUID.BLEReceiveDataSer.value -> {
                            receiveDataService = service
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 发送数据
                        BLEServersUUID.BLESendDataSer.value -> {
                            sendDataService = service
                            catchCharacteristic.addAll(service.characteristics)
                        }
                        // 模块
                        BLEServersUUID.BLEModuleParamsSer.value -> {
                            moduleParamsService = service
                            catchCharacteristic.addAll(service.characteristics)
                        }
                    }
                }
            
                // 有需要订阅的服务，寻找服务的特征值
                var filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEAntihijackCha.value }
                // 反劫持
                if (filterList.isNotEmpty()) {
                    antiHijackCharacteristic = filterList[0]
                    connectedCapnoEasyGATT!!.writeCharacteristic(filterList[0], antiHijackData, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                }

                filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEAntihijackChaNofi.value }
                // 监听反劫持广播
                if (filterList.isNotEmpty()) {
                    antiHijackNotifyCharacteristic = filterList[0]
                    connectedCapnoEasyGATT!!.setCharacteristicNotification(filterList[0], true)
                }

                filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value }
                // 接受数据
                if (filterList.isNotEmpty()) {
                    receiveDataCharacteristic = filterList[0]
                    connectedCapnoEasyGATT!!.setCharacteristicNotification(filterList[0], true)
                }

                // 将任务注册进入，等待onCharacteristicWrite回调触发任务队列
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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                taskQueue.executeTask()
            }
        }
    }

    // 经典蓝牙-扫描回调
    private val discoveryReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && !discoveredPeripherals.contains(device)) {
                        if (device.name == null) { return }
                        println("wswTest 设备名称 ${device.name} === ${isGPPrinterName(device.name)}")
                        // GP蓝牙打印机存在问题，
                        if (isGPPrinterName(device.name)) {
                            autoConnectPrinter(device)
                        }
                        // 目前没有涌上经典蓝牙的设备，所以不需要展示他们和连接他们
                        // 蓝牙打印机虽然使用了经典蓝牙的链接方式，但是为自动连接
                        // discoveredPeripherals.add(device)
                        // scanFind?.invoke(device)
                    }
                }
            }
        }
    }

    // 扫描的设备、服务、特征
    var sendDataService: BluetoothGattService? = null
    
    var sendDataCharacteristic: BluetoothGattCharacteristic? = null
    
    var receiveDataService: BluetoothGattService? = null
    
    var receiveDataCharacteristic: BluetoothGattCharacteristic? = null

    var antiHijackService: BluetoothGattService? = null
    
    var antiHijackCharacteristic: BluetoothGattCharacteristic? = null

    var antiHijackNotifyCharacteristic: BluetoothGattCharacteristic? = null

    var moduleParamsService: BluetoothGattService? = null

    // 蓝牙启动后需要监听的特征值列表
    val catchCharacteristic = mutableListOf<BluetoothGattCharacteristic>()

    // 周围设备列表
    public val discoveredPeripherals = mutableListOf<BluetoothDevice>()

    // 已链接设备-CapnoEasy
    public val connectedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 已链接设备-CapnoEasy GATT
    public var connectedCapnoEasyGATT: BluetoothGatt? = null

    // 已链接设备-打印机
    public val connectedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 佳博打印机SKD
    public val gpPrinterManager = HotmeltPinter()

    // 历史配对设备-CapnoEasy
    public val pairedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 历史配对设备-打印机
    public val pairedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 已经接收到的数据 - 已经解析出来的
    private val receivedCO2WavedData = mutableListOf<DataPoint>()

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

    // 设备序列号
    public var sSerialNumber: MutableState<String> = mutableStateOf("")

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

        bluetoothAdapter?.startDiscovery()
    }

    // 停止扫描经典蓝牙
    @SuppressLint("MissingPermission")
    private fun stopScanClassicDevices() {
        if (!isClassicScanning) { return }

        isClassicScanning = false
        activity.unregisterReceiver(discoveryReceiver)
        bluetoothAdapter?.cancelDiscovery()
    }

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
            println("wswTest 开始经典蓝牙扫描")
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
        gpPrinterManager.connect(device.address, false)
    }

    // 判断设备名称是否满足佳博规则
    private fun isGPPrinterName(name: String): Boolean {
        val regex = Regex("^GP-(.+)_([\\w\\d\\p{Punct}]+)$")
        return regex.matches(name)
    }

    // 链接上CapnoEasy后，需要尽快发送初始化信息，否则会断开
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initCapnoEasyConection() {
        // 写服务注册就绪，开始发送设备初初始化命令
        val filterList = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLESendDataCha.value }
        // 发送数据
        if (filterList.isNotEmpty()) {
            sendDataCharacteristic = filterList[0]
            // TODO: 默认值需要修改
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
                    // 设置结束后，开始尝试接受数据
                    Runnable { sendContinuous() },
                    // 返回首页&&保存配对设备信息到本地
                    Runnable { onDeviceConnectSuccess?.invoke(connectedCapnoEasy.value) },
                )
            )
        }
    }

    private fun calculateCKS(arr: ByteArray): Byte {
        var cks: Int = 0
        for (i in arr.indices) {
            cks += arr[i].toInt() and 0xFF // 使用 and 0xFF 确保转换为无符号整数
        }

        cks = cks.inv() + 1
        cks = cks and 0x7f

        return cks.toByte()
    }

    private fun appendCKS() {
        sendArray.add(calculateCKS(sendArray.toByteArray()));
    }

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
        if (connectedCapnoEasyGATT == null || sendDataCharacteristic == null) {
            resetSendData()
            return
        }
        appendCKS()
        writeDataToDevice()
        resetSendData()
    }

    /**
     * 获取蓝牙设备信息
     * 通过84H获取
     * 硬件版本(ISB=21)
     * serial Number(ISB=20)
     * sensor part number(ISB=18)
     * 通过CAH获取
     * 软件版本
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun getDeviceInfo() {
        // ISB=21
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x02)
        sendArray.add(ISBState84H.GetHardWareRevision.value.toByte())
        sendSavedData()

        // ISB=20
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x02)
        sendArray.add(ISBState84H.GetSerialNumber.value.toByte())
        sendSavedData()

        // ISB=18
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x02)
        sendArray.add(ISBState84H.GetSensorPartNumber.value.toByte())
        sendSavedData()

        // 获取软件版本
        sendArray.add(SensorCommand.GetSoftwareRevision.value.toByte())
        sendArray.add(0x02)
        sendArray.add(0x00)
        sendSavedData()
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
                Runnable { callback?.invoke() }
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

        // TODO: 待添加对wfSpeed的支持
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
    private fun connectClassicDevice(device: BluetoothDevice) {
        // val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        // pairedDevices?.forEach { device ->
        //     val deviceName = device.name
        //     val deviceHardwareAddress = device.address // MAC address
        // }
    }

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
        // 链接后，需要将设备存到不同的属性中
        // 这里做了兼容：我的一加手机会把BLE设备偶尔识别为未知类型
        if (device.type == DEVICE_TYPE_LE || device.type == DEVICE_TYPE_UNKNOWN) {
            connectBleDevice(device)
            connectedCapnoEasy.value = device
        } else if (device.type == DEVICE_TYPE_CLASSIC) {
            connectClassicDevice(device)
            connectedCapnoEasy.value = device
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
            else -> println("未知指令 $commandM")
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
                        currentETCO2.value <= appState.alertETCO2Range.value.start && currentETCO2.value >= appState.alertETCO2Range.value.endInclusive
                    )
                }
                ISBState80H.RRValue.value -> {
                    currentRespiratoryRate.value = data[6].toUByte().toInt() * 128 + (data[7].toUByte().toInt())
                    isValidRR = !currentBreathe || (
                        currentRespiratoryRate.value <= appState.alertRRRange.value.endInclusive && currentRespiratoryRate.value >= appState.alertRRRange.value.endInclusive
                    )
                }
                ISBState80H.FiCO2Value.value -> {
                    currentFiCO2 = ((data[6].toUByte().toInt().toInt() and 0xFF * 128 + (data[7].toUByte().toInt().toInt() and 0xFF)).toFloat() / 10)
                }
                ISBState80H.DetectBreath.value -> currentBreathe = true
                else -> println("CO2Waveform DPI 不匹配")
            }

            // TODO: 报警的逻辑，后续再加
             if (!isAsphyxiation && isValidETCO2 && isValidRR && !isLowerEnergy && !isNeedZeroCorrect && !isAdaptorInvalid && !isAdaptorPolluted) {
                 audioIns.stopAudio()
             } else if (isAsphyxiation || !isValidETCO2 || !isValidRR || isLowerEnergy) {
                 audioIns.playAlertAudio(AlertAudioType.MiddleLevelAlert)
             } else if (isNeedZeroCorrect || isAdaptorInvalid) {
                 // TODO: 临时将  isAdaptorPolluted 删除，因为其值恒为true
                // } else if (isNeedZeroCorrect || isAdaptorInvalid || isAdaptorPolluted) {
                 audioIns.playAlertAudio(AlertAudioType.LowLevelAlert)
             }
        }

        val currentPoint = DataPoint(
            index = receivedCO2WavedData.size,
            value = currentCO2.value,
        )
        receivedCO2WavedData.add(currentPoint)

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

        if (receivedCO2WavedData.size >= maxXPoints) {
            receivedCO2WavedData.removeAt(0)
        }

        // TODO: 临时这么写，后续不这么写，临时把波形打印出来
        // 后续要将波形打印做成筛选后打印
        gpPrinterManager.update(currentCO2.value)

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
    }

    /** 处理设置： 序列号、硬件版本、设备名称 */
    @SuppressLint("DefaultLocale")
    private fun handleSettings(data: ByteArray) {
        when (data[2].toUByte().toInt() and 0xFF) { // 使用 Int 类型进行比较
            ISBState84H.GetSensorPartNumber.value -> {
                deviceName.value = ""
                for (i in 0..9) {
                    val uScalar = (data[i].toUByte().toInt()+ 3) and 0xFF
                    deviceName.value += uScalar.toChar().toString() // 使用 toChar() 转换为字符
                }
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
            else -> println("模块参数设置 未知ISB")
        }
    }

    /** 获取生产日期、软件版本 */
    private fun handleSofrWareVersion(data: ByteArray) {
        val NBFM = data[1].toUByte().toInt() and 0xFF
        var rawSoftWareVersion = ""

        for (i in 0 until NBFM) {
            val uScalar = (data[i].toUByte().toInt()+ 3) and 0xFF
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
                    println("Formatted Time: $productionDate")

                } catch (e: Exception) {
                    println("Failed to parse date: ${e.message}")
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
        appState: AppStateModel
    ) {
        if (::blueToothKit.isInitialized) {
            return
        }
        blueToothKit = BlueToothKit(
            activity = activity,
            appState = appState,
        )
    }
}
