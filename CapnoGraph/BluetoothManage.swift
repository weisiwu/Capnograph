import CoreBluetooth
import Combine
import Foundation

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

// TODO: 需要先加上AntiHijack才能发送命令
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

//蓝牙特征UUID
enum BLECharacteristicUUID: UInt16 {
    case BLESendDataCha = 0xFFE9
    case BLEReceiveDataCha = 0xFFE4
    case BLERenameCha = 0xFF91
    case BLEBaudCha = 0xFF93
    case BLEAntihijackChaNofi = 0xFFC2
    case BLEAntihijackCha = 0xFFC1
};

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
    var sendDataServer: CBService?
    var sendDataCharacteristic: CBCharacteristic?
    var isConnectToDevice: Bool = false
    
    override init() {
        super.init()
        // 中央设备管理器
        centralManager = CBCentralManager(delegate: self, queue: .main)
    }
    
    /**------  发送指令，相关函数 ------*/

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
    func sendStopContinuous() {
        sendArray.append(SensorCommand.StopContinuous.rawValue)
        sendArray.append(0x01)
        appendCKS()
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            let data = convertToData(from: sendArray)
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            print("[sendStopContinuous]发起链接请求")
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
    // 读取服务
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        if let services = peripheral.services {
            for service in services {
                if isAvalidServer(sUuid: service.uuid.hexIntValue) {
                    peripheral.discoverCharacteristics(nil, for: service)
                    if service.uuid.hexIntValue == BLEServerUUID.BLESendDataSer.rawValue {
                        sendDataServer = service
                    }
                }
            }
        }
    }
    
    // 读取特征值
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        if let characteristics = service.characteristics {
            for characteristic in characteristics {
                if isAvalidCharacteristic(cUuid: characteristic.uuid.hexIntValue) {
//                    peripheral.readValue(for: characteristic)
                    if characteristic.uuid.hexIntValue == BLECharacteristicUUID.BLESendDataCha.rawValue {
                        sendDataCharacteristic = characteristic
                        // 读取到特征值后，开始发送命令
                        sendStopContinuous()
                    }
                }
            }
        }
    }
    
    // 特征值更新
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        if let value = characteristic.value {
            print("特征值更新Value for \(characteristic.uuid): \(String(data: value, encoding: .utf8))")
        }
    }
    
    // 外设返回响应（针对写特征值并等待返回的情况）
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        if error != nil {
            print("[sendStopContinuous]发起链接请求失败 => \(error)")
            return
        }
        if let value = characteristic.value {
            print("返回响应 \(characteristic.uuid): \(String(data: value, encoding: .utf8))")
        }
    }
    
    /**------  监听蓝牙状态 ------*/
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        print("监听蓝牙状态")
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
