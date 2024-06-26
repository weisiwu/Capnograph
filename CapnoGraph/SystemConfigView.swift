import SwiftUI

struct RadioButtonGroup: View {
    let items: [Languages]
    
    var body: some View {
        ForEach(items, id: \.self) { item in
            RadioButton(id: item).padding(.leading, 20)
        }
    }
}

struct RadioButton: View {
    let id: Languages
    @EnvironmentObject var appConfigManage: AppConfigManage
    
    var body: some View {
        // TODO: 这里的action事件触发有问题
        Button(action: {}) {
            HStack(alignment: .center, spacing: 10) {
                Image(appConfigManage.language == id ? "radio_input" : "radio_input_empty")
                    .resizable()
                    .frame(width: 20, height: 20)
                    .foregroundColor(appConfigManage.language == id ? .blue : .gray)
                Text(id.rawValue)
                    .foregroundColor(Color.black)
            }
        }
        .foregroundColor(Color.white)
        .onTapGesture {
            appConfigManage.language = id
        }
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
    
    var body: some View {
        HStack {
            Text(title)
                .font(.system(size: 16))
                .fontWeight(.regular)
            Spacer()
            if type == InfoTypes.Text {
                Text(desc)
                    .font(.system(size: 16))
                    .fontWeight(.thin)
                    .foregroundStyle(Color(red: 61/255, green: 61/255, blue: 61/255))

            } else if type == InfoTypes.Radio {
                RadioButtonGroup(items: [.Chinese, .English])
            }
        }
        .frame(height: 40)
    }
}

struct SystemConfigView: View {
    @Environment(\.presentationMode) var presentationMode
    @State var selectedOption: Languages = Languages.Chinese
    @EnvironmentObject var appConfigManage: AppConfigManage
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                List {
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemLanguage"), type: InfoTypes.Radio)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemFirmwareVersion"), desc: "V1.0.0")
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemHardwareVersion"), desc: "V1.0.1")
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemSoftwareVersion"), desc: "V1.0.2")
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemProductionDate"), desc: "2024年05月13日17:56:47")
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemSerialNumber"), desc: "FKUXP72K0P094")
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemModuleName"), desc: "CapnoGraph")
                }
                .background(Color.white)
                .listStyle(PlainListStyle())
                .padding(.bottom, 48)
                Spacer()
            }
        }
        .background(Color.white)
        .listStyle(PlainListStyle())
        .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleSystemSetting"))")
        .onDisappear {
            presentationMode.wrappedValue.dismiss()
        }
    }
}

//#Preview {
//    SystemConfigView()
//}
