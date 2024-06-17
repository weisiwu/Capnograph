import SwiftUI

struct ModuleConfigView: View {
    var body: some View {
        BasePageView(systemConfig: PageTypes.Module) {
            Text("模块参数")
        }
    }
}
