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

//App中文
enum AppTextsChinese: String {
    // 底部tab上文字
    case TabSearch = "搜索"
    case TabMain = "主页"
    case TabSetting = "设置"
    // 顶部标题
    case TitleSearch = " - 附近设备"
    case TitleMain = ""
    case TitleSetting = " - 设置"
    case TitleSystemSetting = " - 系统设置"
    case TitleAlertParams = " - 报警设置"
    case TitleModuleConfig = " - 模块设置"
    case TitleDisplayConfig = " - 显示设置"
    // 搜索结果页
    case SearchNoResult = "附近没有可用蓝牙设备！"
    case SearchBtn = "搜索设备"
    case SearchConfirmTitle = "确认要链接此设备？"
    case SearchConfirmYes = "链接"
    case SearchConfirmNo = "取消"
    case SearchConnecting = "链接中"
    case SearchConnected = "链接成功"
    case SearchSearching = "搜索设备中"
    case SearchDevicePrefix = "设备名"
    // 主页
    case MainDeviceName = "设备名称"
    case MainDeviceID = "设备ID"
    case MainPR = "PR/呼吸率"
    case MainETCO2 = "ETCO2"
    case MainUnknownName = "未知设备"
    case MainLineCharUnit = "秒"
    // 设置页
    case SettingBLConnect = "蓝牙连接"
    case SettingReset = "校零"
    case SettingAlertParams = "报警参数"
    case SettingDisplayParams = "显示参数"
    case SettingModuleParams = "模块参数"
    case SettingSystem = "系统设置"
    case SettingShutDown = "关机"
    case SettingLighter = "屏幕常亮"
    // 系统设置页
    case SystemLanguage = "语言"
    case SystemFirmwareVersion = "固件版本"
    case SystemHardwareVersion = "硬件版本"
    case SystemSoftwareVersion = "软件版本"
    case SystemProductionDate = "生产日期"
    case SystemSerialNumber = "序列号"
    case SystemModuleName = "模块名称"
    // 模块设置页
    case ModuleAirPressure = "大气压(mmHg)"
    case ModuleAsphyxiationTime = "窒息时间(S)"
    case ModuleOxygenCompensation = "氧气补偿(%)"
    // 通用
    case CommonUpdateBtn = "更新"
    // 展示设置页
    case DisplayCO2Unit = "CO2单位"
    case DisplayCO2Scale = "CO2 Scale"
    case DisplayWFSpeed = "WF Speed"
    // 报警设置页
    case AlertETCO2 = "ETCO2范围(mmHg)"
    case AlertRR = "RR范围(bmp)"
    // Toast相关文案
    case ToastZeroing = "正在校零"
    case ToastZeroComplete = "校零结束"
    case ToastShutDown = "正在关机"
    case ToastShutDownComplete = "已关机"
    case ToastLighting = "正在设置常亮"
    case ToastLighterFinished = "设置常亮成功"
    // 更新信息
    case UpdateSetting = "更新中"
    case UpdateSettingFinished = "更新成功"
    // 无设备提示
    case NoDeviceTitle = "未连接设备"
    case NoDeviceMessage = "需要先链接设备才能设置功能"
    case NoDeviceJump = "跳转"
}

