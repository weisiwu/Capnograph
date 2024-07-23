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
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    let tabFontSize: CGFloat = 16
    
    var body: some View {
        TabView(selection: $selectedTabIndex) {
            SearchDeviceListView(selectedPeripheral: nil, selectedTabIndex: $selectedTabIndex)
                .tabItem {
                    Image(selectedTabIndex == PageTypes.SearchDeviceList.rawValue ? "tabs_search_active" : "tabs_search")
                    Text(appConfigManage.getTextByKey(key: "TabSearch"))
                }
                .tag(PageTypes.SearchDeviceList.rawValue)
            
            ResultView()
                .tabItem {
                    Image(selectedTabIndex == PageTypes.Result.rawValue ? "tabs_home_active" : "tabs_home")
                    Text(appConfigManage.getTextByKey(key: "TabMain"))
                }
                .tag(PageTypes.Result.rawValue)
            
            ConfigView(selectedTabIndex: $selectedTabIndex)
                .tabItem {
                    Image(![PageTypes.SearchDeviceList.rawValue, PageTypes.Result.rawValue].contains(selectedTabIndex) ? "tabs_settings_active" : "tabs_settings")
                    Text(appConfigManage.getTextByKey(key: "TabSetting"))
                }
                .tag(PageTypes.Config.rawValue)
        }
        .onAppear {
            // 启动后，设置字体大小
            // https://stackoverflow.com/questions/58353718/swiftui-tabview-tabitem-with-custom-font-does-not-work
            UITabBarItem.appearance().setTitleTextAttributes([NSAttributedString.Key.font: UIFont.systemFont(ofSize: tabFontSize, weight: .medium) ], for: .normal)
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
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    
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
        return ZStack {
            VStack {
                Color.white.edgesIgnoringSafeArea(.all)
                    .frame(height: 0)
                content
                ActionsTabView(selectedTabIndex: $selectedTabIndex)
            }
            .overlay(
                appConfigManage.loadingMessage != "" ? LoadingView(loadingText: appConfigManage.loadingMessage) : nil
            )
            if appConfigManage.toastMessage != "" {
                VStack {
                    Spacer()
                    Toast(message: appConfigManage.toastMessage, type: appConfigManage.toastType)
                }
                .animation(.easeInOut, value: true)
            }
        }
        .onAppear {
            UIApplication.shared.isIdleTimerDisabled = false
        }
        .onChange(of: bluetoothManager.isKeepScreenOn) {
           print("更新了屏幕常亮设置 \(bluetoothManager.isKeepScreenOn)")
            UIApplication.shared.isIdleTimerDisabled = bluetoothManager.isKeepScreenOn
        }
        .onDisappear {
            UIApplication.shared.isIdleTimerDisabled = false
        }
    }
}

struct ContentView: View {
    var body: some View {
        BasePageView {}
    }
}
