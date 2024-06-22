import CoreBluetooth
import Combine
import Foundation

let antiHijackStr = "301001301001"
let antiHijackData = antiHijackStr.data(using: .utf8)!

// 对uuid做扩展
extension CBUUID {
    var hexString: String {
        let uuidString = self.uuidString.replacingOccurrences(of: "-", with: "")
        return uuidString
    }
    
    var hexIntValue: UInt16? {
        return UInt16(self.hexString, radix: 16)
    }
}

// TODO: QT中的getFirstArray逻辑不了解
//指令集合
enum SensorCommand: UInt8 {
    case CO2Waveform = 0x80 // 接受数据
    case Zero = 0x82 // 校零
    case Expand = 0xF2 // TODO: 后续待确认具体作用
    case Settings = 0x84 // 更改读取设置
    case StopContinuous = 0xC9 // 停止连读模式
    case NACKError = 0xC8 // 非应答错误
    case ResetNoBreaths = 0xCC // 清除窒息状态
    case Reset = 0xF8 // 复位指令
}

//蓝牙服务UUID
enum BLEServerUUID: UInt16 {
    case BLESendDataSer = 0xFFE5 // 65509
    case BLEReceiveDataSer = 0xFFE0 // 65504
    case BLEModuleParamsSer = 0xFF90 // 65424
    case BLEAntihijackSer = 0xFFC0 // 65472 反蓝牙劫持
}

//蓝牙服务顺序排列
let BLEServerOrderedUUID: [UInt16] = [
    BLEServerUUID.BLEAntihijackSer.rawValue,
    BLEServerUUID.BLEReceiveDataSer.rawValue,
    BLEServerUUID.BLESendDataSer.rawValue,
    BLEServerUUID.BLEModuleParamsSer.rawValue,
]

//蓝牙特征UUID
enum BLECharacteristicUUID: UInt16 {
    case BLESendDataCha = 0xFFE9
    case BLEReceiveDataCha = 0xFFE4
    case BLERenameCha = 0xFF91
    case BLEBaudCha = 0xFF93
    case BLEAntihijackChaNofi = 0xFFC2
    case BLEAntihijackCha = 0xFFC1
};

//顺序排列蓝牙特征值UUID
let BLECharacteristicOrderedUUID: [UInt16] = [
    BLECharacteristicUUID.BLEAntihijackCha.rawValue,
    BLECharacteristicUUID.BLEAntihijackChaNofi.rawValue,
    BLECharacteristicUUID.BLEReceiveDataCha.rawValue,
    BLECharacteristicUUID.BLESendDataCha.rawValue,
    BLECharacteristicUUID.BLERenameCha.rawValue,
    BLECharacteristicUUID.BLEBaudCha.rawValue
]

//蓝牙描述符UUID
enum BLEDescriptorUUID: UInt16 {
    case CCCDDescriptor = 0x2902
}

//判断是否为App中可订阅的服务
func isAvalidServer(sUuid: UInt16?) -> Bool {
    switch sUuid {
    case BLEServerUUID.BLESendDataSer.rawValue,
        BLEServerUUID.BLEReceiveDataSer.rawValue,
        BLEServerUUID.BLEModuleParamsSer.rawValue,
        BLEServerUUID.BLEAntihijackSer.rawValue:
        return true
    default:
        return false
    }
    return false
}

//判断服务特征值是否为App中可订阅
func isAvalidCharacteristic(cUuid: UInt16?) -> Bool {
    switch cUuid {
    case BLECharacteristicUUID.BLESendDataCha.rawValue,
        BLECharacteristicUUID.BLEReceiveDataCha.rawValue,
        BLECharacteristicUUID.BLERenameCha.rawValue,
        BLECharacteristicUUID.BLEBaudCha.rawValue,
        BLECharacteristicUUID.BLEAntihijackChaNofi.rawValue,
        BLECharacteristicUUID.BLEAntihijackCha.rawValue:
        return true
    default:
        return false
    }
    return false
}

