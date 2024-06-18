import SwiftUI

struct DisplayConfigView: View {
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            Text("展示参数")
        }
    }
}
