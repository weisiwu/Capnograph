import SwiftUI

func resizeImage(_ image: UIImage, targetSize: CGSize) -> UIImage? {
    UIGraphicsBeginImageContextWithOptions(targetSize, false, 0.0)
    image.draw(in: CGRect(origin: .zero, size: targetSize))
    let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    
    return resizedImage
}

struct SingleSliderInner: UIViewRepresentable {
    @Binding var value: Double
    var minimumValue: Float
    var maximumValue: Float
    var btnSize: CGFloat = 30
    
    func makeUIView(context: Context) -> UISlider {
        let slider = UISlider(frame: .zero)
        slider.minimumValue = minimumValue // 设置最小值
        slider.maximumValue = maximumValue // 设置最大值
        slider.setThumbImage(resizeImage(UIImage(named: "slider_right")!, targetSize: CGSize(width: btnSize, height: btnSize)), for: .normal)
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
    @State var airPressure: Double = UserDefaults.standard.double(forKey: "airPressure")
    @State var asphyxiationTime: Double = UserDefaults.standard.double(forKey: "asphyxiationTime")
    @State var oxygenCompensation: Double = UserDefaults.standard.double(forKey: "oxygenCompensation")
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var appConfigManage: AppConfigManage
    
    var body: some View {
        BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            VStack(alignment: .leading) {

                SingleSlider(
                    title: appConfigManage.getTextByKey(key: "ModuleAirPressure"),
                    minimumValue: 94.6,
                    maximumValue: 104.9,
                    unit: "mmHg",
                    value: $airPressure
                )

                SingleSlider(
                    title: appConfigManage.getTextByKey(key: "ModuleAsphyxiationTime"),
                    minimumValue: 10,
                    maximumValue: 60,
                    value: $asphyxiationTime
                )
                
                SingleSlider(
                    title: appConfigManage.getTextByKey(key: "ModuleOxygenCompensation"),
                    minimumValue: 0,
                    maximumValue: 100,
                    value: $oxygenCompensation
                )
                
                Spacer()
                HStack {
                    Spacer()
                    Button(appConfigManage.getTextByKey(key: "CommonUpdateBtn")) {
                        appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "UpdateSetting")
                        appConfigManage.airPressure = airPressure
                        appConfigManage.asphyxiationTime = asphyxiationTime
                        appConfigManage.oxygenCompensation = oxygenCompensation
                        UserDefaults.standard.set(airPressure, forKey: "airPressure")
                        UserDefaults.standard.set(asphyxiationTime, forKey: "asphyxiationTime")
                        UserDefaults.standard.set(oxygenCompensation, forKey: "oxygenCompensation")
                        UserDefaults.standard.synchronize()
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                            appConfigManage.loadingMessage = ""
                            appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingFinished")
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                appConfigManage.toastMessage = ""
                            }
                        }
                    }
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
        .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleModuleConfig"))")
        .onDisappear {
            presentationMode.wrappedValue.dismiss()
        }
    }
}
