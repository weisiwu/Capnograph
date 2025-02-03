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
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.UUID
import javax.inject.Inject

//已配对设备标志
val savedPeripheralIdentifierKey = "SavedPeripheralIdentifier"

// 反劫持传
val antiHijackStr = "301001301001"
val antiHijackData = antiHijackStr.toByteArray(Charsets.UTF_8)


// 统一设备模型
enum class BluetoothType {
    BLE,
    CLASSIC,
    ALL
}

// 接收到的数据
data class DataPoint(val index: Int, val value: Double)

// 记录波形数据用格式
data class CO2WavePointData(
    val co2: Float = 0f,
    val RR: Int = 0,
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
const val REQUEST_ENABLE_DEVICE_DISCOVERABLE = 5

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
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    // 设备已断开连接
                    println("wswTest 设备断开链接")
                }
                else -> {
                    // 其他状态
                    Log.d("GATT", "GATT state changed to $newState")
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 设备已连接
                initCapnoEasyConection(gatt)
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
    
    var moduleParamsService: BluetoothGattService? = null
    
    var moduleParamsCharacteristic: BluetoothGattCharacteristic? = null
    
    var antiHijackService: BluetoothGattService? = null
    
    var antiHijackCharacteristic: BluetoothGattCharacteristic? = null

    var antiHijackNotifyService: BluetoothGattService? = null

    var antiHijackNotifyCharacteristic: BluetoothGattCharacteristic? = null

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
    public val receivedArray = mutableListOf<Int>()

    // 准备发送给设备的命令数据 - 发送后清空
    public val sendArray = mutableListOf<Byte>()

    // 当前CO2值
    public val currentCO2 = mutableListOf<Float>(0f)

    // 本次记录的所有波形数据
    public var totalCO2WavedData = mutableListOf<CO2WavePointData>()

    // 当前ETCO2值
    public var currentETCO2: Float = 0f

    // 当前呼吸率
    public  var currentRespiratoryRate: Int = 0

    // 当前FiCO2
    public var currentFiCO2: Int = 0

    // 当前是否还在呼吸
    public var currentBreathe: Boolean = false

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
        device.connectGatt(activity, false, gattCallback)
    }

    // 链接上CapnoEasy后，需要尽快发送初始化信息，否则会断开
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initCapnoEasyConection(gatt: BluetoothGatt) {
        connectedCapnoEasyGATT = gatt
        val services = gatt.services
        var isCatch = false
        val sortedServices = mutableListOf<BluetoothGattService>()

        // 根据 Service 的 UUID 进行排序
        for (uuid in sortedBLEServersUUID) {
            val service = services.find { it.uuid == uuid }
            if (service != null) {
                sortedServices.add(service)
            }
        }

        for (service in sortedServices) {
            when(service.uuid) {
                // 反劫持
                BLEServersUUID.BLEAntihijackSer.value -> {
                    antiHijackService = service
                    isCatch = true
                }
                // 接受数据
                BLEServersUUID.BLEReceiveDataSer.value -> {
                    receiveDataService = service
                    isCatch = true
                }
                // 发送数据
                BLEServersUUID.BLESendDataSer.value -> {
                    sendDataService = service
                    isCatch = true
                }
            }

            // 有需要订阅的服务，寻找服务的特征值
            if (isCatch) {
                val characteristics = service.characteristics

                for (characteristic in characteristics) {
                    when(characteristic.uuid) {
                        // 反劫持
                        BLECharacteristicUUID.BLEAntihijackCha.value -> {
                            gatt.writeCharacteristic(characteristic, antiHijackData, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                            antiHijackCharacteristic = characteristic
                        }
                        // 监听反劫持广播
                        BLECharacteristicUUID.BLEAntihijackChaNofi.value -> {
                            gatt.setCharacteristicNotification(characteristic, true)
                            antiHijackNotifyCharacteristic = characteristic
                        }
                        // 接受数据
                        BLECharacteristicUUID.BLEReceiveDataCha.value -> {
                            gatt.setCharacteristicNotification(characteristic, true)
                            receiveDataCharacteristic = characteristic
                        }
                        // 发送数据
                        BLECharacteristicUUID.BLESendDataCha.value -> {
//                            // 显示设置
//                            updateCO2Unit(cb: silent)
//                            // 模块设置
//                            updateNoBreathAndGasCompensation(
//                                newAsphyxiationTime: asphyxiationTime,
//                                newOxygenCompensation: oxygenCompensation
//                            )
//                            // 报警设置
//                            updateAlertRange(
//                                co2Low: etCo2Lower,
//                                co2Up: etCo2Upper,
//                                rrLow: rrLower,
//                                rrUp: rrUpper
//                            )
//
                            sendDataCharacteristic = characteristic
                            // 设置结束后，开始尝试接受数据
                            sendContinuous(gatt)
                        }
                    }
                }
            }
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
    @SuppressLint("MissingPermission")
    private fun sendContinuous(gatt: BluetoothGatt) {
        sendArray.add(SensorCommand.CO2Waveform.value.toByte())
        sendArray.add(0x02)
        sendArray.add(0x00)
        appendCKS()
        // 写入特征值
        if (sendDataCharacteristic != null) {
            sendDataCharacteristic!!.value = sendArray.toByteArray()
            gatt.writeCharacteristic(sendDataCharacteristic)
            resetSendData()
        }
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
        // 链接后，需要将设备存到不同的属性中
        if (device.type == DEVICE_TYPE_LE) {
            connectBleDevice(device)
            connectedCapnoEasy.value = device
        } else if (device.type == DEVICE_TYPE_CLASSIC) {
            connectClassicDevice(device)
            connectedCapnoEasy.value = device
        }
    }

    // 从已链接设备读取数据
    public fun readDataFromDevice() {}
}