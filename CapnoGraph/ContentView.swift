import SwiftUI

struct LoadingView: View {
    let loadingText: String?
    
    var body: some View {
        if loadingText != nil {
            ZStack {
                Color.black.opacity(0.4)
                    .edgesIgnoringSafeArea(.all)
                
                ProgressView(value: 70.1, total: 100.0)
                    .scaleEffect(1.5, anchor: .center) // 放大进度指示器
                    .progressViewStyle(CircularProgressViewStyle(tint: .white))

                Text(loadingText!)
                    .padding(.top, 60)
                    .font(.system(size: 16))
                    .fontWeight(.bold)
                    .foregroundColor(.white)
            }

        } else {
            EmptyView()
        }
    }
}


struct ActionsTabView: View {
    @Binding var selectedTab: Int
    var toggleLoading: (Bool, String) -> Bool
    
    var body: some View {
        TabView(selection: $selectedTab) {
            SearchDeviceListView(toggleLoading: toggleLoading)
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
            
            SystemConfigView(toggleLoading: toggleLoading)
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
    @State private var selectionIndex = 1
    @State private var isLoading = false
    @State private var loadingText = ""
    
    func toggleLoading(show: Bool, text: String) -> Bool  {
        isLoading = show
        loadingText = text
        return show
    }

    var title: String {
        get {
            switch selectionIndex {
            case 0:
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
                ActionsTabView(selectedTab: $selectionIndex, toggleLoading: toggleLoading)
            }
            .navigationTitle(title)
            .navigationBarTitleDisplayMode(.inline)
        }
        .overlay(
            isLoading ? LoadingView(loadingText: loadingText) : nil
        )
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
