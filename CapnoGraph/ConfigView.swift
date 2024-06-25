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

//二级配置页View
struct BaseConfigContainerView<Content: View>: View {
    let content: Content
    @State private var selectedTabIndex: Int = PageTypes.Config.rawValue
    @State private var selectedConfigPage: Int
    @State private var isLoading = false
    @State private var loadingText = ""
    @State private var showToast = false
    @State private var toastText = ""
    @StateObject var bluetoothManager = BluetoothManager()
    
    func toggleLoading(show: Bool, text: String) -> Bool  {
        isLoading = show
        loadingText = text
        return show
    }

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
            if showToast {
                if let toastMsg = bluetoothManager.toastMessage {
                    VStack {
                        Spacer()
                        Toast(message: toastMsg)
                    }
                    .animation(.easeInOut, value: showToast)
                }
            }
        }
    }
}

//一级配置View中的配置项目View
struct ConfigItem: View {
    var text: String
    var icon: String
    var configType: ConfigItemTypes
    @Binding var currentConfigType: Int?
    var handleTapGesture: ((Bool, String?) -> Bool)?
    var isLink: Bool {
        get {
            return [ConfigItemTypes.Alert, ConfigItemTypes.Display, ConfigItemTypes.Module, ConfigItemTypes.System].contains(configType)
        }
    }
    @State var isActive: Bool = false

    // 处理点击
    func _handleTapGesture(show: Bool, text: String? = nil) -> Bool? {
        if let result = handleTapGesture?(true, text) {
            if result {
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    _handleTapGesture(show: false)
                }
            }
        }
        return nil
    }
    
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
            .onTapGesture { _handleTapGesture(show: false) }
        }
    }
}

// 一级配置View
struct ConfigView: View {
    var toggleLoading: ((Bool, String) -> Bool)?
    @State var currentConfigType: Int?
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    // TODO: 这里需要调整兼容
    func handleTapGesture(show: Bool, text: String?) -> Bool {
        var loadingText = ""

        if text != nil {
            switch text {
            case "校零":
                loadingText = "正在校零"
            case "关机":
                loadingText = "正在关机"
            default:
                loadingText = ""
            }
        }
        if let toggleLoading {
            if show {
                return toggleLoading(true, loadingText)
            } else {
                return toggleLoading(false, "")
            }
        }
        return false
    }
    
    @ViewBuilder
    var body: some View {
        NavigationView() {
            VStack(spacing: 0) {
                switch currentConfigType {
                case ConfigItemTypes.Alert.rawValue:
                    AlertConfigView()
                case ConfigItemTypes.Display.rawValue:
                    DisplayConfigView()
                case ConfigItemTypes.System.rawValue:
                    SystemConfigView()
                case ConfigItemTypes.Module.rawValue:
                    ModuleConfigView()
                default:
                    List {
                        ConfigItem(
                            text: appConfigManage.getTextByKey(key: "SettingBLConnect"),
                            icon: "setting_icon_bluetooth",
                            configType: ConfigItemTypes.ConnectBlueTooth,
                            currentConfigType: $currentConfigType
                        )
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
                            currentConfigType: $currentConfigType
                        )
                    }
                    .background(Color.white)
                    .listStyle(PlainListStyle())
                    .padding(.bottom, 48)
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
    
    var body: some View {
        switch configType {
        case ConfigItemTypes.Alert.rawValue:
            AlertConfigView()
        case ConfigItemTypes.System.rawValue:
            SystemConfigView()
        case ConfigItemTypes.Module.rawValue:
            ModuleConfigView()
        case ConfigItemTypes.Display.rawValue:
            DisplayConfigView()
        default:
            EmptyView()
        }
    }
}
