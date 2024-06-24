import Foundation

enum Languages: Int {
    case Chinese = 0
    case English = 1
}

enum CO2UnitType: String {
    case KPa = "KPa"
    case Percentage = "%"
    case mmHg = "mmHg"
}

enum CO2ScaleEnum: Double {
    case Small = 6.7
    case Middle = 8
    case Large = 10
}

enum WFSpeedEnum: Int {
    case One = 1
    case Two
    case Three
    case Four
}

class AppConfigManage: ObservableObject {
    // 静态的属性，从外设中读取后就不变化了
    @Published private var firmwareVersion: String = "--"
    @Published private var hardwareVersion: String = "--"
    @Published private var softwareVersion: String = "--"
    @Published private var productionDate: String = ""
    @Published private var serialNumber: String = ""
    @Published private var ModuleName: String = ""
    
    // 可动态设置的属性
    @Published private var language: Languages = Languages.Chinese
    
    // 模块参数设置
    @Published private var airPressure: Double = 0
    @Published private var asphyxiationTime: Double = 0
    @Published private var oxygenCompensation: Double = 0
    
    // 展示参数设置
    @Published private var CO2Unit: CO2UnitType = CO2UnitType.KPa
    @Published private var CO2Scale: CO2ScaleEnum = CO2ScaleEnum.Small
    @Published private var WFSpeed: WFSpeedEnum = WFSpeedEnum.One
    
    // 报警参数设置
    @Published private var etCoLower: Float = 0
    @Published private var etCo2Upper: Float = 0
    @Published private var rrLower: Float = 0
    @Published private var rrUpper: Float = 0
}
