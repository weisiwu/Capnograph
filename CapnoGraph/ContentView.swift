import SwiftUI

enum ToastType {
    case SUCCESS
    case FAIL
}

struct Toast: View {
    var message: String
    var type: ToastType = ToastType.SUCCESS
    
    var body: some View {
        VStack {
            Spacer()
            HStack {
                Image(type == ToastType.SUCCESS ? "toast_success" : "toast_fail")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .padding(.leading, 12)
                Text(message)
                    .foregroundColor(.white)
                    .font(.system(size: 16))
                    .frame(height: 30)
                    .padding(.trailing, 12)
            }
            .background(Color.black.opacity(0.8))
            .frame(height: 30)
            .cornerRadius(4)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.clear)
        .edgesIgnoringSafeArea(.all)
        .transition(.opacity)
        .animation(.easeInOut, value: true)
    }
}

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
    @Binding var selectedTabIndex: Int
    @Binding var showToast: Bool
    @EnvironmentObject var bluetoothManager: BluetoothManager
    var toggleLoading: (Bool, String) -> Bool
    
    var body: some View {
        TabView(selection: $selectedTabIndex) {
            SearchDeviceListView(selectedPeripheral: nil, showToast: $showToast, selectedTabIndex: $selectedTabIndex, toggleLoading: toggleLoading)
                .tabItem {
                    Image(selectedTabIndex == PageTypes.SearchDeviceList.rawValue ? "tabs_search_active" : "tabs_search")
                    Text("搜索设备").font(.system(size: 20))
                }
                .tag(PageTypes.SearchDeviceList.rawValue)
            
            ResultView()
                .tabItem {
                    Image(selectedTabIndex == PageTypes.Result.rawValue ? "tabs_home_active" : "tabs_home")
                    Text("主页").font(.system(size: 20))
                }
                .tag(PageTypes.Result.rawValue)
            
            ConfigView(toggleLoading: toggleLoading)
                .tabItem {
                    Image(![PageTypes.SearchDeviceList.rawValue, PageTypes.Result.rawValue].contains(selectedTabIndex) ? "tabs_settings_active" : "tabs_settings")
                    Text("设置").font(.system(size: 20))
                }
                .tag(PageTypes.Config.rawValue)
        }
    }
}

// 页面类型
enum PageTypes: Int {
    case NilPage = -1 // 非页面，主要用于处理配置一级页中蓝牙连接、校零等按钮。
    case SearchDeviceList = 0 // 附近设备
    case Result = 1 // 主页
    case Config = 2 // 设置一级页
}

struct BasePageView<Content: View>: View {
    let content: Content
    @State private var selectedTabIndex: Int = PageTypes.Result.rawValue
    @State private var selectedConfigPage: Int?
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

    var title: String {
        get {
            switch selectedTabIndex {
            case PageTypes.SearchDeviceList.rawValue:
                return "CapnoGraph - 附近设备";
            case PageTypes.Result.rawValue:
                return "CapnoGraph";
            case PageTypes.Config.rawValue:
                return "CapnoGraph - 设置";
            default:
                return "CapnoGraph";
            }
        }
    }
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    // 一级页构造函数
    init(systemConfig: PageTypes?, @ViewBuilder content: () -> Content) {
        if let systemConfig {
            self.selectedTabIndex = systemConfig.rawValue
        }
        self.content = content()
    }
    
    var body: some View {
        ZStack {
            VStack {
                Color.white.edgesIgnoringSafeArea(.all)
                    .frame(height: 0)
                content
                ActionsTabView(selectedTabIndex: $selectedTabIndex, showToast: $showToast, toggleLoading: toggleLoading)
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

struct ContentView: View {
    var body: some View {
        BasePageView {}
    }
}

//#Preview {
//    ContentView()
//}
