import SwiftUI

struct DisplayConfigView: View {
    @State private var CO2Unit: String?
    @State private var CO2Scale: Double?
    @State private var WFSpeed: Int?
    @EnvironmentObject var appConfigManage: AppConfigManage
    let CO2Units: [CO2UnitType] = [CO2UnitType.KPa, CO2UnitType.Percentage, CO2UnitType.mmHg]
    let CO2Scales: [CO2ScaleEnum] = [CO2ScaleEnum.Small, CO2ScaleEnum.Middle, CO2ScaleEnum.Large]
    let WFSpeeds: [WFSpeedEnum] = [WFSpeedEnum.One, WFSpeedEnum.Two, WFSpeedEnum.Three, WFSpeedEnum.Four]

    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("CO2单位").font(.system(size: 18)).fontWeight(.bold).padding(0)
                Picker("Select CO2单位", selection: $CO2Unit) {
                    ForEach(CO2Units, id: \.self) { unit in
                        Text(unit.rawValue)
                            .frame(height: 30)
                            .font(.system(size: 14))
                    }
                }
                .pickerStyle(WheelPickerStyle())
                .frame(height: 110)

                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("CO2 Scale").font(.system(size: 18)).fontWeight(.bold)
                Picker("Select CO2 Scale", selection: $CO2Scale) {
                    ForEach(CO2Scales, id: \.self) { scale in
                        Text(scale.rawValue.formatted(.number.precision(.fractionLength(0...2))))
                            .frame(height: 30)
                            .font(.system(size: 14))
                    }
                }
                .pickerStyle(WheelPickerStyle())
                .frame(height: 110)
                
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("WF Speed").font(.system(size: 18)).fontWeight(.bold)
                Picker("Select WF Speed", selection: $WFSpeed) {
                    ForEach(WFSpeeds, id: \.self) { speed in
                        Text("\(speed.rawValue)mm/S")
                            .frame(height: 30)
                            .font(.system(size: 14))
                    }
                }
                .pickerStyle(WheelPickerStyle())
                .frame(height: 110)
                
                Spacer()
                HStack {
                    Spacer()
                    Button("更新") {}
                        .frame(width: 68, height: 43)
                        .background(Color(red: 224/255, green: 234/255, blue: 1))
                        .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                        .cornerRadius(22)
                    Spacer().frame(width: 78)
                    Button("设置") {}
                        .frame(width: 68, height: 43)
                        .background(Color(red: 224/255, green: 234/255, blue: 1))
                        .foregroundColor(Color(red: 22/255, green: 93/255, blue: 1))
                        .cornerRadius(22)
                    Spacer()
                }
                .padding(.bottom, 20)
            }
            .padding()
        }
        .background(Color.white)
        .listStyle(PlainListStyle())
        .padding()
        .navigationTitle("CapnoGraph - 显示设置")
    }
}

//#Preview {
//    DisplayConfigView()
//}
