import CoreBluetooth
import Combine
import Foundation
import UIKit

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
    case Settings = 0x84 // 更改读取设置
    case Expand = 0xF2 // 获取系统扩展设置，如硬件、软件版本
    case NACKError = 0xC8 // 非应答错误
    case GetSoftwareRevision = 0xCA // 获取软件版本
    case StopContinuous = 0xC9 // 停止连读模式
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

// 蓝牙服务顺序排列
let BLEServerOrderedUUID: [UInt16] = [
    BLEServerUUID.BLEAntihijackSer.rawValue,
    BLEServerUUID.BLEReceiveDataSer.rawValue,
    BLEServerUUID.BLESendDataSer.rawValue,
    BLEServerUUID.BLEModuleParamsSer.rawValue,
]

// 蓝牙特征UUID
enum BLECharacteristicUUID: UInt16 {
    case BLESendDataCha = 0xFFE9
    case BLEReceiveDataCha = 0xFFE4
    case BLERenameCha = 0xFF91
    case BLEBaudCha = 0xFF93
    case BLEAntihijackChaNofi = 0xFFC2
    case BLEAntihijackCha = 0xFFC1
};

// 顺序排列蓝牙特征值UUID
let BLECharacteristicOrderedUUID: [UInt16] = [
    BLECharacteristicUUID.BLEAntihijackCha.rawValue,
    BLECharacteristicUUID.BLEAntihijackChaNofi.rawValue,
    BLECharacteristicUUID.BLEReceiveDataCha.rawValue,
    BLECharacteristicUUID.BLESendDataCha.rawValue,
    BLECharacteristicUUID.BLERenameCha.rawValue,
    BLECharacteristicUUID.BLEBaudCha.rawValue
]

// 蓝牙描述符UUID
enum BLEDescriptorUUID: UInt16 {
    case CCCDDescriptor = 0x2902
}

// 校零状态
enum ZSBState: Int {
    case Start = 0
    case Resetting = 4
    case NotReady =  8
    case DetectBreathing = 12
}

// 【ISB】读取/设置模块指令
enum ISBState84H: Int {
    case NoUse = 0 // 无效的参数设置
    case AirPressure = 1 // 大气压
    case Temperature = 4 // 气体温度
    case ETCO2Period = 5 // ETCO2 时间周期
    case NoBreaths = 6 // 窒息时间
    case SetCO2Unit = 7 // 设置CO2Unit
    case Sleep = 8 // 休眠模式
    case ZeroPointGasType = 9 // 零点气类型
    case GasCompensation = 11 // 读取/设置 气体补偿
    case GetSensorPartNumber = 18 // 获取sensor part number
    case GetSerialNumber = 20 // 获取sensor serial number
    case GetHardWareRevision = 21 // 获取硬件版本
    case Stop = 27 // 停止采样气泵
}

// 【ISB】扩展指令
enum ISBStateF2H: Int {
    case NoUse = 0x2A //
    case CO2Scale = 0x2C //
}

// 【ISB】获取软件信息指令
enum ISBStateCAH: Int {
    case GetSoftWareRevision = 99 // 获取软件版本
    case GetProductionDate = 98 // 生产日期
    case GetModuleName = 97 // 模块名称
}

// 所有CMD的ISB聚合使用
enum ISBState {
    case CMD_84H(ISBState84H)
    case CMD_CAH(ISBStateCAH)
    case CMD_F2H(ISBStateF2H)
}

class BluetoothManager: NSObject, ObservableObject, CBCentralManagerDelegate, CBPeripheralDelegate {
    private var centralManager: CBCentralManager!
    @Published var discoveredPeripherals = [CBPeripheral]() // 周围设备列表，创建了一个指定类型的空列表
    @Published var connectedPeripheral: CBPeripheral? // 已链接设备
    @Published var receivedCO2WavedData: [DataPoint] = Array(repeating: DataPoint(value: unRealValue), count: maxXPoints)
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
    var isCorrectZero: Bool = false
    var barometricPressure: Int = 0
    var NoBreaths: Bool = false
    var O2Compensation: Int = 0
    // var sFirmwareVersion: String = ""
    var deviceName: String = ""
    var sHardwareVersion: String = ""
    var sSoftwareVersion: String = ""
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
    var shutdownCallback: (() -> Void)?
    var correctZeroCallback: (() -> Void)?
    var updateCO2ScaleCallback: (() -> Void)?
    var getSettingInfoCallback: ((String, ISBState) -> Void)?
    var isSupportCMD: [UInt8] = [
        SensorCommand.CO2Waveform.rawValue,
        SensorCommand.Settings.rawValue,
        SensorCommand.GetSoftwareRevision.rawValue,
        SensorCommand.Expand.rawValue,
    ]
    let savedPeripheralIdentifierKey = "SavedPeripheralIdentifier"

