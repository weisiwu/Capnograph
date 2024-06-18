import SwiftUI

struct SystemConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("系统参数")
        }
    }
}