class BluetoothManager: NSObject, ObservableObject, CBCentralManagerDelegate, CBPeripheralDelegate {
    private var centralManager: CBCentralManager!
    @Published var discoveredPeripherals = [CBPeripheral]() // 周围设备列表，创建了一个指定类型的空列表
    @Published var connectedPeripheral: CBPeripheral? // 以链接设备
    @Published var toastMessage: String? = nil // 通知消息
    var isScanning: Bool = false
    var startScanningCallback: (() -> Void)?
    var connectedCallback: (() -> Void)?
    var sendArray: [UInt8] = []
    var receivedArray: [UInt8] = []
    var sendDataService: CBService?
    var sendDataCharacteristic: CBCharacteristic?
    var receiveDataService: CBService?
    var receiveDataCharacteristic: CBCharacteristic?
    var moduleParamsService: CBService?
    var moduleParamsCharacteristic: CBCharacteristic?
    var antiHijackService: CBService?
    var antiHijackCharacteristic: CBCharacteristic?
    // 这三个特征值没有明确属于哪个服务，所以可能为空
    var baudCharacteristic: CBCharacteristic?
    var renameCharacteristic: CBCharacteristic?
    var antiHijackNotifyCharacteristic: CBCharacteristic?
    var CCCDDescriptor: CBDescriptor?
    var isConnectToDevice: Bool = false
    
    override init() {
        super.init()
        // 中央设备管理器
        centralManager = CBCentralManager(delegate: self, queue: .main)
    }
    
    /**------  发送指令，相关函数 ------*/
    
    // 对指定UUID的服务进行注册
    func registerService(peripheral: CBPeripheral?, services: [CBService]?) {
        guard let _peripheral = peripheral else {
            return
        }
        guard var _services = services else {
            return
        }
        
        // 将所有特征值按照指定顺序进行排列
        _services = _services
            .filter { ser -> Bool in
                guard let serHex = ser.uuid.hexIntValue else {
                    return false
                }
                return BLEServerOrderedUUID.contains(serHex)
            }
            .sorted { (ser1, ser2) -> Bool in
                guard let ser1Hex = ser1.uuid.hexIntValue,
                      let ser2Hex = ser2.uuid.hexIntValue  else {
                    return false
                }
                guard let index1 = BLEServerOrderedUUID.firstIndex(of: ser1Hex),
                      let index2 = BLEServerOrderedUUID.firstIndex(of: ser2Hex) else {
                    return false
                }
                return index1 < index2
            }
        
        for _service in _services {
            switch _service.uuid.hexIntValue {
            case BLEServerUUID.BLESendDataSer.rawValue:
                sendDataService = _service
                _peripheral.discoverCharacteristics(nil, for: _service)
            case BLEServerUUID.BLEReceiveDataSer.rawValue:
                // TODO: 临时注释代码
                // connect(m_service, &QLowEnergyService::stateChanged, this, &DeviceHandler::serviceStateChanged);
                // connect(m_service, &QLowEnergyService::characteristicChanged, this, &DeviceHandler::updateCO2Value);
                // connect(m_service, &QLowEnergyService::descriptorWritten, this, &DeviceHandler::confirmedDescriptorWrite);
                receiveDataService = _service
                _peripheral.discoverCharacteristics(nil, for: _service)
            case BLEServerUUID.BLEModuleParamsSer.rawValue:
                moduleParamsService = _service
                _peripheral.discoverCharacteristics(nil, for: _service)
            case BLEServerUUID.BLEAntihijackSer.rawValue:
                antiHijackService = _service
                _peripheral.discoverCharacteristics(nil, for: _service)
            default:
                print("not match service \(_service.uuid.hexIntValue)")
            }
        }
    }
    
