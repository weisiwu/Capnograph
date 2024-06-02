import SwiftUI

struct ActionsTabView: View {
    @Binding var selectedTab: Int
    
    var body: some View {
        TabView(selection: $selectedTab) {
            SearchDeviceListView()
                .tabItem {
                    Image(selectedTab == 0 ? "tabs_search_active" : "tabs_search")
                    Text("搜索设备")
                }
                .tag(0)
            
            ResultView()
                .tabItem {
                    Image(selectedTab == 1 ? "tabs_home_active" : "tabs_home")
                    Text("主页")
                }
                .tag(1)
            
            SystemConfigView()
                .tabItem {
                    Image(selectedTab >= 2 ? "tabs_settings_active" : "tabs_settings")
                    Text("设置")
                }
                .tag(2)
        }
    }
}

struct BasePageView<Content: View>: View {
    let content: Content
    @State private var selectionIndex = 0

    var title: String {
        get {
            switch selectionIndex {
            case 0:#imageLiteral(resourceName: "simulator_screenshot_C2770F8E-F81B-4191-8F65-39EACD9FA5A9.png")
                return "CapnoGraph - 附近设备";
            case 1:
                return "CapnoGraph";
            case 2:
                return "CapnoGraph - 设置";
            case 3:
                return "CapnoGraph - 报警设置";
            case 4:
                return "CapnoGraph - 系统设置";
            default:
                return "CapnoGraph";
            }
        }
    }
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    init(systemConfig: Int?, @ViewBuilder content: () -> Content) {
        if let systemConfig {
            self.selectionIndex = systemConfig
        }
        self.content = content()
    }
    
    var body: some View {
        NavigationView() {
            VStack {
                content
                ActionsTabView(selectedTab: $selectionIndex)
            }
            .navigationTitle(title)
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct ContentView: View {
    var body: some View {
        BasePageView {}
    }
}

#Preview {
    ContentView()
}
