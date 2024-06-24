import SwiftUI

@main
struct CapnoGraphApp: App {
    @State var isSplashFinish: Bool = false
    @StateObject private var bluetoothManager = BluetoothManager()
    @StateObject private var appConfigManage = AppConfigManage()

    var body: some Scene {
        WindowGroup {
            if isSplashFinish {
                ContentView()
                    .environmentObject(bluetoothManager)
                    .environmentObject(appConfigManage)
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
