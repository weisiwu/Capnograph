import CoreBluetooth

//TODO:
//1.NSObject了解
//2.ObservableObject了解
//3.CBCentralManagerDelegate了解
class BluetoothManager: NSObject, ObservableObject, CBCentralManagerDelegate {
    private var centralManager: CBCentralManager!
    @Published var discoveredPeripherals = [CBPeripheral]()
    
    override init() {
        super.init()
        centralManager = CBCentralManager(delegate: self, queue: .main)
    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if central.state == .poweredOn {
            // 开始搜索外围设备
            centralManager.scanForPeripherals(withServices: nil, options: nil)
        } else {
            // 处理其他状态
        }
    }
    
    // 发现外围设备的回调
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String: Any], rssi RSSI: NSNumber) {
        if !discoveredPeripherals.contains(peripheral) {
            discoveredPeripherals.append(peripheral)
        }
    }
    
    // 添加用来开始和停止扫描的方法
    func startScanning() {
        guard centralManager.state == .poweredOn else { return }
        centralManager.scanForPeripherals(withServices: nil, options: nil)
    }

    func stopScanning() {
        centralManager.stopScan()
    }
}
