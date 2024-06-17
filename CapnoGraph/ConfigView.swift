import SwiftUI

struct ConfigItem: View {
    var text: String
    var icon: String
    var isLink: Bool
    var handleTapGesture: ((Bool, String?) -> Bool)?
    @Binding var pageType: Int? // 当前选中page

    init(text: String, icon: String, isLink: Bool) {
        self.text = text
        self.icon = icon
        self.isLink = isLink
    }
    
    init(text: String, icon: String, isLink: Bool, pageType: Int?) {
        self.text = text
        self.icon = icon
        self.isLink = isLink
        self.pageType = pageType
    }
    
//    init(text: String, icon: String, isLink: Bool, handleTapGesture: ((Bool, String?) -> Bool)?) {
//        self.text = text
//        self.icon = icon
//        self.isLink = isLink
//        self.handleTapGesture = handleTapGesture
//    }
    
    init(text: String, icon: String, isLink: Bool, pageType: Int?, handleTapGesture: ((Bool, String?) -> Bool)?) {
        self.text = text
        self.icon = icon
        self.isLink = isLink
        self.pageType = pageType
        self.handleTapGesture = handleTapGesture
    }
    
    func _handleTapGesture(show: Bool, text: String? = nil) -> Bool? {
        if let handleTapGesture {
            return handleTapGesture(show, text)
        }
        return nil
    }

    var body: some View {
        if isLink {
            ZStack(alignment: .leading) {
                NavigationLink(destination: SubConfigContainerView(pageType: $pageType)) {
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
            .onTapGesture {
                if let handleTapGesture {
                    if let isCorrecting = _handleTapGesture(show: true, text: text) {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            _handleTapGesture(show: false)
                        }
                    }
                }
            }
        }
    }
}

struct SubConfigContainerView: View {
    @Binding var pageType: Int? // 当前选中page

    var body: some View {
        switch pageType {
        case PageTypes.Alert.rawValue:
            AlertConfigView()
        case PageTypes.Display.rawValue:
            DisplayConfigView()
        case PageTypes.System.rawValue:
            SystemConfigView()
        case PageTypes.Module.rawValue:
            ModuleConfigView()
        default:
            EmptyView()
        }
    }
}

struct ConfigView: View {
    @Binding var selectedPage: Int // 当前选中page
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
                ConfigItem(text: "蓝牙连接", icon: "setting_icon_bluetooth", isLink: false)
                ConfigItem(text: "校零", icon: "setting_icon_reset", isLink: false, handleTapGesture: handleTapGesture)
                ConfigItem(text: "报警参数", icon: "setting_icon_back", isLink: true, pageType: selectedPage)
                ConfigItem(text: "显示参数", icon: "setting_icon_back", isLink: true, pageType: selectedPage)
                ConfigItem(text: "模块参数", icon: "setting_icon_back", isLink: true, pageType: selectedPage)
                ConfigItem(text: "系统设置", icon: "setting_icon_back", isLink: true, pageType: selectedPage)
                ConfigItem(text: "关机", icon: "setting_icon_shutdown", isLink: false, handleTapGesture: handleTapGesture)
                ConfigItem(text: "屏幕常亮", icon: "setting_icon_lighter", isLink: false)
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
