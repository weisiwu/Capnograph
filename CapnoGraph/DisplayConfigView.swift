import SwiftUI

struct DisplayConfigView: View {
    var body: some View {
        BasePageView(systemConfig: PageTypes.Display) {
            Text("展示参数")
        }
    }
}
