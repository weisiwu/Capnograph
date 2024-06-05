import SwiftUI

@main
struct CapnoGraphApp: App {
    @State var isSplashFinish: Bool = false

    var body: some Scene {
        WindowGroup {
            if isSplashFinish {
                ContentView()
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