//App英文
enum AppTextsEnglish: String {
    // 底部tab上文字
    case TabSearch = "Search"
    case TabMain = "Main"
    case TabSetting = "Setting"
    // 顶部标题
    case TitleSearch = " - Nearby Devices"
    case TitleMain = ""
    case TitleSetting = " - Setting"
    case TitleSystemSetting = " - System Setting"
    case TitleAlertParams = " - Alert Config"
    case TitleModuleConfig = " - Module Config"
    case TitleDisplayConfig = " - Display Config"
    // 搜索结果页
    case SearchNoResult = "There are no available Bluetooth devices nearby!"
    // TODO: 后面要么统一了，要么改了
    case SearchBtn = "Search "
    case SearchConfirmTitle = "Are you sure you want to connect this device?"
    case SearchConfirmYes = "Connect"
    case SearchConfirmNo = "Cancel"
    case SearchConnecting = "Connecting"
    case SearchConnected = "Link Successful"
    case SearchSearching = "Searching for Devices"
    case SearchDevicePrefix = "Name"
    // 主页
    case MainDeviceName = "Device Name"
    case MainDeviceID = "Device ID"
    case MainPR = "PR/Respiratory Rate"
    case MainETCO2 = "ETCO2"
    case MainUnknownName = "Unknown Device"
    case MainLineCharUnit = "S"
    // 设置页
    case SettingBLConnect = "Connect Bluetooth"
    case SettingReset = "Zero Calibration"
    case SettingAlertParams = "Alarm Parameters"
    case SettingDisplayParams = "Display Parameters"
    case SettingModuleParams = "Module Parameters"
    case SettingSystem = "System Settings"
    case SettingShutDown = "ShutDown"
    case SettingLighter = "Keep Screen On"
    // 系统设置页
    case SystemLanguage = "Language"
    case SystemFirmwareVersion = "Firmware Version"
    case SystemHardwareVersion = "Hardware Version"
    case SystemSoftwareVersion = "Software Version"
    case SystemProductionDate = "Production Date"
    case SystemSerialNumber = "Serial Number"
    case SystemModuleName = "Module Name"
    // 模块设置页
    case ModuleAirPressure = "Air Pressure(mmHg)"
    case ModuleAsphyxiationTime = "Asphyxiation Time(S)"
    case ModuleOxygenCompensation = "Oxygen Compensation(%)"
    // 通用
    case CommonUpdateBtn = "Update"
    // 展示设置页
    case DisplayCO2Unit = "CO2 Unit"
    case DisplayCO2Scale = "CO2 Scale"
    case DisplayWFSpeed = "WF Speed"
    // 报警设置页
    case AlertETCO2 = "ETCO2 Range(mmHg)"
    case AlertRR = "Respiratory Rate Range(bmp)"
    // Toast相关文案
    case ToastZeroing = "Zeroing"
    case ToastZeroComplete = "Zero Calibration Finished"
    case ToastShutDown = "Shutting Down"
    case ToastShutDownComplete = "Powered Off"
    case ToastLighting = "Setting Sceen On"
    case ToastLighterFinished = "Keep Screen On Finished"
    // 更新loading
    case UpdateSetting = "Updating"
    case UpdateSettingFinished = "Update Success"
    // 无设备提示
    case NoDeviceTitle = "Device Not Connected"
    case NoDeviceMessage = "You need to connect the device first to enable this feature."
    case NoDeviceJump = "Jump"
}

