import SwiftUI

struct AlertConfigView: View {
    var body: some View {
        BasePageView(configPageType: ConfigItemTypes.Alert) {
            Text("警告参数")
        }
    }
}

#Preview {
    AlertConfigView()
}
