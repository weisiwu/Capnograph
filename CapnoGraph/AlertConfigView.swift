import SwiftUI

struct AlertConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("警告参数")
        }
    }
}

#Preview {
    AlertConfigView()
}