enum LocalizedText {
    case english(AppTextsEnglish)
    case chinese(AppTextsChinese)
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
            "etCoLower": 0,
            "etCo2Upper":50,
            "rrLower": 20,
            "rrUpper": 50,
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
    // TODO: 非常臃肿，需要优化
    func getTextByKey(key: String) -> String {
        if self.language == Languages.Chinese {
            switch key {
                // 底部tab上文字
                case "TabSearch":
                    return AppTextsChinese.TabSearch.rawValue
                case "TabMain":
                    return AppTextsChinese.TabMain.rawValue
                case "TabSetting":
                    return AppTextsChinese.TabSetting.rawValue
                // 顶部标题
                case "TitleSearch":
                    return AppTextsChinese.TitleSearch.rawValue
                case "TitleMain":
                    return AppTextsChinese.TitleMain.rawValue
                case "TitleSetting":
                    return AppTextsChinese.TitleSetting.rawValue
                case "TitleSystemSetting":
                    return AppTextsChinese.TitleSystemSetting.rawValue
                case "TitleAlertParams":
                    return AppTextsChinese.TitleAlertParams.rawValue
                case "TitleModuleConfig":
                    return AppTextsChinese.TitleModuleConfig.rawValue
                case "TitleDisplayConfig":
                    return AppTextsChinese.TitleDisplayConfig.rawValue
                // 搜索结果页
                case "SearchNoResult":
                    return AppTextsChinese.SearchNoResult.rawValue
                case "SearchBtn":
                    return AppTextsChinese.SearchBtn.rawValue
                case "SearchConfirmTitle":
                    return AppTextsChinese.SearchConfirmTitle.rawValue
                case "SearchConfirmYes":
                    return AppTextsChinese.SearchConfirmYes.rawValue
                case "SearchConfirmNo":
                    return AppTextsChinese.SearchConfirmNo.rawValue
                case "SearchConnecting":
                    return AppTextsChinese.SearchConnecting.rawValue
                case "SearchConnected":
                    return AppTextsChinese.SearchConnected.rawValue
                case "SearchSearching":
                    return AppTextsChinese.SearchSearching.rawValue
                case "SearchDevicePrefix":
                    return AppTextsChinese.SearchDevicePrefix.rawValue
                // 主页
                case "MainDeviceName":
                    return AppTextsChinese.MainDeviceName.rawValue
                case "MainDeviceID":
                    return AppTextsChinese.MainDeviceID.rawValue
                case "MainPR":
                    return AppTextsChinese.MainPR.rawValue
                case "MainETCO2":
                    return AppTextsChinese.MainETCO2.rawValue
                case "MainUnknownName":
                    return AppTextsChinese.MainUnknownName.rawValue
                case "MainLineCharUnit":
                    return AppTextsChinese.MainLineCharUnit.rawValue
                // 设置页
                case "SettingBLConnect":
                    return AppTextsChinese.SettingBLConnect.rawValue
                case "SettingReset":
                    return AppTextsChinese.SettingReset.rawValue
                case "SettingAlertParams":
                    return AppTextsChinese.SettingAlertParams.rawValue
                case "SettingDisplayParams":
                    return AppTextsChinese.SettingDisplayParams.rawValue
                case "SettingModuleParams":
                    return AppTextsChinese.SettingModuleParams.rawValue
                case "SettingSystem":
                    return AppTextsChinese.SettingSystem.rawValue
                case "SettingShutDown":
                    return AppTextsChinese.SettingShutDown.rawValue
                case "SettingLighter":
                    return AppTextsChinese.SettingLighter.rawValue
                // 系统设置页
                case "SystemLanguage":
                    return AppTextsChinese.SystemLanguage.rawValue
                case "SystemFirmwareVersion":
                    return AppTextsChinese.SystemFirmwareVersion.rawValue
                case "SystemHardwareVersion":
                    return AppTextsChinese.SystemHardwareVersion.rawValue
                case "SystemSoftwareVersion":
                    return AppTextsChinese.SystemSoftwareVersion.rawValue
                case "SystemProductionDate":
                    return AppTextsChinese.SystemProductionDate.rawValue
                case "SystemSerialNumber":
                    return AppTextsChinese.SystemSerialNumber.rawValue
                case "SystemModuleName":
                    return AppTextsChinese.SystemModuleName.rawValue
                // 模块设置页
                case "ModuleAirPressure":
                    return AppTextsChinese.ModuleAirPressure.rawValue
                case "ModuleAsphyxiationTime":
                    return AppTextsChinese.ModuleAsphyxiationTime.rawValue
                case "ModuleOxygenCompensation":
                    return AppTextsChinese.ModuleOxygenCompensation.rawValue
                // 通用
                case "CommonUpdateBtn":
                    return AppTextsChinese.CommonUpdateBtn.rawValue
                // 展示设置页
                case "DisplayCO2Unit":
                    return AppTextsChinese.DisplayCO2Unit.rawValue
                case "DisplayCO2Scale":
                    return AppTextsChinese.DisplayCO2Scale.rawValue
                case "DisplayWFSpeed":
                    return AppTextsChinese.DisplayWFSpeed.rawValue
                // 报警设置页
                case "AlertETCO2":
                    return AppTextsChinese.AlertETCO2.rawValue
                case "AlertRR":
                    return AppTextsChinese.AlertRR.rawValue
                // Toast相关文案
                case "ToastZeroing":
                    return AppTextsChinese.ToastZeroing.rawValue
               case "ToastZeroComplete":
                    return AppTextsChinese.ToastZeroComplete.rawValue
                case "ToastShutDown":
                    return AppTextsChinese.ToastShutDown.rawValue
                case "ToastShutDownComplete":
                    return AppTextsChinese.ToastShutDownComplete.rawValue
                case "ToastLighting":
                    return AppTextsChinese.ToastLighting.rawValue
                case "ToastLighterFinished":
                return AppTextsChinese.ToastLighterFinished.rawValue
                // 更新loading
                case "UpdateSetting":
                    return AppTextsChinese.UpdateSetting.rawValue
                case "UpdateSettingFinished":
                    return AppTextsChinese.UpdateSettingFinished.rawValue
                // 无设备提示
                case "NoDeviceTitle":
                    return AppTextsChinese.NoDeviceTitle.rawValue
                case "NoDeviceMessage":
                    return AppTextsChinese.NoDeviceMessage.rawValue
                case "NoDeviceJump":
                    return AppTextsChinese.NoDeviceJump.rawValue
                default:
                    return ""
            }
        } else {
            switch key {
                // 底部tab上文字
                case "TabSearch":
                    return AppTextsEnglish.TabSearch.rawValue
                case "TabMain":
                    return AppTextsEnglish.TabMain.rawValue
                case "TabSetting":
                    return AppTextsEnglish.TabSetting.rawValue
                // 顶部标题
                case "TitleSearch":
                    return AppTextsEnglish.TitleSearch.rawValue
                case "TitleMain":
                    return AppTextsEnglish.TitleMain.rawValue
                case "TitleSetting":
                    return AppTextsEnglish.TitleSetting.rawValue
                case "TitleSystemSetting":
                    return AppTextsEnglish.TitleSystemSetting.rawValue
                case "TitleAlertParams":
                    return AppTextsEnglish.TitleAlertParams.rawValue
                case "TitleModuleConfig":
                    return AppTextsEnglish.TitleModuleConfig.rawValue
                case "TitleDisplayConfig":
                    return AppTextsEnglish.TitleDisplayConfig.rawValue
                // 搜索结果页
                case "SearchNoResult":
                    return AppTextsEnglish.SearchNoResult.rawValue
                case "SearchBtn":
                    return AppTextsEnglish.SearchBtn.rawValue
                case "SearchConfirmTitle":
                    return AppTextsEnglish.SearchConfirmTitle.rawValue
                case "SearchConfirmYes":
                    return AppTextsEnglish.SearchConfirmYes.rawValue
                case "SearchConfirmNo":
                    return AppTextsEnglish.SearchConfirmNo.rawValue
                case "SearchConnecting":
                    return AppTextsEnglish.SearchConnecting.rawValue
                case "SearchConnected":
                    return AppTextsEnglish.SearchConnected.rawValue
                case "SearchSearching":
                    return AppTextsEnglish.SearchSearching.rawValue
                case "SearchDevicePrefix":
                    return AppTextsEnglish.SearchDevicePrefix.rawValue
                // 主页
                case "MainDeviceName":
                    return AppTextsEnglish.MainDeviceName.rawValue
                case "MainDeviceID":
                    return AppTextsEnglish.MainDeviceID.rawValue
                case "MainPR":
                    return AppTextsEnglish.MainPR.rawValue
                case "MainETCO2":
                    return AppTextsEnglish.MainETCO2.rawValue
                case "MainUnknownName":
                    return AppTextsEnglish.MainUnknownName.rawValue
                case "MainLineCharUnit":
                    return AppTextsEnglish.MainLineCharUnit.rawValue
                // 设置页
                case "SettingBLConnect":
                    return AppTextsEnglish.SettingBLConnect.rawValue
                case "SettingReset":
                    return AppTextsEnglish.SettingReset.rawValue
                case "SettingAlertParams":
                    return AppTextsEnglish.SettingAlertParams.rawValue
                case "SettingDisplayParams":
                    return AppTextsEnglish.SettingDisplayParams.rawValue
                case "SettingModuleParams":
                    return AppTextsEnglish.SettingModuleParams.rawValue
                case "SettingSystem":
                    return AppTextsEnglish.SettingSystem.rawValue
                case "SettingShutDown":
                    return AppTextsEnglish.SettingShutDown.rawValue
                case "SettingLighter":
                    return AppTextsEnglish.SettingLighter.rawValue
                // 系统设置页
                case "SystemLanguage":
                    return AppTextsEnglish.SystemLanguage.rawValue
                case "SystemFirmwareVersion":
                    return AppTextsEnglish.SystemFirmwareVersion.rawValue
                case "SystemHardwareVersion":
                    return AppTextsEnglish.SystemHardwareVersion.rawValue
                case "SystemSoftwareVersion":
                    return AppTextsEnglish.SystemSoftwareVersion.rawValue
                case "SystemProductionDate":
                    return AppTextsEnglish.SystemProductionDate.rawValue
                case "SystemSerialNumber":
                    return AppTextsEnglish.SystemSerialNumber.rawValue
                case "SystemModuleName":
                    return AppTextsEnglish.SystemModuleName.rawValue
                // 模块设置页
                case "ModuleAirPressure":
                    return AppTextsEnglish.ModuleAirPressure.rawValue
                case "ModuleAsphyxiationTime":
                    return AppTextsEnglish.ModuleAsphyxiationTime.rawValue
                case "ModuleOxygenCompensation":
                    return AppTextsEnglish.ModuleOxygenCompensation.rawValue
                // 通用
                case "CommonUpdateBtn":
                    return AppTextsEnglish.CommonUpdateBtn.rawValue
                // 展示设置页
                case "DisplayCO2Unit":
                    return AppTextsEnglish.DisplayCO2Unit.rawValue
                case "DisplayCO2Scale":
                    return AppTextsEnglish.DisplayCO2Scale.rawValue
                case "DisplayWFSpeed":
                    return AppTextsEnglish.DisplayWFSpeed.rawValue
                // 报警设置页
                case "AlertETCO2":
                    return AppTextsEnglish.AlertETCO2.rawValue
                case "AlertRR":
                    return AppTextsEnglish.AlertRR.rawValue
                // toast相关文案
                case "ToastZeroing":
                    return AppTextsEnglish.ToastZeroing.rawValue
               case "ToastZeroComplete":
                    return AppTextsEnglish.ToastZeroComplete.rawValue
                case "ToastShutDown":
                    return AppTextsEnglish.ToastShutDown.rawValue
                case "ToastShutDownComplete":
                    return AppTextsEnglish.ToastShutDownComplete.rawValue
                case "ToastLighting":
                    return AppTextsEnglish.ToastLighting.rawValue
                case "ToastLighterFinished":
                    return AppTextsEnglish.ToastLighting.rawValue
                // 更新loading
                case "UpdateSetting":
                    return AppTextsEnglish.UpdateSetting.rawValue
                case "UpdateSettingFinished":
                    return AppTextsEnglish.UpdateSettingFinished.rawValue
                // 无设备提示
                case "NoDeviceTitle":
                    return AppTextsChinese.NoDeviceTitle.rawValue
                case "NoDeviceMessage":
                    return AppTextsChinese.NoDeviceMessage.rawValue
                case "NoDeviceJump":
                    return AppTextsChinese.NoDeviceJump.rawValue
                default:
                    return ""
            }
        }
    }

    // 模块参数设置
    @Published var airPressure: Double = UserDefaults.standard.double(forKey: "airPressure")
    @Published var asphyxiationTime: Double = UserDefaults.standard.double(forKey: "asphyxiationTime")
    @Published var oxygenCompensation: Double = UserDefaults.standard.double(forKey: "oxygenCompensation")
}