    // 对指定UUID的特征值进行注册
    func registerCharacteristic(peripheral: CBPeripheral?, characteristics: [CBCharacteristic]?) {
        guard let _peripheral = peripheral else {
            return
        }
        guard var _characteristics = characteristics else {
            return
        }
        
        // 将所有特征值按照指定顺序进行排列
        _characteristics = _characteristics
            .filter { (char) -> Bool in
                guard let charHex = char.uuid.hexIntValue else {
                    return false
                }
                return BLECharacteristicOrderedUUID.contains(charHex)
            }
            .sorted { (char1, char2) -> Bool in
                guard let char1Hex = char1.uuid.hexIntValue,
                      let char2Hex = char2.uuid.hexIntValue  else {
                    return false
                }
                guard let index1 = BLECharacteristicOrderedUUID.firstIndex(of: char1Hex),
                      let index2 = BLECharacteristicOrderedUUID.firstIndex(of: char2Hex) else {
                    return false
                }
                return index1 < index2
            }
        
        // 排序后，依次处理
        for _characteristic in _characteristics {
            // 客户端如何处理服务器特征值更新(0x2902)
            // 启用通知: QByteArray::fromHex("0100")
            // 禁用通知: QByteArray::fromHex("0000")
            // 这里主要是接受数据、反蓝牙劫持服务(service)开启了接受通知
            // 在swift的CB中，和QT的QLowEnergyService设计不同，有独立的方法setNotifyValue来完成对描述符的订阅
            switch _characteristic.uuid.hexIntValue {
            case BLECharacteristicUUID.BLEReceiveDataCha.rawValue:
                receiveDataCharacteristic = _characteristic
                _peripheral.setNotifyValue(true, for: _characteristic)
            case BLECharacteristicUUID.BLESendDataCha.rawValue:
                sendDataCharacteristic = _characteristic
            case BLECharacteristicUUID.BLERenameCha.rawValue:
                renameCharacteristic = _characteristic
            case BLECharacteristicUUID.BLEBaudCha.rawValue:
                baudCharacteristic = _characteristic
            case BLECharacteristicUUID.BLEAntihijackChaNofi.rawValue:
                antiHijackNotifyCharacteristic = _characteristic
                // 监听反劫持广播
                peripheral?.setNotifyValue(true, for: _characteristic)
            case BLECharacteristicUUID.BLEAntihijackCha.rawValue:
                antiHijackCharacteristic = _characteristic
                // 写入反劫持串
                peripheral?.writeValue(antiHijackData, for: _characteristic, type: .withResponse)
            default:
                print("not match characteristic \(_characteristic.uuid.hexIntValue)")
            }
        }
    }
    
    func receivePeripheralData(peripheral: CBPeripheral, characteristic: CBCharacteristic) {
        guard let charValue = characteristic.value, let charHex = characteristic.uuid.hexIntValue else {
            return
        }
        if charHex == BLECharacteristicUUID.BLEReceiveDataCha.rawValue {
            receivedArray.append(contentsOf: charValue)
            print("接受到外设数据(\(characteristic.uuid))=>\(receivedArray)")

//            if receivedArray.count >= 20 {
//                QByteArray firstArray=getFirstArray();
//                getSpecificValue(firstArray);
//            }
        }
        
    }
    
    func appendCKS() {
        var cks: UInt8 = 0;
        for i in 0..<sendArray.count {
            cks += sendArray[i];
        }
        cks=((~cks+1) & 0x7f);
        sendArray.append(cks);
    }

    func convertToData(from: [UInt8]) -> Data {
        return Data(from)
    }

