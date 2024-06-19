import SwiftUI

struct DisplayConfigView: View {
    @State private var CO2Unit: String?
    @State private var CO2Scale: Double?
    @State private var WFSpeed: Int?
    let CO2Units = ["KPa", "%", "mmHg"]
    let CO2Scales = [6.7, 8, 10]
    let WFSpeeds = [1,2,3,4]

    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("CO2单位").font(.system(size: 18)).fontWeight(.bold).padding(0)
                Picker("Select CO2单位", selection: $CO2Unit) {
                    ForEach(CO2Units, id: \.self) { unit in
                        Text(unit)
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
                        Text(scale.formatted(.number.precision(.fractionLength(0...2))))
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
                        Text("\(speed)mm/S")
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

#Preview {
    DisplayConfigView()
}
