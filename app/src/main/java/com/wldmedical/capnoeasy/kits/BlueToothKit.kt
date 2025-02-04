package com.wldmedical.capnoeasy.kits

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_CLASSIC
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import dagger.hilt.android.qualifiers.ActivityContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.LinkedList
import java.util.Timer
import java.util.TimerTask
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.experimental.and
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

// 记录波形数据用格式
data class CO2WavePointData(
    val co2: Float = 0f,
    val RR: UInt = 0u,
    val ETCO2: Float = 0f,
    val FiCO2: Float = 0f,
    val index: Int = 0
)

/**
 * 报警类型
 */
enum class AudioType {
    // 低级报警
    // 技术报警: 需要零点校准/无适配器/适配器污染
    LowLevelAlert,
    // 中级报警
    // 生理报警: ETCO2值低/ETCO2值高/RR 值高/RR值低/窒息/电池电量低
    MiddleLevelAlert
}

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
    private val checkBlueToothFail: (() -> Unit)? = null, // 权限检查失败
    private val SCAN_PERIOD: Long = 5000,
    private val onScanFind: ((BluetoothDevice) -> Unit)? = null, // 扫描结束
    private val scanFinish: (() -> Unit)? = null, // 扫描结束
) {
    /******************* 属性 *******************/
    // 获取主线程的 Handler
    val handler = Handler(Looper.getMainLooper())

    private val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    // 蓝牙指令任务队列，避免快速写导致外围设备无法响应
    private val messageQueue = LinkedList<ByteArray>()

    private var isSending = false

    private var timer: Timer? = null

    private fun sendMessage(
        gatt: BluetoothGatt? = connectedCapnoEasyGATT,
        data: ByteArray? = sendArray.toByteArray(),
        type: Int = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE,
        characteristic: BluetoothGattCharacteristic? = sendDataCharacteristic
    ) {
        messageQueue.offer(data!!.copyOf()) // 将消息放入队列

        if (!isSending) {
            println("wswTest 开启了timer")
            startTimer(gatt, type, characteristic) // 启动定时器
        }
    }

    private fun startTimer(gatt: BluetoothGatt?, type: Int, characteristic: BluetoothGattCharacteristic?) {
        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    println("wswTest 开始执行下一条数据 ${characteristic!!.uuid}")
                    sendNextMessage(gatt, type, characteristic)
                }
            },
            0, // 延迟 0 毫秒后开始执行
             300 // 每 100 毫秒执行一次
        )
    }

    private fun stopTimer() {
        println("wswTest 关闭了timer")
        timer?.cancel()
        timer = null
    }

    @SuppressLint("MissingPermission", "NewApi")
    private fun sendNextMessage(gatt: BluetoothGatt?, type: Int, characteristic: BluetoothGattCharacteristic?) {
        println("wswTest messageQueue ${messageQueue.size} isSending ${isSending}")
        if (messageQueue.isNotEmpty() && !isSending) {
            isSending = true
            val message = messageQueue.poll()
            val status = message?.let {
                gatt?.writeCharacteristic(
                    characteristic!!,
                    it,
                    type
                )
            }

            if (status == BluetoothStatusCodes.SUCCESS) {
                println("wswTest 消息发送成功 ${status} ${characteristic!!.uuid}")
            } else {
                println("wswTest 消息发送失败 ${status} ${characteristic!!.uuid}")
            }
            isSending = false
        } else if (messageQueue.isEmpty()) {
            stopTimer() // 队列为空，停止定时器
            isSending = false
        }
    }

    // 是否正在扫描
    private var scanning = false

    // BLE蓝牙-扫描回调
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            if (device != null && !discoveredPeripherals.contains(device)) {
                discoveredPeripherals.add(device)
                onScanFind?.invoke(device)
            }
        }

        // 扫描失败
        override fun onScanFailed(errorCode: Int) {}
    }

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
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 设备已连接
                initCapnoEasyConection(gatt)
            }
        }

        // 用于接收设备发送的通知数据。
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            println("wswTest 特征值发生改变 ${characteristic?.uuid}")
            // 收到通知数据
            if (characteristic != null) { }
        }

        // 处理特征值写入操作的结果，包括设置订阅状态
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            println("wswTest 开始写入特征值 ${characteristic?.uuid}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                println("wswTest 设置订阅状态成功: ${characteristic?.uuid}")
                // 订阅receiveData成功后，发送链接请求
                if (characteristic?.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value) {
                    sendStopContinuous() // 停止历史可能存在数据串
                    sendContinuous() // 重新开始请求新的数据串
                }
            } else {
                println("wswTest 设置订阅状态失败: ${characteristic?.uuid}，状态码: $status")
            }
        }
    }

    // 经典蓝牙-扫描回调
    private val discoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null && !discoveredPeripherals.contains(device)) {
                    discoveredPeripherals.add(device)
                    onScanFind?.invoke(device)
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

    var moduleParamsCharacteristic: BluetoothGattCharacteristic? = null

    // 周围设备列表
    public val discoveredPeripherals = mutableListOf<BluetoothDevice>()

    // 已链接设备-CapnoEasy
    public val connectedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 已链接设备-CapnoEasy GATT
    public var connectedCapnoEasyGATT: BluetoothGatt? = null

    // 已链接设备-打印机
    public val connectedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 历史配对设备-CapnoEasy
    public val pairedCapnoEasy = mutableStateOf<BluetoothDevice?>(null)

    // 历史配对设备-打印机
    public val pairedPrinter = mutableStateOf<BluetoothDevice?>(null)

    // 已经接收到的数据 - 已经解析出来的
    public val receivedCO2WavedData = mutableListOf<DataPoint>()

    // 已经接收到的波次数据 - 原始数据，解析出后清空
    public val receivedArray = mutableListOf<UInt>()

    // 准备发送给设备的命令数据 - 发送后清空
    public val sendArray = mutableListOf<Byte>()

    // 当前CO2值
    public var currentCO2: Float = 0f

    // 本次记录的所有波形数据
    public var totalCO2WavedData = mutableListOf<CO2WavePointData>()

    // 当前ETCO2值
    public var currentETCO2: Float = 0f

    // 当前呼吸率
    public var currentRespiratoryRate: UInt = 0u

    // 当前FiCO2
    public var currentFiCO2: Float = 0f

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
    public var deviceName: String = ""

    // 设备序列号
    public var sSerialNumber: String = ""

    // 设备硬件版本
    public var sHardwareVersion: String = ""

    // 设备软件版本
    public var sSoftwareVersion: String = ""

    // 生产日期
    public var productionDate: String = ""

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
        if (scanning) { return }

        scanning = true
        bluetoothLeScanner?.startScan(leScanCallback)
    }

    // 停止扫描低功耗蓝牙设备
    @SuppressLint("MissingPermission")
    private fun stopScanBleDevices() {
        if (!scanning) { return }

        scanning = false
        bluetoothLeScanner?.stopScan(leScanCallback)
    }

    // 扫描经典蓝牙
    @SuppressLint("MissingPermission")
    private fun scanClassicDevices() {
        if (scanning) { return }

        scanning = true
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(activity, discoveryReceiver, filter, ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS)

        bluetoothAdapter?.startDiscovery()
    }

    // 停止扫描经典蓝牙
    @SuppressLint("MissingPermission")
    private fun stopScanClassicDevices() {
        if (!scanning) { return }

        scanning = false
        activity.unregisterReceiver(discoveryReceiver)
        bluetoothAdapter?.cancelDiscovery()
    }

    // 扫描流程集合
    @RequiresApi(Build.VERSION_CODES.S)
    public fun searchDevices(type: BluetoothType = BluetoothType.ALL) {
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
        println("wswTest 通过GATT进行连接")
        device.connectGatt(activity, false, gattCallback)
    }

    // 链接上CapnoEasy后，需要尽快发送初始化信息，否则会断开
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initCapnoEasyConection(gatt: BluetoothGatt) {
        connectedCapnoEasyGATT = gatt
        if (connectedCapnoEasyGATT == null) {
            return
        }

        val services = connectedCapnoEasyGATT!!.services
        val sortedServices = mutableListOf<BluetoothGattService>()
        val catchCharacteristic = mutableListOf<BluetoothGattCharacteristic>()

        // 根据 Service 的 UUID 进行排序
        for (uuid in sortedBLEServersUUID) {
            val service = services.find { it.uuid == uuid }
            if (service != null) {
                println("wswTest 注册了哪些服务 ${service.uuid}")
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
            println("wswTest 注册了反劫持")
            antiHijackCharacteristic = filterList[0]
            sendMessage(data = antiHijackData, type = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT, characteristic = antiHijackCharacteristic)
//            connectedCapnoEasyGATT!!.writeCharacteristic(filterList[0], antiHijackData, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
        }

        filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEAntihijackChaNofi.value }
        // 监听反劫持广播
        if (filterList.isNotEmpty()) {
            println("wswTest 注册了反劫持广播")
            antiHijackNotifyCharacteristic = filterList[0]
            connectedCapnoEasyGATT!!.setCharacteristicNotification(filterList[0], true)
        }

        filterList  = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLEReceiveDataCha.value }
        // 接受数据
        if (filterList.isNotEmpty()) {
            println("wswTest 注册了接受数据")
            receiveDataCharacteristic = filterList[0]
            connectedCapnoEasyGATT!!.setCharacteristicNotification(filterList[0], true)
        }

        filterList = catchCharacteristic.filter { it -> it.uuid == BLECharacteristicUUID.BLESendDataCha.value }
        // 发送数据
        if (filterList.isNotEmpty()) {
            println("wswTest 注册了发送时间")
            sendDataCharacteristic = filterList[0]
            // 读取单位前，必须要先停止设置
            sendStopContinuous()
            // 显示设置
            // TODO: 临时测试
            updateCO2UnitScale(CO2_UNIT.KPA, CO2_SCALE.LARGE)
            // 模块设置
            updateNoBreathAndGasCompensation(asphyxiationTime, oxygenCompensation)
            // 报警设置
            // TODO: 默认值来源还没有定下俩
            updateAlertRange(
                co2Low = 2f,
                co2Up = 10f,
                rrLow = 2,
                rrUp = 10
            )
            // 设置结束后，开始尝试接受数据
            sendContinuous()
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
            println("wswTEst sendSavedData【失败】 ")
            resetSendData()
            return
        }
        appendCKS()
        println("wswTest sendSavedData【成功】")
