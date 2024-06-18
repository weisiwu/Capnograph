import SwiftUI

struct ModuleConfigView: View {
    @State var airPressure: Double = 0.0
    @State var asphyxiationTime: Double = 30
    @State var oxygenCompensation: Double = 20
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("大气压(mmHg)").font(.system(size: 18)).fontWeight(.bold).padding(0)
                Slider(
                    value: $airPressure,
                    in: 94.6...104.9,
                    step: 0.5
//                    onEditingChanged: { editing in
//                        isEditing = editing
//                    }
                )
                    .accentColor(Color(red: 0, green: 206/255, blue: 201/255))
                Text(airPressure.formatted(.number.precision(.fractionLength(0...2))))
                    .foregroundColor(.blue)

                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("窒息时间(S)").font(.system(size: 18)).fontWeight(.bold)
                Slider(
                    value: $asphyxiationTime,
                    in: 10...60
//                    onEditingChanged: { editing in
//                        isEditing = editing
//                    }
                )
                    .accentColor(Color(red: 0, green: 206/255, blue: 201/255))
                Text(asphyxiationTime.formatted(.number.precision(.fractionLength(0...0))))
                    .foregroundColor(.blue)
                
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1)).padding(.bottom, 14)
                Text("氧气补偿(%)").font(.system(size: 18)).fontWeight(.bold)
                Slider(
                    value: $oxygenCompensation,
                    in: 0...100
//                    onEditingChanged: { editing in
//                        isEditing = editing
//                    }
                )
                    .accentColor(Color(red: 0, green: 206/255, blue: 201/255))
                Text(oxygenCompensation.formatted(.number.precision(.fractionLength(0...0))))
                    .foregroundColor(.blue)
                
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
        .navigationTitle("CapnoGraph - 模块设置")
    }
}
