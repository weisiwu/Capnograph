import SwiftUI

struct SystemConfigView: View {
    @State private var selectedNumber = 0
    let numbers = Array(0...10)
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.System) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1))
                Text("CO2单位").font(.system(size: 18)).fontWeight(.bold)
                Picker("Select CO2单位", selection: $selectedNumber) {
                    ForEach(numbers, id: \.self) { number in
                        Text("\(number)")
                    }
                }
                .pickerStyle(WheelPickerStyle())

                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1))
                Text("CO2 Scale").font(.system(size: 18)).fontWeight(.bold)
                Picker("Select CO2 Scale", selection: $selectedNumber) {
                    ForEach(numbers, id: \.self) { number in
                        Text("\(number)")
                    }
                }
                .pickerStyle(WheelPickerStyle()) // 使 Picker 表现为滚动选择器
                
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1))
                Text("WF Speed").font(.system(size: 18)).fontWeight(.bold)
                Picker("Select WF Speed", selection: $selectedNumber) {
                    ForEach(numbers, id: \.self) { number in
                        Text("\(number)")
                    }
                }
                .pickerStyle(WheelPickerStyle()) // 使 Picker 表现为滚动选择器
                
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
        .navigationTitle("CapnoGraph - 系统设置")
    }
}