//        println("wswTest sendSavedData【成功】===> 特征值有么有 $sendDataCharacteristic")
        sendMessage()
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
    fun shutdown() {
        sendArray.add(SensorCommand.Reset.value.toByte())
        sendArray.add(0x01)
        sendSavedData()
    }

    /** 发送校零指令 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun correctZero() {
        sendArray.add(SensorCommand.Zero.value.toByte())
        sendArray.add(0x01)
        sendSavedData()
//        audioIns.stopAudio()
//        resetInstance()
    }

    /** 更新CO2单位/CO2Scale */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    fun updateCO2UnitScale(
        co2Unit: CO2_UNIT? = null,
        co2Scale: CO2_SCALE? = null,
    ) {
        println("wswTest 初始化单位相关， ${co2Unit} ${co2Scale}")
        if (co2Unit != null) {
            // 设置CO2单位
            sendArray.add(SensorCommand.Settings.value.toByte())
            sendArray.add(0x03)
            sendArray.add(ISBState84H.SetCO2Unit.value.toByte())

            if (co2Unit == CO2_UNIT.MMHG) {
                sendArray.add(0x00)
                // TODO: 这里需要监听如果其他配置变化了，这块需要重置
//            if (needReset)
//                etCo2Lower = 25
//                etCo2Upper = 50
//                etco2Min = 0
//                etco2Max = 99
//            }
            } else if (co2Unit == CO2_UNIT.KPA) {
                sendArray.add(0x01)
//            if isCO2UnitChange {
//                etCo2Lower = 3.3
//                etCo2Upper = 6.6
//                etco2Min = 0.0
//                etco2Max = 9.9
//            }
            } else if (co2Unit == CO2_UNIT.PERCENT) {
                sendArray.add(0x02)
//            if isCO2UnitChange {
//                etCo2Lower = 3.2
//                etCo2Upper = 6.5
//                etco2Min = 0.0
//                etco2Max = 9.9
//            }
            }
            sendSavedData()
        }

        if (co2Scale != null) {
            // 设置CO2Scale
            sendArray.add(SensorCommand.Expand.value.toByte())
            sendArray.add(0x03)
            sendArray.add(ISBStateF2H.CO2Scale.value.toByte())
            if (co2Scale == CO2_SCALE.MIDDLE) {
                sendArray.add(0x00)
            } else if (co2Scale == CO2_SCALE.SMALL) {
                sendArray.add(0x01)
            } else if (co2Scale == CO2_SCALE.LARGE) {
                sendArray.add(0x02)
            }
            println("wswTest 准备发送Scale数据")
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

    /** 设置模块参数: 窒息时间、氧气补偿 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun updateNoBreathAndGasCompensation(
        newAsphyxiationTime: Int = 0,
        newOxygenCompensation: Float = 0f
    ) {
        // 设置窒息时间
        sendArray.add(SensorCommand.Settings.value.toByte())
        sendArray.add(0x03.toByte())
        sendArray.add(ISBState84H.NoBreaths.value.toByte())
        sendArray.add(newAsphyxiationTime.toByte())
        sendSavedData()

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
//        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
//        pairedDevices?.forEach { device ->
//            val deviceName = device.name
//            val deviceHardwareAddress = device.address // MAC address
//        }
    }

    // 链接蓝牙设备
    @SuppressLint("MissingPermission")
    public fun connectDevice(device: BluetoothDevice?) {
        if (device == null) {
            return
        }
        println("wswTest 开始链接设备")
        // 链接后，需要将设备存到不同的属性中
        if (device.type == DEVICE_TYPE_LE) {
            connectBleDevice(device)
            connectedCapnoEasy.value = device
        } else if (device.type == DEVICE_TYPE_CLASSIC) {
            connectClassicDevice(device)
            connectedCapnoEasy.value = device
        }
    }

    fun getCMDDataArray(): List<UInt> {
        var getArray: List<UInt> = listOf();
        val command: UByte = receivedArray[0].toUByte()

        // 从接受到数据头部不停的向后移动，直到获取指令类型
        while (receivedArray.size > 0 && !supportCMDs.contains(command.toInt())) {
            receivedArray.removeAt(0)
        }

        // 数据长度为空，排除
        // 当长度为1的时候
        // 根据NBF获取所有数据: 第二位是NBF长度，但是整体需要加上额外两位(指令位、NBF位)
        // 只有CMD，连NBF都没有，属于异常响应
        if (receivedArray.size <= 0) {
            return getArray;
        }

        // 取此段指令所有数据: CMD + NBF + DB + CKS
        val endIndex = (receivedArray[1] + 2u).toInt()

        if (endIndex <= receivedArray.size) {
            getArray = receivedArray.subList(0, endIndex)
            receivedArray.subList(0, endIndex).clear() // 移除已经读取的数据
        }

        return getArray;
    }

    /** 从蓝牙返回数据中解析返回值 */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getSpecificValue(firstArray: ByteArray) {
        // 使用 ByteBuffer 直接访问内存，更安全高效
        val buffer = ByteBuffer.wrap(firstArray).order(ByteOrder.LITTLE_ENDIAN)
        val commandM = buffer.get().toInt()
        val NBFM = buffer.get().toInt() and 0xFF // 转换为无符号整数
        val cksM = firstArray.last().toInt()

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
            SensorCommand.CO2Waveform.value -> handleCO2Waveform(firstArray, NBFM) // 传递原始数组
            SensorCommand.Settings.value -> handleSettings(firstArray) // 传递原始数组
            SensorCommand.GetSoftwareRevision.value -> handleSofrWareVersion(firstArray) // 传递原始数组
            SensorCommand.Expand.value -> handleSystemExpand(firstArray) // 传递原始数组
            else -> println("未知指令 $commandM")
        }
    }

    /** 处理CO2波形数据 */
    private fun handleCO2Waveform(data: ByteArray, NBFM: Int) {
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)

        val DPIM = buffer.get(5)
        currentCO2 = (128 * buffer.get(3).toInt() and 0xFF + (buffer.get(4).toInt() and 0xFF) - 1000).toFloat() / 100

        if (NBFM > 4) {
            when (DPIM) {
                ISBState80H.CO2WorkStatus.value.toByte() -> {
                    handleCO2Status(data, NBFM)
                    isNeedZeroCorrect = (buffer.get(7) and 0x0C) == 0x0C.toByte()
                    isAdaptorInvalid = (buffer.get(6) and 0x02) == 0x02.toByte()
                    isAdaptorPolluted = currentCO2 < 0 && (buffer.get(6) and 0x01) == 1.toByte()
                }
                ISBState80H.ETCO2Value.value.toByte() -> {
                    currentETCO2 = (buffer.get(6).toInt() and 0xFF * 128 + (buffer.get(7).toInt() and 0xFF)).toFloat() / 10
                }
                ISBState80H.RRValue.value.toByte() -> {
                    currentRespiratoryRate = buffer.get(6).toUInt() and 0xFFu * 128u + (buffer.get(7).toUInt() and 0xFFu)
                }
                ISBState80H.FiCO2Value.value.toByte() -> {
                    currentFiCO2 = ((buffer.get(6).toInt() and 0xFF * 128 + (buffer.get(7).toInt() and 0xFF)).toFloat() / 10)
                }
                ISBState80H.DetectBreath.value.toByte() -> currentBreathe = true
                else -> println("CO2Waveform DPI 不匹配")
            }

            // TODO: 报警的逻辑，后续再加
//            if (!isAsphyxiation && isValidETCO2 && isValidRR && !isLowerEnergy && !isNeedZeroCorrect && !isAdaptorInvalid && !isAdaptorPolluted) {
//                audioIns.stopAudio()
//            } else if (isAsphyxiation || !isValidETCO2 || !isValidRR || isLowerEnergy) {
//                audioIns.playAlertAudio(AudioType.MiddleLevelAlert)
//            } else if (isNeedZeroCorrect || isAdaptorInvalid || isAdaptorPolluted) {
//                audioIns.playAlertAudio(AudioType.LowLevelAlert)
//            }
        }

        receivedCO2WavedData.add(
            DataPoint(
                index = receivedCO2WavedData.size,
                value = currentCO2,
            )
        )
        totalCO2WavedData.add(
            CO2WavePointData(
                co2 = currentCO2,
                RR = currentRespiratoryRate,
                ETCO2 = currentETCO2,
                FiCO2 = currentFiCO2,
                index = totalCO2WavedData.size
            )
        )

        if (receivedCO2WavedData.size > maxXPoints) {
            receivedCO2WavedData.removeAt(0)
        }
    }

    /** 处理校零，校零结果会在80h中获取，DPI=1 */
    private fun handleCO2Status(data: ByteArray, NBFM: Int) {
        if (NBFM <= 1) {
            return
        }

        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)

        val ZSBM = buffer.get(7).toInt() and 0x0C // 使用 & 0x0C 提取相关位

        when (ZSBM) {
            ZSBState.NOZeroning.value -> {
                if (isCorrectZero) {
                    // TODO: 这里加一个矫零的回调
//                    correctZeroCallback?.invoke()
                    isCorrectZero = false
                }
            }
            ZSBState.Resetting.value -> isCorrectZero = true
            ZSBState.NotReady.value,
            ZSBState.NotReady2.value -> isCorrectZero = false
            else -> isCorrectZero = false
        }

        isAsphyxiation = (buffer.get(6).toInt() and 0x40) == 0x40 // 检查是否置位
    }

    /** 处理设置： 序列号、硬件版本、设备名称 */
    private fun handleSettings(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN) // 假设小端序

        when (buffer.get(2).toInt() and 0xFF) { // 使用 Int 类型进行比较
            ISBState84H.GetSensorPartNumber.value -> {
                deviceName = ""
                for (i in 0..9) {
                    val uScalar = buffer.get(i + 3).toInt() and 0xFF
                    deviceName += uScalar.toChar().toString() // 使用 toChar() 转换为字符
                }
            }
            ISBState84H.GetSerialNumber.value -> {
                val DB1 = buffer.get(3).toDouble() * 2.0.pow(28)
                val DB2 = buffer.get(4).toDouble() * 2.0.pow(21)
                val DB3 = buffer.get(5).toDouble() * 2.0.pow(14)
                val DB4 = buffer.get(6).toDouble() * 2.0.pow(7)
                val DB5 = buffer.get(7).toDouble()
                val sNum = DB1 + DB2 + DB3 + DB4 + DB5
                sSerialNumber = String.format("%.0f", sNum)
            }
            ISBState84H.GetHardWareRevision.value -> {
                val DB1 = buffer.get(3).toInt() and 0xFF
                val DB2 = buffer.get(4).toInt() and 0xFF
                val DB3 = buffer.get(5).toInt() and 0xFF
                sHardwareVersion = "${DB1.toChar()}.${DB2.toChar()}.${DB3.toChar()}"
            }
            else -> println("模块参数设置 未知ISB")
        }
    }

    /** 获取生产日期、软件版本 */
    private fun handleSofrWareVersion(data: ByteArray) {
        val buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)
        val NBFM = buffer.get(1).toInt() and 0xFF
        var rawSoftWareVersion = ""

        for (i in 0 until NBFM) {
            val uScalar = buffer.get(i + 3).toInt() and 0xFF
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

                sSoftwareVersion = rawSoftWareVersion.replace(matcher.group(), "").trim()
                sSoftwareVersion = sSoftwareVersion.replace(Regex("[\\p{Cc}]"), "") // Remove control characters
                sSoftwareVersion = sSoftwareVersion.replace(Regex("-+$"), "") // Remove trailing hyphens

                val formatter = SimpleDateFormat("yy MM/dd/yy HH:mm")
                try {
                    val date = formatter.parse(fullTimeString)
                    formatter.applyPattern("yyyy/MM/dd HH:mm:ss")
                    productionDate = formatter.format(date)
                    println("Formatted Time: $productionDate")
                    // TODO: 这里需要加待办
//                    getSettingInfoCallback?.invoke(formattedDateString, ISBState.CMD_CAH(ISBState.GetProductionDate)) // 需要定义 ISBState.CMD_CAH

                } catch (e: Exception) {
                    println("Failed to parse date: ${e.message}")
                }

                // TODO: 需要加待办
//                getSettingInfoCallback?.invoke(remainingText, ISBState.CMD_CAH(ISBState.GetModuleName)) // 需要定义 ISBState.CMD_CAH
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

        when (buffer.get(2).toInt() and 0xFF) {
            ISBStateF2H.CO2Scale.value -> {
                sendContinuous()
            }
            ISBStateF2H.EnergyStatus.value -> {
                isLowerEnergy = (buffer.get(6).toInt() and 0xFF) == 1
                println("查询是否低电量的DB ${buffer.get(3)}-${buffer.get(4)}-${buffer.get(5)}-${buffer.get(6)}")
            }
            else -> println("扩展指令未知场景")
        }
    }

    // 从已链接设备读取数据
    public fun readDataFromDevice() {}
}