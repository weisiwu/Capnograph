import SwiftUI

struct RangeSlider: View {
    @Binding var lowerValue: CGFloat
    @Binding var upperValue: CGFloat
    var range: ClosedRange<CGFloat>
    
    private let trackHeight: CGFloat = 4
    private let knobRadius: CGFloat = 10
    
    var body: some View {
        GeometryReader { geometry in
            self.generateSlider(with: geometry)
        }
        .frame(height: knobRadius * 2)
    }
    
    private func generateSlider(with geometry: GeometryProxy) -> some View {
        let width = geometry.size.width
        let lowerKnobPosition = CGFloat((lowerValue - range.lowerBound) / (range.upperBound - range.lowerBound)) * width
        let upperKnobPosition = CGFloat((upperValue - range.lowerBound) / (range.upperBound - range.lowerBound)) * width
        
        return ZStack(alignment: .leading) {
            Rectangle()
                .frame(height: trackHeight)
                .foregroundColor(.gray)
            Rectangle()
                .frame(width: upperKnobPosition - lowerKnobPosition, height: trackHeight)
                .offset(x: lowerKnobPosition)
                .foregroundColor(.blue)
            Circle()
                .frame(width: knobRadius * 2, height: knobRadius * 2)
                .foregroundColor(.white)
                .shadow(radius: 2)
                .offset(x: lowerKnobPosition)
                .gesture(DragGesture().onChanged({ value in
                    let newLowerValue = min(max(0, value.translation.width + lowerKnobPosition), width) / width
                    lowerValue = range.lowerBound + (range.upperBound - range.lowerBound) * newLowerValue
                }))
            Circle()
                .frame(width: knobRadius * 2, height: knobRadius * 2)
                .foregroundColor(.white)
                .shadow(radius: 2)
                .offset(x: upperKnobPosition - knobRadius * 2)
                .gesture(DragGesture().onChanged({ value in
                    let newUpperValue = min(max(0, value.translation.width + upperKnobPosition - knobRadius * 2), width) / width
                    upperValue = range.lowerBound + (range.upperBound - range.lowerBound) * newUpperValue
                }))
        }
    }
}



struct AlertConfigView: View {
    @State private var etCoLower: CGFloat = 0
    @State private var etCo2Upper: CGFloat = 0
    @State private var rrLower: CGFloat = 0
    @State private var rrUpper: CGFloat = 0

    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            VStack(alignment: .leading) {
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1))
                Text("ETCO2范围").font(.system(size: 18)).fontWeight(.bold)
                RangeSlider(lowerValue: $etCoLower, upperValue: $etCo2Upper, range: 0...1).padding()
                Divider().frame(height: 2).background(Color(red: 0, green: 0, blue: 0).opacity(0.1))
                Text("RR范围").font(.system(size: 18)).fontWeight(.bold)
                RangeSlider(lowerValue: $rrLower, upperValue: $rrUpper, range: 0...1).padding()
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
        .navigationTitle("CapnoGraph - 报警设置")
    }
}

#Preview {
    AlertConfigView()
}
