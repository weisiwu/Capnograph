import SwiftUI

struct ConfigItem: View {
    var text: String
    var icon: String
    var isLink: Bool

    var body: some View {
        if isLink {
            ZStack(alignment: .leading) {
                NavigationLink(destination: AlertConfigView()) {
                    HStack(spacing: 0) {
                        Text(text)
                            .font(.system(size: 20))
                            .fontWeight(.light)
                            .padding(.bottom, 2)
                        Spacer()
                        Image(icon)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24, height: 24)
                            .padding(.trailing, 8)
                    }
                    .frame(height: 40)
                }
                .opacity(0)
                HStack(spacing: 0) {
                    Text(text)
                        .font(.system(size: 20))
                        .fontWeight(.light)
                        .padding(.bottom, 2)
                    Spacer()
                    Image(icon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                }
            }
        } else {
            HStack(spacing: 0) {
                Text(text)
                    .font(.system(size: 20))
                    .fontWeight(.light)
                    .padding(.bottom, 2)
                Spacer()
                Image(icon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
            }
            .frame(height: 40)
        }
    }
}

struct SystemConfigView: View {
    var body: some View {
        VStack(spacing: 0) {
            List {
                ConfigItem(text: "蓝牙连接", icon: "setting_icon_bluetooth", isLink: false)
                ConfigItem(text: "校零", icon: "setting_icon_reset", isLink: false)
                ConfigItem(text: "报警参数", icon: "setting_icon_back", isLink: true)
                ConfigItem(text: "显示参数", icon: "setting_icon_back", isLink: true)
                ConfigItem(text: "模块参数", icon: "setting_icon_back", isLink: true)
                ConfigItem(text: "系统设置", icon: "setting_icon_back", isLink: true)
                ConfigItem(text: "关机", icon: "setting_icon_shutdown", isLink: false)
                ConfigItem(text: "屏幕常亮", icon: "setting_icon_lighter", isLink: false)
            }
            .background(Color.white)
            .listStyle(PlainListStyle())
            .padding(.bottom, 48)
        }
    }

}

#Preview {
    SystemConfigView()
}
