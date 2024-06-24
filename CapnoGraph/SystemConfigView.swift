import SwiftUI

//TODO: 二次点击radio没有效果
//TODO: radio需要换选中样式
struct RadioButtonGroup: View {
    let items: [String]
    @Binding var selectedId: String
    
    let callback: (String) -> Void
    
    var body: some View {
        ForEach(items, id: \.self) { item in
            RadioButton(id: item, selectedId: $selectedId, callback: callback)
                .padding(.leading, 20)
        }
    }
}

struct RadioButton: View {
    let id: String
    @Binding var selectedId: String
    let callback: (String) -> Void
    
    var body: some View {
        Button(action: {
            self.selectedId = self.id
            self.callback(self.id)
        }) {
            HStack(alignment: .center, spacing: 10) {
                Image(systemName: self.selectedId == id ? "largecircle.fill.circle" : "circle")
                    .resizable()
                    .frame(width: 24, height: 24)
                    .foregroundColor(self.selectedId == id ? .blue : .gray)
                Text(id)
                    .foregroundColor(Color.black)
            }
        }
        .foregroundColor(Color.white)
    }
}

enum InfoTypes {
    case Text
    case Radio
}

struct InfoItem: View {
    var title: String = ""
    var desc: String = ""
    var type: InfoTypes = InfoTypes.Text
    @State private var selectedOption: String = "中文"
    
    var body: some View {
        HStack {
            Text(title)
                .font(.system(size: 18))
                .fontWeight(.regular)
            Spacer()
            if type == InfoTypes.Text {
                Text(desc)
                    .font(.system(size: 14))
                    .fontWeight(.thin)
                    .foregroundStyle(Color(red: 61/255, green: 61/255, blue: 61/255))

            } else if type == InfoTypes.Radio {
                RadioButtonGroup(items: ["中文", "English"], selectedId: $selectedOption) { selected in
                    print("Selected option: \(selected)")
                }
            }
        }
        .frame(height: 40)
    }
}

struct SystemConfigView: View {
    @EnvironmentObject var appConfigManage: AppConfigManage
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                List {
                    InfoItem(title: "语言", type: InfoTypes.Radio)
                    InfoItem(title: "固件版本", desc: "V1.0.0")
                    InfoItem(title: "硬件版本", desc: "V1.0.1")
                    InfoItem(title: "软件版本", desc: "V1.0.2")
                    InfoItem(title: "生产日期", desc: "2024年05月13日17:56:47")
                    InfoItem(title: "序列号", desc: "FKUXP72K0P094")
                    InfoItem(title: "模块名称", desc: "CapnoGraph")
                }
                .background(Color.white)
                .listStyle(PlainListStyle())
                .padding(.bottom, 48)
                Spacer()
            }
        }
        .background(Color.white)
        .listStyle(PlainListStyle())
        .navigationTitle("CapnoGraph - 系统设置")
    }
}


//#Preview {
//    SystemConfigView()
//}
