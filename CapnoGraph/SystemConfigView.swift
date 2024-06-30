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
    @EnvironmentObject var bluetoothManager: BluetoothManager
    
    func getSettingInfoCallback(value: String, type: ISBState84H) {
        if type == ISBState84H.GetSensorPartNumber {
            appConfigManage.ModuleName = value
        } else if type == ISBState84H.GetSerialNumber {
            appConfigManage.serialNumber = value
        } else if type == ISBState84H.GetHardWareRevision {
            appConfigManage.hardwareVersion = value
        } else if type == ISBState84H.GetProductionDate {
            appConfigManage.productionDate = value
        } else if type == ISBState84H.GetModuleName {
            appConfigManage.ModuleName = value
        } else if type == ISBState84H.GetSoftWareRevision {
            appConfigManage.softwareVersion = value
        }
    }

    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                List {
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemLanguage"), type: InfoTypes.Radio)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemFirmwareVersion"), desc: appConfigManage.firmwareVersion)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemHardwareVersion"), desc: appConfigManage.hardwareVersion)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemSoftwareVersion"), desc: appConfigManage.softwareVersion)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemProductionDate"), desc: appConfigManage.productionDate)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemSerialNumber"), desc: appConfigManage.serialNumber)
                    InfoItem(title: appConfigManage.getTextByKey(key: "SystemModuleName"), desc: appConfigManage.ModuleName)
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
        .onAppear {
            // 进入系统设置页后判断是否已经成功获取设备
            if appConfigManage.firmwareVersion == defaultDeviceInfo {
                bluetoothManager.getDeviceInfo(cb: getSettingInfoCallback) // 获取设备信息
            }
        }
    }
}