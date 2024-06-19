import SwiftUI

struct SingleSliderInner: UIViewRepresentable {
    @Binding var value: Double
    var minimumValue: Float
    var maximumValue: Float
    
    func makeUIView(context: Context) -> UISlider {
        let slider = UISlider(frame: .zero)
        slider.minimumValue = minimumValue // 设置最小值
        slider.maximumValue = maximumValue // 设置最大值
        //TODO: 需要调节图片大小
        slider.setThumbImage(UIImage(named: "slider_icon"), for: .normal)
        slider.addTarget(context.coordinator, action: #selector(Coordinator.valueChanged(_:)), for: .valueChanged)
        return slider
    }

    func updateUIView(_ uiView: UISlider, context: Context) {
        uiView.value = Float(value)
        uiView.minimumValue = minimumValue
        uiView.maximumValue = maximumValue
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(value: $value)
    }
    
    class Coordinator: NSObject {
        var value: Binding<Double>
        
        init(value: Binding<Double>) {
            self.value = value
        }
        
        @objc func valueChanged(_ sender: UISlider) {
            self.value.wrappedValue = Double(sender.value)
        }
    }
}

struct SingleSlider: View {
    var title: String
    var minimumValue: Float
    var maximumValue: Float
    var unit: String = "S"
    @Binding var value: Double
    var valueRange: CGFloat {
        CGFloat(maximumValue - minimumValue)
    }
    
    func calculateOffsetX(for value: CGFloat, in geometry: GeometryProxy, with valueRange: CGFloat, minV minimumValue: Float) -> CGFloat {
        let offsetPercent = (value - CGFloat(minimumValue)) / valueRange
        let fullWidth = geometry.size.width
        let centeredX = offsetPercent * fullWidth - 30 * offsetPercent
        return centeredX
    }

    var body: some View {
        GeometryReader { geometry in
            VStack(alignment: .leading) {
                Divider()
                    .frame(height: 2)
                    .background(Color(red: 0, green: 0, blue: 0)
                    .opacity(0.1))
                    .padding(.bottom, 14)
                Text(title)
                    .font(.system(size: 18))
                    .fontWeight(.bold)
                    .padding(0)
                    .padding(.bottom, 30)
                ZStack(alignment: .leading) {
                    Text(value.formatted(.number.precision(.fractionLength(0...1))) + unit)
                        .offset(
                            x: calculateOffsetX(for: CGFloat(value), in: geometry, with: valueRange, minV: minimumValue),
                            y: -30
                        )
                        .animation(.linear, value: value)
                        .frame(alignment: .leading)
                        .font(.system(size: 16))
                        .fontWeight(.thin)
                        .foregroundColor(.black)
                    SingleSliderInner(value: $value, minimumValue: minimumValue, maximumValue: maximumValue)
                        .accentColor(Color(red: 0, green: 206/255, blue: 201/255))
                }
            }
        }
        .frame(height: 150)
    }
}

struct ModuleConfigView: View {
    // TODO: 这里的初始值需要调整
    @State var airPressure: Double = 103
    @State var asphyxiationTime: Double = 30
    @State var oxygenCompensation: Double = 20
    @State private var sliderValue: Double = 0.5
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            VStack(alignment: .leading) {

                SingleSlider(
                    title: "大气压(mmHg)",
                    minimumValue: 94.6,
                    maximumValue: 104.9,
                    unit: "mmHg",
                    value: $airPressure
                )

                SingleSlider(
                    title: "窒息时间(S)",
                    minimumValue: 10,
                    maximumValue: 60,
                    value: $asphyxiationTime
                )
                
                SingleSlider(
                    title: "氧气补偿(%)",
                    minimumValue: 0,
                    maximumValue: 100,
                    value: $oxygenCompensation
                )
                
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

//#Preview {
//    ModuleConfigView()
//}
