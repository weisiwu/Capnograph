import SwiftUI

struct AlertConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("警告参数")
        }
        .navigationTitle("CapnoGraph - 报警设置")
    }
}

#Preview {
    AlertConfigView()
}
