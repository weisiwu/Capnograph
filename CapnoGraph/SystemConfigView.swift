import SwiftUI

struct SystemConfigView: View {
    var body: some View {
        BasePageView(systemConfig: PageTypes.System) {
            Text("系统参数")
        }
    }
}