    // 图标展示的实时单位、范围、速度
    @Published var CO2Unit: CO2UnitType = .mmHg {
        didSet {
            switch CO2Unit {
                case .mmHg:
                    CO2Scale = .mmHg_Small
                    CO2Scales = [.mmHg_Small, .mmHg_Middle, .mmHg_Large]
                    CO2ScaleStep = 10
                case .Percentage:
                    CO2Scale = .percentage_Small
                    CO2Scales = [.percentage_Small, .percentage_Middle, .percentage_Large]
                    CO2ScaleStep = 2
                case .KPa:
                    CO2Scale = .KPa_Small
                    CO2Scales = [.KPa_Small, .KPa_Middle, .KPa_Large]
                    CO2ScaleStep = 2
            }
        }
    }

    // 展示设置
    @Published var CO2Scale: CO2ScaleEnum = .mmHg_Small
    @Published var CO2ScaleStep: Double = 10
    @Published var CO2Scales: [CO2ScaleEnum] = [.mmHg_Small, .mmHg_Middle, .mmHg_Large]
    @Published var WFSpeed: WFSpeedEnum = .Two // 目前未用到

    // 报警参数设置
    @Published var etCo2Lower: CGFloat = CGFloat(UserDefaults.standard.float(forKey: "etCo2Lower"))
    @Published var etCo2Upper: CGFloat = CGFloat(UserDefaults.standard.float(forKey: "etCo2Upper"))
    @Published var rrLower: CGFloat = CGFloat(UserDefaults.standard.float(forKey: "rrLower"))
    @Published var rrUpper: CGFloat = CGFloat(UserDefaults.standard.float(forKey: "rrUpper"))

    // 报警参数范围
    let etco2Min: CGFloat = 0
    let etco2Max: CGFloat = 99
    let rrMin: CGFloat = 3
    let rrMax: CGFloat = 150

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

            if receivedArray.count >= 20 {
                let firstArray = getCMDDataArray()
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
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            sendArray.append(SensorCommand.CO2Waveform.rawValue)
            sendArray.append(0x02)
            sendArray.append(0x00)
            appendCKS()

            let data = convertToData(from: sendArray)

            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            resetSendData()
        }
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
    func getDeviceInfo(cb: @escaping (String, ISBState) -> Void) {
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            // ISB=21
            sendArray.append(SensorCommand.Settings.rawValue)
            sendArray.append(0x02)
            sendArray.append(0x15)
            appendCKS()
            getSettingInfoCallback = cb

            let sData1 = convertToData(from: sendArray)
            peripheral.writeValue(sData1, for: characteristic, type: .withResponse)
            resetSendData()

            // ISB=20
            sendArray.append(SensorCommand.Settings.rawValue)
            sendArray.append(0x02)
            sendArray.append(0x14)
            appendCKS()

            let sData2 = convertToData(from: sendArray)
            peripheral.writeValue(sData2, for: characteristic, type: .withResponse)
            resetSendData()

            // ISB=18
            sendArray.append(SensorCommand.Settings.rawValue)
            sendArray.append(0x02)
            sendArray.append(0x12)
            appendCKS()

            let sData3 = convertToData(from: sendArray)
            peripheral.writeValue(sData3, for: characteristic, type: .withResponse)
            resetSendData()

            // 获取软件版本
            sendArray.append(SensorCommand.GetSoftwareRevision.rawValue)
            sendArray.append(0x02)
            sendArray.append(0x00)
            appendCKS()

            let sData4 = convertToData(from: sendArray)
            peripheral.writeValue(sData4, for: characteristic, type: .withResponse)
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

    // 发送关机指令
    func shutdown(cb: @escaping () -> Void) {
        sendArray.append(SensorCommand.Reset.rawValue)
        sendArray.append(0x01)
        appendCKS()

        let data = convertToData(from: sendArray)
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            resetSendData()
            shutdownCallback = cb
        }
    }

