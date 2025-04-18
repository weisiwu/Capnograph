import SwiftUI

// 页面类型
enum ConfigItemTypes: Int {
    case ConnectBlueTooth = 0 // 链接蓝牙
    case CorrectZero = 1 // 校零
    case Alert = 2 // 告警设置二级页
    case Display = 3 // 展示设置二级页
    case Module = 4 // 模块设置二级页
    case System = 5 // 系统设置二级页
    case Showdown = 6 // 关机
    case Lighter = 7 // 屏幕常亮
}

// 二级配置页View
struct BaseConfigContainerView<Content: View>: View {
    let content: Content
    @State private var selectedTabIndex: Int = PageTypes.Config.rawValue
    @State private var selectedConfigPage: Int
    @State private var isLoading = false
    @State private var loadingText = ""
    @State private var toastText = ""
    @EnvironmentObject var appConfigManage: AppConfigManage

    init(configType: ConfigItemTypes, @ViewBuilder content: () -> Content) {
        self.selectedConfigPage = configType.rawValue
        self.content = content()
    }
    
    var body: some View {
        ZStack {
            VStack {
                Color.white.edgesIgnoringSafeArea(.all)
                    .frame(height: 0)
                content
            }
            .overlay(
                isLoading ? LoadingView(loadingText: loadingText) : nil
            )
        }
    }
}

// 一级配置View中的配置项目View
struct ConfigItem: View {
    var text: String
    var icon: String
    var configType: ConfigItemTypes
    @Binding var currentConfigType: Int?
    var handleTapGesture: ((String?) -> Bool)?
    var isLink: Bool {
        get {
            return [ConfigItemTypes.Alert, ConfigItemTypes.Display, ConfigItemTypes.Module, ConfigItemTypes.System].contains(configType)
        }
    }
    @State var isActive: Bool = false
    
