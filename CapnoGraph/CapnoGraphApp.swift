import SwiftUI
import Combine

@main
struct CapnoGraphApp: App {
    @State var isSplashFinish: Bool = false
    @StateObject private var bluetoothManager = BluetoothManager()
    @StateObject private var appConfigManage = AppConfigManage()
    @State private var cancellables = Set<AnyCancellable>()
    
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
