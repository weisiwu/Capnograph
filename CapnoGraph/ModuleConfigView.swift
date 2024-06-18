import SwiftUI

struct ModuleConfigView: View {
    var body: some View {
        BasePageView(configPageType: ConfigItemTypes.Module) {
            Text("模块参数")
        }
    }
}