    var body: some View {
        if isLink {
            ZStack(alignment: .leading) {
                NavigationLink(destination: SubConfigContainerView(configType: configType.rawValue)) {
                    HStack(spacing: 0) {
                        Text(text)
                            .font(.system(size: 16))
                            .fontWeight(.light)
                            .padding(.bottom, 2)
                        Spacer()
                        Image(icon)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24, height: 24)
                    }
                    .frame(height: 40)
                }
                .opacity(0)
                HStack(spacing: 0) {
                    Text(text)
                        .font(.system(size: 16))
                        .fontWeight(.light)
                        .padding(.bottom, 2)
                    Spacer()
                    Image(icon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                        .padding(.leading, 8)
                }
                .frame(height: 40)
                .contentShape(Rectangle())
                .onAppear {
                    isActive = currentConfigType == configType.rawValue
                }
            }
        } else {
            HStack(spacing: 0) {
                Text(text)
                    .font(.system(size: 16))
                    .fontWeight(.light)
                    .padding(.bottom, 2)
                Spacer()
                Image(icon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
            }
            .frame(height: 40)
            .contentShape(Rectangle())
            .onTapGesture { handleTapGesture?(text) }
        }
    }
}

// 一级配置View
struct ConfigView: View {
    @State var currentConfigType: Int?
    @Binding var selectedTabIndex: Int
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    func handleShutdown() {
        appConfigManage.loadingMessage = ""
        appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "ToastShutDownComplete")
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            appConfigManage.toastMessage = ""
        }
        // 关机后，重置检查位，下次关机继续检查
        appConfigManage.showConfirmShutDownAlert = false
    }

    func handleSetZero() {
        appConfigManage.loadingMessage = ""
        appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "ToastZeroComplete")
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            appConfigManage.toastMessage = ""
        }
    }
    
    func handleSetZeroFail() {
        appConfigManage.loadingMessage = ""
        appConfigManage.showNoDeviceAlert = true
    }

    func handleKeepScreenOn() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            appConfigManage.loadingMessage = ""
            appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "ToastLighterFinished")
            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                appConfigManage.toastMessage = ""
            }
        }
    }

    func handleTapGesture(text: String?) -> Bool {
        var loadingText = ""
        guard let textStr = text else {
            return false
        }

        // 如果没有链接设备，直接出alert，让用户前往链接设备
        if let connectedPeripheral = bluetoothManager.connectedPeripheral, bluetoothManager.receivedArray.count > 0 {
            appConfigManage.showNoDeviceAlert = false
        } else {
            appConfigManage.showNoDeviceAlert = true
            return false
        }

        switch textStr {
            // 校零
            case AppTextsChinese.SettingReset.rawValue:
                loadingText = AppTextsChinese.ToastZeroing.rawValue
            case AppTextsEnglish.SettingReset.rawValue:
                loadingText = AppTextsEnglish.ToastZeroing.rawValue
            // 屏幕常量
            case AppTextsChinese.SettingLighter.rawValue:
                loadingText = AppTextsChinese.ToastLighting.rawValue
            case AppTextsEnglish.SettingLighter.rawValue:
                loadingText = AppTextsEnglish.ToastLighting.rawValue
            default:
                loadingText = ""
        }

        appConfigManage.loadingMessage = loadingText
        
        switch textStr {
            // 校零
            case AppTextsChinese.SettingReset.rawValue, AppTextsEnglish.SettingReset.rawValue:
                appConfigManage.showNoDeviceAlert = false
                appConfigManage.showConfirmShutDownAlert = false
                bluetoothManager.correctZero(cb: handleSetZero, fail: handleSetZeroFail)
            // 关机
            case AppTextsChinese.SettingShutDown.rawValue, AppTextsEnglish.SettingShutDown.rawValue:
                appConfigManage.showNoDeviceAlert = false
                appConfigManage.showConfirmShutDownAlert = true
            // 屏幕常亮
            case AppTextsChinese.SettingLighter.rawValue, AppTextsEnglish.SettingLighter.rawValue:
                appConfigManage.showNoDeviceAlert = false
                appConfigManage.showConfirmShutDownAlert = false
                bluetoothManager.keepScreenOn(cb: handleKeepScreenOn)
            default:
                print("No Action when Click In System Config Page")
        }
        return false
    }
    
    @ViewBuilder
    var body: some View {
        NavigationStack() {
            VStack(spacing: 0) {
                List {
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingReset"),
                        icon: "setting_icon_reset",
                        configType: ConfigItemTypes.CorrectZero,
                        currentConfigType: $currentConfigType,
                        handleTapGesture: handleTapGesture
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingAlertParams"),
                        icon: "setting_icon_back",
                        configType: ConfigItemTypes.Alert,
                        currentConfigType: $currentConfigType
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingDisplayParams"),
                        icon: "setting_icon_back",
                        configType: ConfigItemTypes.Display,
                        currentConfigType: $currentConfigType
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingModuleParams"),
                        icon: "setting_icon_back",
                        configType: ConfigItemTypes.Module,
                        currentConfigType: $currentConfigType
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingSystem"),
                        icon: "setting_icon_back",
                        configType: ConfigItemTypes.System,
                        currentConfigType: $currentConfigType
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingShutDown"),
                        icon: "setting_icon_shutdown",
                        configType: ConfigItemTypes.Showdown,
                        currentConfigType: $currentConfigType,
                        handleTapGesture: handleTapGesture
                    )
                    ConfigItem(
                        text: appConfigManage.getTextByKey(key: "SettingLighter"),
                        icon: "setting_icon_lighter",
                        configType: ConfigItemTypes.Lighter,
                        currentConfigType: $currentConfigType,
                        handleTapGesture: handleTapGesture
                    )
                }
                .background(Color.white)
                .listStyle(PlainListStyle())
                .padding(.bottom, 48)
                .alert(
                    Text(appConfigManage.alertTitle),
                    isPresented: $appConfigManage.showAlert
                ) {
                    Button(appConfigManage.alertConfirmBtn) {
                        appConfigManage.loadingMessage = ""
                        if appConfigManage.showConfirmShutDownAlert {
                            if appConfigManage.language == Languages.Chinese {
                                appConfigManage.loadingMessage = AppTextsChinese.ToastShutDown.rawValue
                            } else {
                                appConfigManage.loadingMessage = AppTextsEnglish.ToastShutDown.rawValue
                            }
                            bluetoothManager.shutdown(cb: handleShutdown)
                        } else {
                            selectedTabIndex = PageTypes.SearchDeviceList.rawValue
                        }
                        appConfigManage.showAlert = false
                    }
                    Button(appConfigManage.getTextByKey(key: "SearchConfirmNo")) {
                        appConfigManage.loadingMessage = ""
                        appConfigManage.showAlert = false
                    }
                } message: {
                    Text(appConfigManage.alertMessage)
                }
            }
            .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleSetting"))")
            .navigationBarTitleDisplayMode(.inline)
            .onDisappear {
                currentConfigType = nil
            }
        }
    }
}

struct SubConfigContainerView: View {
    var configType: Int?
    @EnvironmentObject var bluetoothManager: BluetoothManager
    
    var body: some View {
        switch configType {
        case ConfigItemTypes.Alert.rawValue:
            AlertConfigView()
        case ConfigItemTypes.System.rawValue:
            SystemConfigView()
        case ConfigItemTypes.Module.rawValue:
            ModuleConfigView()
        case ConfigItemTypes.Display.rawValue:
            DisplayConfigView(
                initCO2Unit: bluetoothManager.CO2Unit,
                initCO2Scale: bluetoothManager.CO2Scale,
                CO2Scales: bluetoothManager.CO2Scales,
                connectedPeripheral: bluetoothManager.connectedPeripheral,
                checkBluetoothStatus: bluetoothManager.checkBluetoothStatus,
                updateCO2Unit: bluetoothManager.updateCO2Unit,
                updateDisplayParams: bluetoothManager.updateDisplayParams,
                sendContinuous: bluetoothManager.sendContinuous
            )
        default:
            EmptyView()
        }
    }
}