    // 发送链接请求
    func sendContinuous() {
        sendArray.append(SensorCommand.CO2Waveform.rawValue)
        sendArray.append(0x02)
        sendArray.append(0x00)
        appendCKS()

        let data = convertToData(from: sendArray)
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            sendArray = []
            print("[sendContinuous]发起链接请求")
        }
    }

    // 发送链接请求
    func sendStopContinuous() {
        sendArray.append(SensorCommand.StopContinuous.rawValue)
        sendArray.append(0x01)
        appendCKS()

        let data = convertToData(from: sendArray)
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            sendArray = []
            print("[sendStopContinuous]终止链接请求")
        }
    }
    
    func resetSendData() {
        sendArray = []
    }
    
    /**------  中央设备事件回调 ------*/
    // 开始扫描设备
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        // 查看是否在已发现设备列表，防止重复
        if !discoveredPeripherals.contains(where: { $0.identifier == peripheral.identifier }) {
            discoveredPeripherals.append(peripheral)
        }
        startScanningCallback?()
    }
    
    // 连接成功后显示 Toast
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        connectedCallback?()
        // 给外设添加事件管理函数
        peripheral.delegate = self
        peripheral.discoverServices(nil)
    }
    
    // 链接失败
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("链接失败")
    }

    // 设备断开链接后
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        if connectedPeripheral == peripheral {
            connectedPeripheral = nil
        }
    }

    /**------  外围设备事件回调 ------*/
    // 扫描设备服务
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        registerService(peripheral: peripheral, services: peripheral.services)
    }
    
    // 扫描服务特征值
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        registerCharacteristic(peripheral: peripheral, characteristics: service.characteristics)
    }
    
    // 特征值更新
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        receivePeripheralData(peripheral: peripheral, characteristic: characteristic)
    }
    
    // 外设返回响应（针对写特征值并等待返回的情况）
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        print("接受到\(characteristic.uuid)返回数据 \(characteristic.value)")
        if error != nil {
            print("接受特征\(characteristic.uuid)返回值异常=> \(error)")
            return
        }
        if let value = characteristic.value {
            print("接受特征\(characteristic.uuid)返回值正常=> \(String(data: value, encoding: .utf8))")
        }
    }
    
    // 监听订阅状态是否成功切换
    func peripheral(_ peripheral: CBPeripheral, didUpdateNotificationStateFor characteristic: CBCharacteristic, error: Error?) {
        if let error = error {
            print("设置订阅状态失败: \(characteristic.uuid) \(error.localizedDescription)")
        } else {
            print("设置订阅状态成功: \(characteristic.uuid)")
            // receiveData成功后，发送链接请求
            if characteristic.uuid.hexIntValue == BLECharacteristicUUID.BLEReceiveDataCha.rawValue {
                sendStopContinuous()
                sendContinuous()
            }
        }
    }
    
    /**------  监听蓝牙状态 ------*/
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        switch central.state {
        case .poweredOn:
            print("Bluetooth is powered on and ready to use.")
            // 在此处启动扫描或恢复连接
            centralManager.scanForPeripherals(withServices: nil, options: nil)
        case .poweredOff:
            print("Bluetooth is currently powered off.")
        case .resetting:
            print("Bluetooth is resetting.")
        case .unauthorized:
            print("Bluetooth is not authorized for this app.")
        case .unsupported:
            print("Bluetooth is not supported on this device.")
        case .unknown:
            print("Bluetooth state is unknown.")
        @unknown default:
            print("A previously unknown Bluetooth state occurred.")
        }

    }

    /**------  工具方法 ------*/
    func startScanning(callback: (() -> Void)?) -> Bool {
        guard centralManager.state == .poweredOn else { return false }
        if isScanning {
            return false
        }
        // 检查状态
        guard let isPass = checkBluetoothStatus(centralManager), !isPass else {
            discoveredPeripherals.removeAll()
            centralManager.scanForPeripherals(withServices: nil, options: nil)
            isScanning = true
            startScanningCallback = callback
            return true
        }
        return false
    }
    
    func stopScanning() {
        centralManager.stopScan()
        isScanning = false
    }
    
    // 链接蓝牙外设
    func connect(to peripheral: CBPeripheral?, callback: (() -> Void)?) {
        if let peripheral {
            centralManager.stopScan()
            connectedPeripheral = peripheral
            connectedPeripheral?.delegate = self
            centralManager.connect(peripheral, options: nil)
            connectedCallback = callback
        }
    }
    
    // 判断蓝牙状态
    // unknown：正在初始化
    // resetting：蓝牙硬件暂时不可用
    // unsupported：设备不支持蓝牙功能
    // unauthorized：应用未被授权使用蓝牙功能
    // poweredOff：蓝牙已关闭
    // poweredOn：蓝牙已打开并可用
    func checkBluetoothStatus(_ central: CBCentralManager) -> Bool? {
        switch central.state {
        case .unknown, .resetting, .unsupported, .unauthorized, .poweredOff:
            return false
        case .poweredOn:
            return true
        @unknown default:
            return nil
        }
    }
}
