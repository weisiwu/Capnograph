import SwiftUI

struct DisplayConfigView: View {
    var body: some View {
        BasePageView(configPageType: ConfigItemTypes.Display) {
            Text("展示参数")
        }
    }
}
