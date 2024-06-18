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

struct ConfigItem: View {
    var text: String
    var icon: String
    var configType: ConfigItemTypes
    var handleTapGesture: ((Bool, String?) -> Bool)?
    var isLink: Bool {
        get {
            return [ConfigItemTypes.Alert, ConfigItemTypes.Display, ConfigItemTypes.Module, ConfigItemTypes.System].contains(configType)
        }
    }
    
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
                HStack(spacing: 0) {
                    Text(text)
                        .font(.system(size: 20))
                        .fontWeight(.light)
                        .padding(.bottom, 2)
                    Spacer()
                    Image(icon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                }
                NavigationLink(destination: SubConfigContainerView(configType: configType.rawValue)) {
                    HStack(spacing: 0) {
                        Text(text)
                            .font(.system(size: 20))
                            .fontWeight(.light)
                            .padding(.bottom, 2)
                        Spacer()
                        Image(icon)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24, height: 24)
                            .padding(.trailing, 8)
                    }
                    .frame(height: 40)
                }
                .opacity(0)
            }
        } else {
            HStack(spacing: 0) {
                Text(text)
                    .font(.system(size: 20))
                    .fontWeight(.light)
                    .padding(.bottom, 2)
                Spacer()
                Image(icon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
            }
            .frame(height: 40)
            .onTapGesture { _handleTapGesture(show: false) }
        }
    }
}

struct SubConfigContainerView: View {
    var configType: Int? // 当前选中page

    var body: some View {
        switch configType {
        case ConfigItemTypes.Alert.rawValue:
            AlertConfigView()
        case ConfigItemTypes.Display.rawValue:
            DisplayConfigView()
        case ConfigItemTypes.System.rawValue:
            SystemConfigView()
        case ConfigItemTypes.Module.rawValue:
            ModuleConfigView()
        default:
            EmptyView()
        }
    }
}

struct ConfigView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    var toggleLoading: ((Bool, String) -> Bool)?

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
        VStack(spacing: 0) {
            List {
                ConfigItem(text: "蓝牙连接", icon: "setting_icon_bluetooth", configType: ConfigItemTypes.ConnectBlueTooth)
                ConfigItem(text: "校零", icon: "setting_icon_reset", configType: ConfigItemTypes.CorrectZero, handleTapGesture: handleTapGesture)
                ConfigItem(text: "报警参数", icon: "setting_icon_back", configType: ConfigItemTypes.Alert)
                ConfigItem(text: "显示参数", icon: "setting_icon_back", configType: ConfigItemTypes.Display)
                ConfigItem(text: "模块参数", icon: "setting_icon_back", configType: ConfigItemTypes.Module)
                ConfigItem(text: "系统设置", icon: "setting_icon_back", configType: ConfigItemTypes.System)
                ConfigItem(text: "关机", icon: "setting_icon_shutdown", configType: ConfigItemTypes.Showdown, handleTapGesture: handleTapGesture)
                ConfigItem(text: "屏幕常亮", icon: "setting_icon_lighter", configType: ConfigItemTypes.Lighter)
            }
            .background(Color.white)
            .listStyle(PlainListStyle())
            .padding(.bottom, 48)
        }
    }

}

//#Preview {
//    SystemConfigView()
//}