    // 发送校零指令
    func correctZero(cb: @escaping () -> Void) {
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            sendArray.append(SensorCommand.Zero.rawValue)
            sendArray.append(0x01)
            appendCKS()

            let data = convertToData(from: sendArray)

            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            resetSendData()
            correctZeroCallback = cb
        }
    }

    // 更新CO2单位/CO2Scale
    func updateCO2Unit(cb: @escaping () -> Void) {
        // 读取单位前，必须要先停止设置
        sendStopContinuous()

        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            // 设置CO2单位
            sendArray.append(SensorCommand.Settings.rawValue)
            sendArray.append(0x03)
            sendArray.append(UInt8(ISBState84H.SetCO2Unit.rawValue))
            if CO2Unit == CO2UnitType.mmHg {
                sendArray.append(0x00)
            } else if CO2Unit == CO2UnitType.KPa {
                sendArray.append(0x01)
            } else if CO2Unit == CO2UnitType.Percentage {
                sendArray.append(0x02)
            }
            appendCKS()
            let data = convertToData(from: sendArray)
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            resetSendData()

            // 设置CO2Scale
            sendArray.append(SensorCommand.Expand.rawValue)
            sendArray.append(0x03)
            sendArray.append(UInt8(ISBStateF2H.CO2Scale.rawValue))
            switch CO2Scale {
                case .mmHg_Small, .percentage_Small, .KPa_Small:
                    sendArray.append(0x00)
                case .mmHg_Middle, .percentage_Middle, .KPa_Middle:
                    sendArray.append(0x01)
                case .mmHg_Large, .percentage_Large, .KPa_Large:
                    sendArray.append(0x02)
            }
            appendCKS()
            let data2 = convertToData(from: sendArray)
            peripheral.writeValue(data2, for: characteristic, type: .withResponse)
            resetSendData()

            cb()
        } else {
            // TODO: 这里要说明，他失败了
            cb()
        }
    }

    // 调整ETCO2/RR的报警范围
    func updateAlertRange() {
        if let peripheral = connectedPeripheral, let characteristic = sendDataCharacteristic {
            sendArray.append(SensorCommand.Expand.rawValue)
            let _etCo2Upper = Int(round(etCo2Upper) * 10)
            let _etCo2Lower = Int(round(etCo2Lower) * 10)
            let _rrUpper = Int(round(rrUpper))
            let _rrLower = Int(round(rrLower))
            sendArray.append(0x0A)
            sendArray.append(0x2A) // ISB=42
            sendArray.append(UInt8(_etCo2Upper >> 7))
            sendArray.append(UInt8(_etCo2Upper & 0x7f))
            sendArray.append(UInt8(_etCo2Lower >> 7))
            sendArray.append(UInt8(_etCo2Lower & 0x7f))
            sendArray.append(UInt8(_rrUpper >> 7))
            sendArray.append(UInt8(_rrUpper & 0x7f))
            sendArray.append(UInt8(_rrLower >> 7))
            sendArray.append(UInt8(_rrLower & 0x7f))
            appendCKS()
            let data = convertToData(from: sendArray)
            peripheral.writeValue(data, for: characteristic, type: .withResponse)
            resetSendData()
        }
    }

    // 保持屏幕常亮
    func keepScreenOn(cb: @escaping () -> Void) {
        UIApplication.shared.isIdleTimerDisabled = !UIApplication.shared.isIdleTimerDisabled
        cb()
    }

    func getCMDDataArray() -> [UInt8] {
        var getArray: [UInt8] = [];
        let command: UInt8 = receivedArray[0]
        
        // 从接受到数据头部不停的向后移动，直到获取指令类型
        while receivedArray.count > 0
            && command != SensorCommand.CO2Waveform.rawValue
            && command != SensorCommand.Zero.rawValue
            && command != SensorCommand.Settings.rawValue
            && command != SensorCommand.Expand.rawValue
            && command != SensorCommand.GetSoftwareRevision.rawValue
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

        return getArray;
    }
    
    // 从蓝牙返回数据中解析返回值
    func getSpecificValue(firstArray: [UInt8]) {
        // 直接访问内存
        firstArray.withUnsafeBytes { (pointer: UnsafeRawBufferPointer) in
            let data = pointer.bindMemory(to: UInt8.self)
            
            // 以M为后缀的都是内存中的数据
            let commandM = data[0]
            let NBFM = Int(data[1])
            let cksM = data[firstArray.count - 1]
            
            if !isSupportCMD.contains(commandM) {
                return;
            }

            // 判断内存中NBF位和传入的长度是否符合
            if NBFM != firstArray.count - 2 {
                print("内存中长度不对 commandM:\(commandM)  NBFM:\(NBFM) Total:\(firstArray.count - 2)")
               return;
            }

            // 判断cks是否对的上
            let cks: UInt8 = calculateCKS(arr: Array(firstArray.dropLast()))
            if cks != cksM {
                print("checksum校验失败 cks:\(cks) 接收到的cks:\(cksM)")
                return;
            }
            
            // 根据指令类型开始解析数据
            switch commandM {
            case SensorCommand.CO2Waveform.rawValue:
                handleCO2Waveform(data: data, NBFM: NBFM)
            case SensorCommand.Settings.rawValue:
                handleSettings(data: data)
            case SensorCommand.GetSoftwareRevision.rawValue:
                print("处理获取软件信息")
                handleSofrWareVersion(data: data)
            case SensorCommand.Expand.rawValue:
                handleSystemExpand(data: data)
            default:
                print("未知指令 \(commandM)")
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

        // 存在DPI位时，常规波形信息还会携带定时上报的内容，需要额外处理
        if NBFM > 4 {
            switch DPIM {
            case 0x01: // CO2工作状态
               handleSetZero(data: data, NBFM: NBFM)
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
        receivedCO2WavedData.append(DataPoint(value: currentCO2))
        if receivedCO2WavedData.count > maxXPoints {
            receivedCO2WavedData.removeFirst()
        }
        // TODO: 没有迁移
        // emit statsChanged();
    }

    // 处理校零，校零结果会在80h中获取，DPI=1
    func handleSetZero(data: UnsafeBufferPointer<UInt8>, NBFM: Int) {
        if NBFM <= 1 {
            return
        }
        // 从80h中获取的校零状态数据
        let ZSBM = Int(data[7] & 12)
        switch ZSBM {
        case ZSBState.Start.rawValue:
            // 如果重新恢复到0，前面又是正在检测中，说明校零成功
            if isCorrectZero {
                correctZeroCallback?()
                isCorrectZero = false
            }
        case ZSBState.Resetting.rawValue:
            isCorrectZero = true;
        case ZSBState.NotReady.rawValue, ZSBState.DetectBreathing.rawValue:
            isCorrectZero = false;
        default:
            isCorrectZero = false;
        }
    }
    
    // 处理设置
    func handleSettings(data: UnsafeBufferPointer<UInt8>) {
        print("受到的setting试试吗\(Int(data[2]))")
        switch Int(data[2]) {
            case ISBState84H.AirPressure.rawValue:
                barometricPressure = 128 * Int(data[3]) + Int(data[4]);
            case ISBState84H.NoBreaths.rawValue:
                NoBreaths = Int(data[3]) != 0;
            case ISBState84H.SetCO2Unit.rawValue:
                // NoBreaths = Int(data[3]) != 0;
                print("这是什么书吗?? \(Int(data[3]))")
            case ISBState84H.GetSensorPartNumber.rawValue:
                deviceName = ""
                for i in 0..<10 {
                    if let uScalar = UnicodeScalar(Int(data[i + 3])) {
                        deviceName += String(uScalar)
                    }
                }
            getSettingInfoCallback?(deviceName, ISBState.CMD_84H(.GetSensorPartNumber))
            case ISBState84H.GetSerialNumber.rawValue:
                // B2A解析方法
                let DB1: Double = Double(data[3]) * pow(2, 28)
                let DB2: Double = Double(data[4]) * pow(2, 21)
                let DB3: Double = Double(data[5]) * pow(2, 14)
                let DB4: Double = Double(data[6]) * pow(2, 7)
                let DB5: Double = Double(data[7])
                let sNum = DB1 + DB2 + DB3 + DB4 + DB5
                let sSerialNumber = String(format: "%.0f", sNum)
            getSettingInfoCallback?(sSerialNumber, ISBState.CMD_84H(.GetSerialNumber))
            case ISBState84H.GetHardWareRevision.rawValue:
                if let DB1 = UnicodeScalar(Int(data[3])), let DB2 = UnicodeScalar(Int(data[4])), let DB3 = UnicodeScalar(Int(data[5])) {
                    sHardwareVersion = "\(DB1).\(DB2).\(DB3)"
                    getSettingInfoCallback?(sHardwareVersion, ISBState.CMD_84H(.GetHardWareRevision))
                }
            case ISBState84H.GasCompensation.rawValue:
                O2Compensation = Int(data[3]);
                // TODO: 这里没有做
                // emit sensorParamsChanged();
            // case ISBState84H.UnVerified.rawValue:
            // TODO:  没有搞清楚这个是什么
            //     setCO2Unit(data[3],false);
            //     updateCO2Scale();
            //     getAlarmParams();
            //     changeAlarmRange();
            //     m_updateParams=true;
            //     emit displayParamChanged();
            //     sendContinuous();
            default:
                print("模块参数设置 未知ISB")
        }
    }

    // TODO:(wsw) 这里逻辑不太了解
    func handleSofrWareVersion(data: UnsafeBufferPointer<UInt8>) {
        let NBFM = Int(data[1])
        sSoftwareVersion = ""
        for i in 0..<NBFM {
            if let uScalar = UnicodeScalar(Int(data[i + 3])) {
                sSoftwareVersion += String(uScalar)
            }
        }

        do {
            let pattern = "(\\d{2})\\s(\\d{2}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2})"
            let regex = try NSRegularExpression(pattern: pattern, options: [])
            let nsString = sSoftwareVersion as NSString
            let results = regex.matches(in: sSoftwareVersion, options: [], range: NSRange(location: 0, length: nsString.length))
            
            // 提取时间部分和剩余文本
            if let match = results.first {
                let yearRange = match.range(at: 1)
                let dateTimeRange = match.range(at: 2)
                
                let yearString = nsString.substring(with: yearRange)
                let dateTimeString = nsString.substring(with: dateTimeRange)
                let fullTimeString = yearString + " " + dateTimeString
                
                var remainingText = nsString.replacingCharacters(in: match.range, with: "").trimmingCharacters(in: .whitespaces)
                
                // 移除非打印字符（如 NULL 字符）
                remainingText = remainingText.components(separatedBy: .controlCharacters).joined()
                // 移除最后面的 '-'
                remainingText = remainingText.trimmingCharacters(in: CharacterSet(charactersIn: "-"))
                
                // 解析和格式化时间
                let formatter = DateFormatter()
                formatter.dateFormat = "yy MM/dd/yy HH:mm"
                if let date = formatter.date(from: fullTimeString) {
                    formatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
                    let formattedDateString = formatter.string(from: date)
                    print("Formatted Time: \(formattedDateString)")
                    getSettingInfoCallback?(formattedDateString, ISBState.CMD_CAH(.GetProductionDate))
                } else {
                    print("Failed to parse date")
                }
                
                print("Remaining text: \(remainingText)")
                getSettingInfoCallback?(remainingText, ISBState.CMD_CAH(.GetModuleName))
            } else {
                print("No match found")
            }
        } catch let error {
            print("Invalid regex: \(error.localizedDescription)")
        }
    }

    // 处理系统扩展
    func handleSystemExpand(data: UnsafeBufferPointer<UInt8>) {
        print("接受系统扩展相关信息=>\(Int(data[2]))")

        // WLD扩展ISB
        switch Int(data[2]) {
        // case 41: // 查询设备电源状态
        // case 42: // 读取报警配置
        // case 43: // 读取报警静音状态
            // setAlarmParams((qreal)(data[3]*128+data[4])/10,(qreal)(data[5]*128+data[6])/10,
            //         data[7]*128+data[8],data[9]*128+data[10],false);
            // emit alarmParamsChanged();
            // sendContinuous();
        case 44: // 设置波形显示范围
            sendContinuous();
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
        UserDefaults.standard.set(peripheral.identifier.uuidString, forKey: savedPeripheralIdentifierKey)
    }
    
    // 链接失败
    func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: Error?) {
        print("链接失败")
    }

    // 设备断开链接后
    func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: Error?) {
        if connectedPeripheral == peripheral {
            connectedPeripheral = nil
            shutdownCallback?()
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
        if error != nil {
            print("特征\(characteristic.uuid)返回值异常=> \(error)")
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
            // 恢复连接
            if let savedIdentifier = UserDefaults.standard.string(forKey: savedPeripheralIdentifierKey) {
                let peripherals = centralManager.retrievePeripherals(withIdentifiers: [UUID(uuidString: savedIdentifier)!])
                if let _peripheral = peripherals.first {
                    connectedPeripheral = _peripheral
                    central.connect(_peripheral, options: nil)
                }
            }
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
