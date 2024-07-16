import SwiftUI

struct DisplayConfigView: View {
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var appConfigManage: AppConfigManage
    @EnvironmentObject var bluetoothManager: BluetoothManager
    let CO2Units: [CO2UnitType] = [.KPa, .Percentage, .mmHg]
    let WFSpeeds: [WFSpeedEnum] = [.One, .Two, .Four]

    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text(appConfigManage.getTextByKey(key: "DisplayCO2Unit")).font(.system(size: 18)).fontWeight(.bold).padding(0)

                Picker(appConfigManage.getTextByKey(key: "DisplayCO2Unit"), selection: $bluetoothManager.CO2Unit) {
                    ForEach(CO2Units, id: \.self) { unit in
                        Text(unit.rawValue)
                            .frame(height: 30)
                            .font(.system(size: 14))
                    }
                }
                    .pickerStyle(WheelPickerStyle())
                    .frame(height: 110)
                    .frame(height: 110)

                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text(appConfigManage.getTextByKey(key: "DisplayCO2Scale")).font(.system(size: 18)).fontWeight(.bold)

                Picker(appConfigManage.getTextByKey(key: "DisplayCO2Scale"), selection: $bluetoothManager.CO2Scale) {
                    ForEach(bluetoothManager.CO2Scales, id: \.self) { scale in
                        Text(scale.rawValue.formatted(.number.precision(.fractionLength(0...2))))
                            .frame(height: 30)
                            .font(.system(size: 14))
                    }
                }
                    .pickerStyle(WheelPickerStyle())
                    .frame(height: 110)
                
                Spacer()
                HStack {
                    Spacer()
                    Button(appConfigManage.getTextByKey(key: "CommonUpdateBtn")) {
                        appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "UpdateSetting")
                        if let isPass = bluetoothManager.checkBluetoothStatus(),
                            bluetoothManager.connectedPeripheral == nil,
                            !isPass {
                            bluetoothManager.updateCO2Unit {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    appConfigManage.loadingMessage = ""
                                    appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingFail")
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                        appConfigManage.toastMessage = ""
                                    }
                                }
                            }
                            return
                        }
                        UserDefaults.standard.set(bluetoothManager.CO2Unit.rawValue, forKey: "CO2Unit")
                        UserDefaults.standard.set(bluetoothManager.CO2Scale.rawValue, forKey: "CO2Scale")
                        bluetoothManager.updateCO2Unit {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                appConfigManage.loadingMessage = ""
                                appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingFinished")
                                appConfigManage.toastType = .SUCCESS
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                    appConfigManage.toastMessage = ""
                                }
                            }
                        }
                    }
                        .frame(width: 120, height: 40)
                        .background(Color(red: 224/255, green: 234/255, blue: 1))
                        .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                        .cornerRadius(20)
                    Spacer()
                }
                .padding(.bottom, 20)
            }
            .padding()
        }
        .background(Color.white)
        .listStyle(PlainListStyle())
        .padding()
        .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleDisplayConfig"))")
        .onDisappear {
            presentationMode.wrappedValue.dismiss()
        }
    }
}
