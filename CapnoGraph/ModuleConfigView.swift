import SwiftUI

struct ModuleConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("模块参数")
        }
    }
}
