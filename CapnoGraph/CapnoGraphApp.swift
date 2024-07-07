import SwiftUI
import Combine
//import Firebase
//import FirebaseCore
//import FirebaseFirestore
//import FirebaseAuth

// firebase debug 崩溃
//如有缺失依赖，请按照这种方式处理
// https://forums.developer.apple.com/forums/thread/728620
//注入firebase代码
// https://firebase.google.com/docs/ios/setup?hl=zh-cn
// https://console.firebase.google.com/project/capnograph-d3c36/overview?hl=zh-cn
// https://peterfriese.dev/blog/2020/swiftui-new-app-lifecycle-firebase/
@main
struct CapnoGraphApp: App {
    // TODO:(wsw) APP 待添加崩溃监控
//    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @State var isSplashFinish: Bool = false
    @StateObject private var bluetoothManager = BluetoothManager()
    @StateObject private var appConfigManage = AppConfigManage()
    @State private var cancellables = Set<AnyCancellable>()

    // init() {
    //     FirebaseApp.configure()
    //     bluetoothManager.$isBluetoothClose
    //         .sink { newValue in
    //             print("蓝牙状态发生改变为 \(newValue)")
    //         }
    //         .store(in: &cancellables)
    // }
    
    var body: some Scene {
        WindowGroup {
            if isSplashFinish {
                ContentView()
                    .environmentObject(bluetoothManager)
                    .environmentObject(appConfigManage)
                    .onAppear {
                        appConfigManage.listenToBluetoothManager(bluetoothManager: bluetoothManager)
                    }
            } else {
                SplashView()
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            self.isSplashFinish = true
                        }
                    }
            }
        }
    }
}
