import SwiftUI
import Combine
import CoreBluetooth

class CO2DisplayParamModel: ObservableObject {
    @Published var CO2Unit: CO2UnitType = .mmHg
    @Published var CO2Scale: CO2ScaleEnum = .mmHg_Middle
    
    init(initCO2Unit: CO2UnitType, initCO2Scale: CO2ScaleEnum) {
        self.CO2Unit = initCO2Unit
        self.CO2Scale = initCO2Scale
    }
}

struct DisplayConfigView: View {
    // 图标展示的实时单位、范围、速度
    var initCO2Unit: CO2UnitType
    var initCO2Scale: CO2ScaleEnum
    @State var CO2Scales: [CO2ScaleEnum] = []
    @State var connectedPeripheral: CBPeripheral?
    var checkBluetoothStatus: () -> Bool?
    var updateCO2Unit: (@escaping () -> Void) -> ()
    var updateDisplayParams: ((CO2UnitType, CO2ScaleEnum) -> Void)
    let CO2Units: [CO2UnitType] = [.KPa, .Percentage, .mmHg]
    let WFSpeeds: [WFSpeedEnum] = [.One, .Two, .Four]
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var appConfigManage: AppConfigManage
    @StateObject var paramsModel: CO2DisplayParamModel
    @State var isAppeared: Bool = false
    
    init(
        initCO2Unit: CO2UnitType,
        initCO2Scale: CO2ScaleEnum,
        CO2Scales: [CO2ScaleEnum],
        connectedPeripheral: CBPeripheral?,
        checkBluetoothStatus: @escaping () -> Bool?,
        updateCO2Unit: @escaping (@escaping () -> Void) -> (),
        updateDisplayParams: @escaping ((CO2UnitType, CO2ScaleEnum) -> Void)
    ) {
        self.initCO2Unit = initCO2Unit
        self.initCO2Scale = initCO2Scale
        self.CO2Scales = CO2Scales
        self.connectedPeripheral = connectedPeripheral
        self.checkBluetoothStatus = checkBluetoothStatus
        self.updateCO2Unit = updateCO2Unit
        self.updateDisplayParams = updateDisplayParams
        _paramsModel = StateObject(wrappedValue: CO2DisplayParamModel(initCO2Unit: initCO2Unit, initCO2Scale: initCO2Scale))
    }
    
    var body: some View {
        return BaseConfigContainerView(configType: ConfigItemTypes.System) {
                VStack(alignment: .leading) {
                    Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                    Text(appConfigManage.getTextByKey(key: "DisplayCO2Unit")).font(.system(size: 18)).fontWeight(.bold).padding(0)

                    Picker(appConfigManage.getTextByKey(key: "DisplayCO2Unit"), selection: $paramsModel.CO2Unit) {
                        ForEach(CO2Units, id: \.self) { unit in
                            Text(unit.rawValue)
                                .frame(height: 30)
                                .font(.system(size: 14))
                        }
                    }
                    .pickerStyle(.wheel)
                    .frame(height: 110)

                    Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                    Text(appConfigManage.getTextByKey(key: "DisplayCO2Scale")).font(.system(size: 18)).fontWeight(.bold)

                    Picker(appConfigManage.getTextByKey(key: "DisplayCO2Scale"), selection: $paramsModel.CO2Scale) {
                        ForEach(CO2Scales, id: \.self) { scale in
                            Text(scale.rawValue.formatted(.number.precision(.fractionLength(0...2))))
                                .frame(height: 30)
                                .font(.system(size: 14))
                        }
                    }
                    .pickerStyle(.wheel)
                    .frame(height: 110)
                    
                    Spacer()
                    HStack {
                        Spacer()
                        Button(appConfigManage.getTextByKey(key: "CommonUpdateBtn")) {
                            appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "UpdateSetting")
                            if let isPass = checkBluetoothStatus(),
                                connectedPeripheral == nil,
                                !isPass {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                    appConfigManage.loadingMessage = ""
                                    appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingFail")
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                        appConfigManage.toastMessage = ""
                                    }
                                }
                                return
                            }
                            UserDefaults.standard.set(paramsModel.CO2Unit.rawValue, forKey: "CO2Unit")
                            UserDefaults.standard.set(paramsModel.CO2Scale.rawValue, forKey: "CO2Scale")
                            updateDisplayParams(paramsModel.CO2Unit, paramsModel.CO2Scale)
                            updateCO2Unit{
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
            .onReceive(paramsModel.$CO2Unit) { newValue in
                if !isAppeared {
                    return
                }
                switch newValue {
                    case .mmHg:
                        paramsModel.CO2Scale = .mmHg_Small
                        CO2Scales = [.mmHg_Small, .mmHg_Middle, .mmHg_Large]
                    case .Percentage:
                        paramsModel.CO2Scale = .percentage_Small
                        CO2Scales = [.percentage_Small, .percentage_Middle, .percentage_Large]
                    case .KPa:
                        paramsModel.CO2Scale = .KPa_Small
                        CO2Scales = [.KPa_Small, .KPa_Middle, .KPa_Large]
                }
            }
            .onAppear {
                isAppeared = true
            }
            .onDisappear {
                isAppeared = false
            }
    }
}
