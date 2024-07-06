import Foundation

enum Languages: String {
    case Chinese = "中文"
    case English = "English"
}

enum CO2UnitType: String {
    case KPa = "kPa"
    case Percentage = "%"
    case mmHg = "mmHg"
}

enum CO2ScaleEnum: Double {
    case mmHg_Small = 50
    case mmHg_Middle = 60
    case mmHg_Large = 75
    case KPa_Small = 6.7
    case KPa_Middle = 8
    case KPa_Large = 10
    case percentage_Small = 6.6
    case percentage_Middle = 7.9
    case percentage_Large = 9.9
}

enum WFSpeedEnum: Int {
    case One = 1
    case Two = 2
    case Four = 4
}

//默认值信息
let defaultDeviceInfo: String = "--"

class AppConfigManage: ObservableObject {
    init() {
        // 设置默认值
        UserDefaults.standard.register(defaults: [
            "airPressure": 103,
            "asphyxiationTime": 30,
            "oxygenCompensation": 20,
            "etCo2Lower": 0,
            "etCo2Upper":50,
            "rrLower": 20,
            "rrUpper": 50,
            "CO2Scale": CO2ScaleEnum.mmHg_Small.rawValue,
            "CO2Unit": CO2UnitType.mmHg.rawValue
        ])
    }
    
    // 静态的属性，从外设中读取后就不变化了
    @Published var firmwareVersion: String = defaultDeviceInfo
    @Published var hardwareVersion: String = defaultDeviceInfo
    @Published var softwareVersion: String = defaultDeviceInfo
    @Published var productionDate: String = defaultDeviceInfo
    @Published var serialNumber: String = defaultDeviceInfo
    @Published var ModuleName: String = "CapnoGraph"

    // 全局loading相关配置
    @Published var loadingMessage: String = ""
    
    // Toast相关配置
     @Published var toastMessage: String = ""
    @Published var toastType: ToastType = ToastType.SUCCESS
    
    // App语言设置
    @Published var language: Languages = Languages.Chinese

    // 模块参数设置
    @Published var airPressure: Double = UserDefaults.standard.double(forKey: "airPressure")
    @Published var asphyxiationTime: Double = UserDefaults.standard.double(forKey: "asphyxiationTime")
    @Published var oxygenCompensation: Double = UserDefaults.standard.double(forKey: "oxygenCompensation")
}
