import SwiftUI

struct ModuleConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("模块参数")
        }
        .navigationTitle("CapnoGraph - 模块设置")
    }
}
