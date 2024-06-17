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
    @State var selectedTabIndex: Int = PageTypes.Result.rawValue
    @State var isCorrectTabIndex: Bool = false // 是否为点击了大于2的tab，正在校正tabIndex
    @Binding var selectedPage: Int // 当前选中page
    @Binding var showToast: Bool
    @EnvironmentObject var bluetoothManager: BluetoothManager
    var toggleLoading: (Bool, String) -> Bool
    
    var body: some View {
        TabView(selection: $selectedTabIndex) {
            SearchDeviceListView(selectedPeripheral: nil, showToast: $showToast, selectedPage: $selectedPage, toggleLoading: toggleLoading)
                .tabItem {
                    Image(selectedTabIndex == PageTypes.SearchDeviceList.rawValue ? "tabs_search_active" : "tabs_search")
                    Text("搜索设备")
                }
                .tag(PageTypes.SearchDeviceList.rawValue)
            
            ResultView()
                .tabItem {
                    Image(selectedTabIndex == PageTypes.Result.rawValue ? "tabs_home_active" : "tabs_home")
                    Text("主页")
                }
                .tag(PageTypes.Result.rawValue)
            
            ConfigView(selectedPage: $selectedPage, toggleLoading: toggleLoading)
                .tabItem {
                    Image(![PageTypes.SearchDeviceList.rawValue, PageTypes.Result.rawValue].contains(selectedTabIndex) ? "tabs_settings_active" : "tabs_settings")
                    Text("设置")
                }
                .tag(PageTypes.Config.rawValue)
        }
        .onChange(of: selectedTabIndex) { newValue in
            if [PageTypes.SearchDeviceList.rawValue, PageTypes.Result.rawValue, PageTypes.Config.rawValue].contains(newValue) && !isCorrectTabIndex {
                selectedPage = newValue
            }
            isCorrectTabIndex = false
            print("当前页面是selectedPage=> \(selectedPage)")
        }
        .onAppear {
            if selectedPage >= PageTypes.Config.rawValue {
                selectedTabIndex = PageTypes.Config.rawValue
                isCorrectTabIndex = true
            }
            print("当前页面是123selectedPage=> \(selectedPage)")
        }
    }
}

// 页面类型
enum PageTypes: Int {
    case SearchDeviceList = 0 // 附近设备
    case Result = 1 // 主页
    case Config = 2 // 设置一级页
    case Alert = 3 // 告警设置二级页
    case System = 4 // 系统设置二级页
    case Module = 5 // 模块设置二级页
    case Display = 6 // 展示设置二级页
}

struct BasePageView<Content: View>: View {
    let content: Content
    @State private var selectedPage = PageTypes.Result.rawValue
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
            switch selectedPage {
            case PageTypes.SearchDeviceList.rawValue:
                return "CapnoGraph - 附近设备";
            case PageTypes.Result.rawValue:
                return "CapnoGraph";
            case PageTypes.Config.rawValue:
                return "CapnoGraph - 设置";
            // 以下为设置的二级页面
            case PageTypes.System.rawValue, PageTypes.Alert.rawValue, PageTypes.Display.rawValue, PageTypes.Module.rawValue:
                return "";
            default:
                return "CapnoGraph";
            }
        }
    }
    
    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    init(systemConfig: PageTypes?, @ViewBuilder content: () -> Content) {
        if let systemConfig {
            self.selectedPage = systemConfig.rawValue
        }
        self.content = content()
    }
    
    var body: some View {
        ZStack {
            NavigationView() {
                VStack {
                    Color.white.edgesIgnoringSafeArea(.all)
                        .frame(height: 0)
                    content
                    ActionsTabView(selectedPage: $selectedPage, showToast: $showToast, toggleLoading: toggleLoading)
                }
                .navigationTitle(title)
                .navigationBarTitleDisplayMode(.inline)
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

#Preview {
    ContentView()
}
