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

//指令集合
enum SensorCommand: UInt8 {
    case CO2Waveform = 0x80 // 接受数据
    case Zero = 0x82 // 校零
    case Expand = 0xF2 // 获取系统扩展设置，如硬件、软件版本
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

// 校零状态
enum ZSBState: Int {
    case Start = 0
    case NotReady
    case Resetting
    case DetectBreathing
}

// 模块设置
enum ISBState: Int {
    case NoUse = 0 // 无效的参数设置
    case AirPressure = 1 // 大气压
    case Temperature = 4 // 气体温度
    case ETCO2Period = 5 // ETCO2 时间周期
    case NoBreaths = 6 // 窒息时间
    case UnVerified = 7 // TODO: 待确认
    case Sleep = 8 // 休眠模式
    case ZeroPointGasType = 9 // 零点气类型
    case GasCompensation = 11 // 读取/设置 气体补偿
    case Stop = 27 // 停止采样气泵
}

// 系统扩展设置
enum ExpandState: Int {
    case HardFirmVersion = 2 // 获取固件、硬件版本
    case DateSerialNumber = 4 // 获取生产日期、序列号
    // TODO: 这两个都是没有确定的
    // case NoUse: Int = 42 // 
    // case NoUse: Int = 44 // 
}

class BluetoothManager: NSObject, ObservableObject, CBCentralManagerDelegate, CBPeripheralDelegate {
    private var centralManager: CBCentralManager!
    @Published var discoveredPeripherals = [CBPeripheral]() // 周围设备列表，创建了一个指定类型的空列表
    @Published var connectedPeripheral: CBPeripheral? // 已链接设备
    @Published var toastMessage: String? = nil // 通知消息
    @Published var receivedCO2WavedData: [DataPoint] = [DataPoint(value: 0)]
    var isScanning: Bool = false
    var startScanningCallback: (() -> Void)?
    var connectedCallback: (() -> Void)?
    var sendArray: [UInt8] = []
    var receivedArray: [UInt8] = []
    var currentCO2: Float = 0
    @Published var ETCO2: Float = 0
    @Published var RespiratoryRate: Int = 0
    var FiCO2: Int = 0
    var Breathe: Bool = false
    var canZero: Bool = false
    var barometricPressure: Int = 0
    var NoBreaths: Bool = false
    var O2Compensation: Int = 0
    var sFirmwareVersion: String = ""
    var sHardwareVersion: String = ""
    var sProductionDate: String = ""
    var sSerialNumber: String = ""
    // 扫描的设备、服务、特征
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
        
        // 处理接受波形数据
        if charHex == BLECharacteristicUUID.BLEReceiveDataCha.rawValue {
            receivedArray.append(contentsOf: charValue)
//            print("接受到外设数据(\(characteristic.uuid))=>\(receivedArray)")

            // TODO: 明确为什么不能多余20
            if receivedArray.count >= 20 {
                let firstArray = getFirstArray()
                getSpecificValue(firstArray: firstArray);
            }
        }
        
    }

    // 监听特征值状态变化
    func characteristicStateUpdate (characteristic: CBCharacteristic) {
        // 订阅receiveData成功后，发送链接请求
        if characteristic.uuid.hexIntValue == BLECharacteristicUUID.BLEReceiveDataCha.rawValue {
            sendStopContinuous() // 停止历史可能存在数据串
            sendContinuous() // 重新开始请求新的数据串
        }
    }
    
    func calculateCKS(arr: [UInt8]) -> UInt8 {
        var cks: Int = 0;
        for i in 0..<arr.count {
            cks += Int(arr[i]);
        }
        cks=((~cks+1) & 0x7f);
        return UInt8(cks)
    }

