import SwiftUI

struct AlertConfigView: View {
    var body: some View {
        BasePageView(systemConfig: PageTypes.Alert) {
            Text("警告参数")
        }
    }
}

#Preview {
    AlertConfigView()
}
