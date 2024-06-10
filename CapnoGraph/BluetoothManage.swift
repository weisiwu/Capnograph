import CoreBluetooth
import Combine

class BluetoothManager: NSObject, ObservableObject, CBCentralManagerDelegate, CBPeripheralDelegate {
    private var centralManager: CBCentralManager!
    @Published var discoveredPeripherals = [CBPeripheral]() // 周围设备列表，创建了一个指定类型的空列表
    @Published var connectedPeripheral: CBPeripheral? // 以链接设备
    @Published var toastMessage: String? = nil // 通知消息
    var isScanning: Bool = false
    
    override init() {
        super.init()
        // 中央设备管理器
        centralManager = CBCentralManager(delegate: self, queue: .main)
    }
    
    // CBCentralManagerDelegate 的方法， 在蓝牙状态发生变化的时候，开始扫描外围设备
    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        // 判断蓝牙状态
        // unknown：正在初始化
        // resetting：蓝牙硬件暂时不可用
        // unsupported：设备不支持蓝牙功能
        // unauthorized：应用未被授权使用蓝牙功能
        // poweredOff：蓝牙已关闭
        // poweredOn：蓝牙已打开并可用
        switch central.state {
//        case .unknown:
//        case .resetting:
//        case .unsupported:
//        case .unauthorized:
//        case .poweredOff:
        case .poweredOn:
            // 正在扫描中，先停止扫描
            if isScanning {
                stopScanning()
            }
            startScanning()
        @unknown default:
            print("未知情况")
        }
    }
    
    /**------  中央设备事件回调 ------*/
    // 开始扫描设备
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        // 查看是否在已发现设备列表，防止重复
        if !discoveredPeripherals.contains(where: { $0.identifier == peripheral.identifier }) {
            discoveredPeripherals.append(peripheral)
        }
    }
    
    // 连接成功后显示 Toast
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        DispatchQueue.main.async {
            self.toastMessage = "链接成功"
        }
        // 给外设添加事件管理函数
        peripheral.delegate = self
        peripheral.discoverServices(nil)
    }
    
    // 链接失败
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        DispatchQueue.main.async {
            self.toastMessage = "链接成功"
        }
    }

    // 设备断开链接后
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        if connectedPeripheral == peripheral {
            connectedPeripheral = nil
        }
    }
    
    // TODO: 如何手动触发链接设备？
    // centralManager.stopScan()
    // centralManager.connect(peripheral, options: nil)

    /**------  外围设备事件回调 ------*/
    // 读取服务
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        // TODO: 这里需要细化，肯定不是所有的service我们都需要
        if let services = peripheral.services {
            for service in services {
                peripheral.discoverCharacteristics(nil, for: service)
            }
        }
    }
    
    // 读取特征值
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        if let characteristics = service.characteristics {
            for characteristic in characteristics {
                peripheral.readValue(for: characteristic)
                    // peripheral.setNotifyValue(true, for: characteristic)
            }
        }
    }
    
    // 特征值更新
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        if let value = characteristic.value {
            print("Value for \(characteristic.uuid): \(value)")
        }
    }

    /**------  工具方法 ------*/
    func startScanning() {
        guard centralManager.state == .poweredOn else { return }
        discoveredPeripherals.removeAll()
        centralManager.scanForPeripherals(withServices: nil, options: nil)
        isScanning = true
    }
    
    func stopScanning() {
        centralManager.stopScan()
        isScanning = false
    }
    
    // 链接蓝牙外设
    func connect(to peripheral: CBPeripheral) {
        centralManager.stopScan()
        connectedPeripheral = peripheral
        connectedPeripheral?.delegate = self
        centralManager.connect(peripheral, options: nil)
    }
    
    func toast() {
        // 连接成功后显示 Toast
        DispatchQueue.main.async {
            self.toastMessage = "链接成功"
        }
    }
}