    func appendCKS() {
        let cks = calculateCKS(arr: sendArray)
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
            resetSendData()
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
            resetSendData()
            print("[sendStopContinuous]终止链接请求")
        }
    }
    
    func getFirstArray() -> [UInt8] {
        var getArray: [UInt8] = [];
        let command: UInt8 = receivedArray[0]
        
        // 从接受到数据头部不停的向后移动，直到获取指令类型
        while receivedArray.count > 0
            && command != SensorCommand.CO2Waveform.rawValue
            && command != SensorCommand.Zero.rawValue
            && command != SensorCommand.Settings.rawValue
            && command != SensorCommand.Expand.rawValue
            && command != SensorCommand.StopContinuous.rawValue
            && command != SensorCommand.NACKError.rawValue
            && command != SensorCommand.ResetNoBreaths.rawValue {
            receivedArray.removeSubrange(0..<1);
        }

        // 数据长度为空，排除
        if receivedArray.count == 0 {
            return getArray;
        }

        // 根据NBF获取所有数据: 第二位是NBF长度，但是整体需要加上额外两位(指令位、NBF位)
        let endIndex = Int(receivedArray[1]) + 2
        getArray = Array(receivedArray.prefix(endIndex))
        receivedArray.removeSubrange(0..<endIndex) // 移除已经读取的数据
//        print("【接受到的蓝牙数据】本次读取的指令=>\(receivedArray[0]) 本次读取的数据=>\(getArray) 本次长度=>\(endIndex)")

        return getArray;
    }
    
    func getSpecificValue(firstArray: [UInt8]) {
        // 直接访问内存
        firstArray.withUnsafeBytes { (pointer: UnsafeRawBufferPointer) in
            let data = pointer.bindMemory(to: UInt8.self)
            
            // 以M为后缀的都是内存中的数据
            let commandM = data[0]
            let NBFM = Int(data[1])
            let cksM = data[firstArray.count - 1]
            
            // 判断内存中NBF位和传入的长度是否符合
            if NBFM != firstArray.count - 2 {
                print("内存中长度不对")
                return;
            }
            
            // 判断cks是否对的上
            let cks: UInt8 = calculateCKS(arr: Array(firstArray.dropLast()))
            if cks != cksM {
                print("内存中的cks对不上 cks:\(cks) cksM:\(cksM)")
                return;
            }
            
            // 根据指令类型开始解析数据
             switch commandM {
             case SensorCommand.CO2Waveform.rawValue:
                handleCO2Waveform(data: data, NBFM: NBFM)
             case SensorCommand.Zero.rawValue:
                handleSetZero(data: data, NBFM: NBFM)
             case SensorCommand.Settings.rawValue:
                handleSettings(data: data)
             case SensorCommand.Expand.rawValue:
                handleSystemExpand(data: data)
             default:
                 print("未知指令")
             }
        }
    }

    func resetSendData() {
        sendArray = []
    }

    // 处理CO2波形数据
    func handleCO2Waveform(data: UnsafeBufferPointer<UInt8>, NBFM: Int) {
        let DPIM = data[5] // DPIM 只在接受CO2波形时体现
        // 计算当前CO2数据
        currentCO2 = Float((128 * Int(data[3]) + Int(data[4]) - 1000)) / 100;
//        print("【当前CO2的值】currentCO2=> \(currentCO2)")
        // TODO: 当前关系到设置单位的，都不移动
        // if( currentCO2 <= m_CO2Scale * 0.02)
        //     currentCO2 = m_CO2Scale * 0.02;

//        print("DPIM 是什么 \(DPIM) \(type(of: DPIM))")
        // 存在DPI位时，常规波形信息还会携带定时上报的内容，需要额外处理
        if NBFM > 4 {
            switch DPIM {
            // TODO: DPI=1还没有处理
            case 0x01: // CO2工作状态
                // analysisCO2WaveDPI1(firstArray);
                print("DPI暂时没有处理")
            case 0x02: // 监测计算到的 ETCO2 数值
                ETCO2 = Float(Int(data[6]) * 128 + Int(data[7])) / 10;
            case 0x03: // 表示监测计算到的呼吸率数值
                RespiratoryRate = Int(Int(data[6]) * 128 + Int(data[7]));
            case 0x04: // 表示监测计算到的 FiCO2 数值
                FiCO2 = Int((Int(data[6]) * 128 + Int(data[7])) / 10);
            case 0x05: // 表示检测到一次呼吸，该帧只有在检查到呼吸之后才会设置一次
                Breathe = true;
            case 0x07: // 该帧在硬件确实有问题的时候，将每秒自动汇报一次
                print("CO2Waveform DPI 0X07 不处理")
            default:
                print("CO2Waveform DPI 不匹配")
            }
        }
        // TODO: 告警范围改变修改，没有迁移
        // updateRangeAlarm();
        // 将受到的数据绘制到曲线图上
        // TODO: 添加绘图
        receivedCO2WavedData.append(DataPoint(value: currentCO2))
        if receivedCO2WavedData.count > maxXPoints {
            receivedCO2WavedData.removeFirst()
        }
//        print("外面的长度是多少 \(receivedCO2WavedData.count)")

        // emit statsChanged();
    }

    // 处理校零
    func handleSetZero(data: UnsafeBufferPointer<UInt8>, NBFM: Int) {
        if NBFM <= 1 {
            return
        }
        // 较零状态: 0: 较零开始 1: 模块还未准备好较零 2: 较零中 3: 模块尝试较零而且在过去的 20 秒内检测到呼吸。
        let ZSBM = Int(data[2])
        switch ZSBM {
        case ZSBState.Start.rawValue:
            canZero = true;
        case ZSBState.NotReady.rawValue:
            canZero = false;
            // setError(languageContent.sZeroNoReadyErr());
        case ZSBState.Resetting.rawValue:
            canZero=false;
            // setError(languageContent.sZeroingErr());
        case ZSBState.DetectBreathing.rawValue:
            canZero = false;
            // setError(languageContent.sZeroDetectBreathingErr());
        default:
            print("校零过程中，遇到异常ZSB: \(ZSBM)")
        }
        // TODO: 校零暂时没有迁移
        // zeroStatusChanged();
    }
    
    // 处理设置
    func handleSettings(data: UnsafeBufferPointer<UInt8>) {
        switch Int(data[2]) {
        case ISBState.AirPressure.rawValue:
            barometricPressure = 128 * Int(data[3]) + Int(data[4]);
        case ISBState.NoBreaths.rawValue:
            NoBreaths = Int(data[3]) != 0;
            // case ISBState.UnVerified.rawValue:
            //     // TODO:  没有搞清楚这个是什么
            //     setCO2Unit(data[3],false);
            //     updateCO2Scale();
            //     getAlarmParams();
            //     changeAlarmRange();
            //     m_updateParams=true;
            //     emit displayParamChanged();
            //     sendContinuous();
        case ISBState.GasCompensation.rawValue:
            O2Compensation = Int(data[3]);
            // TODO: 这里没有做
            // emit sensorParamsChanged();
        default:
            print("模块参数设置 未知ISB")
        }
    }

    // 处理系统扩展
    func handleSystemExpand(data: UnsafeBufferPointer<UInt8>) {
        switch Int(data[2]) {
        case ExpandState.HardFirmVersion.rawValue:
            sFirmwareVersion = "\(Int(data[3])).\(Int(data[4])).\(Int(data[5]))"
            sHardwareVersion = "\(Int(data[6])).\(Int(data[7]))"
            print("获取硬件的设置 sFirmwareVersion:\(sFirmwareVersion) sHardwareVersion:\(sHardwareVersion)")
            // emit systemParamsChanged();
        case ExpandState.DateSerialNumber.rawValue:
            sProductionDate = "\(Int(data[3]) + 2000)/\(Int(data[4]))/\(Int(data[5]))"
            sSerialNumber = String(Int(data[6]) * Int(pow(2.0, 14.0)) + Int(data[7]) * Int(pow(2.0, 7.0)) + Int(data[8]))
            print("获取生产日期和序列号 sProductionDate:\(sProductionDate) sSerialNumber:\(sSerialNumber)")
            // emit systemParamsChanged();
        // case 42:
            // setAlarmParams((qreal)(data[3]*128+data[4])/10,(qreal)(data[5]*128+data[6])/10,
            //         data[7]*128+data[8],data[9]*128+data[10],false);
            // emit alarmParamsChanged();
            // sendContinuous();
        // case 44:
            // setCO2ScaleByIndex(data[3],false);
            // emit displayParamChanged();
            // sendContinuous();
        default:
            print("扩展指令未知场景")
        }
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
            characteristicStateUpdate(characteristic: characteristic)
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
