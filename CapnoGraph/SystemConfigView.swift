import SwiftUI

struct SystemConfigView: View {
    var body: some View {
        BasePageView(configPageType: ConfigItemTypes.System) {
            Text("系统参数")
        }
    }
}
