import SwiftUI

// TODO: 两个滑块的字太近，会导致互相遮盖
// TODO: 右滑块能滑动到左滑块左边，需要限制位置
struct RangeSlider: View {
    var title: String
    @Binding var lowerValue: CGFloat // 最小值
    @Binding var upperValue: CGFloat // 最大值
    var range: ClosedRange<CGFloat> // 可选值范围
    var unit: String = ""
    var btnSize: CGFloat = 30
    // 当前选中范围
    var currentRange: CGFloat {
        CGFloat(range.upperBound - range.lowerBound)
    }
    
    private let trackHeight: CGFloat = 4
    private let knobRadius: CGFloat = 10
    
    var body: some View {
        VStack(alignment: .leading) {
            Divider()
                .frame(height: 2)
                .background(Color(red: 0, green: 0, blue: 0)
                .opacity(0.1))
                .padding(.bottom, 12)
            Text(title)
                .font(.system(size: 18))
                .fontWeight(.bold)
                .padding(.bottom, 32)
            GeometryReader { geometry in
                self.generateSlider(with: geometry)
            }
            .frame(height: knobRadius * 2)
        }
        .frame(height: 150)
    }
    
    func calculateOffsetX(for value: CGFloat, in geometry: GeometryProxy, with valueRange: CGFloat, minV minimumValue: Float) -> CGFloat {
        let offsetPercent = (value - CGFloat(minimumValue)) / valueRange
        let fullWidth = geometry.size.width
        let centeredX = offsetPercent * fullWidth - 15 * offsetPercent
        return centeredX
    }
    
    private func generateSlider(with geometry: GeometryProxy) -> some View {
        let width = geometry.size.width
        let lowerKnobPosition = CGFloat((lowerValue - range.lowerBound) / range.upperBound) * width
        let upperKnobPosition = CGFloat((upperValue - range.lowerBound) / range.upperBound) * width
        
        return ZStack(alignment: .leading) {
            // 背景灰度条
            Rectangle()
                .frame(height: trackHeight)
                .foregroundColor(Color(red: 233/255, green: 230/255, blue: 233/255))
            
            // 选中范围条
            Rectangle()
                .frame(width: upperKnobPosition - lowerKnobPosition, height: trackHeight)
                .offset(x: lowerKnobPosition)
                .foregroundColor(Color(red: 0, green: 206/255, blue: 201/255))
            
            // 最小值
            Text(Double(lowerValue).formatted(.number.precision(.fractionLength(0...0))))
                .offset(
                    x: calculateOffsetX(for: CGFloat(lowerValue), in: geometry, with: currentRange, minV: Float(range.lowerBound)),
                    y: -30
                )
                .animation(.easeInOut, value: lowerValue)
                .frame(alignment: .leading)
                .font(.system(size: 16))
                .fontWeight(.regular)
                .foregroundColor(.black)
            Circle()
                .opacity(0)
                .frame(width: btnSize, height: btnSize)
                .shadow(radius: 2)
                .offset(x: lowerKnobPosition)
                .background(
                    Image("slider_left")
                        .resizable()
                        .scaledToFill()
                        .clipped()
                )
                .clipShape(Circle())
                .contentShape(Circle())
                .gesture(DragGesture().onChanged({ value in
                    let newLowerValue = min(max(0, value.translation.width + lowerKnobPosition), width) / width
                    lowerValue = range.lowerBound + (range.upperBound - range.lowerBound) * newLowerValue
                }))
                .offset(x: lowerKnobPosition)
            
            // 最大值
            Text(Double(upperValue).formatted(.number.precision(.fractionLength(0...0))))
                .offset(
                    x: calculateOffsetX(for: CGFloat(upperValue), in: geometry, with: currentRange, minV: Float(range.lowerBound)),
                    y: -30
                )
                .animation(.easeInOut, value: upperValue)
                .frame(alignment: .leading)
                .font(.system(size: 16))
                .fontWeight(.regular)
                .foregroundColor(.black)

            Circle()
                .opacity(0)
                .frame(width: btnSize, height: btnSize)
                .shadow(radius: 2)
                .offset(x: upperKnobPosition - knobRadius * 2)
                .background(
                    Image("slider_right")
                        .resizable()
                        .scaledToFill()
                        .clipped()
                )
                .clipShape(Circle())
                .contentShape(Circle())
                .gesture(DragGesture().onChanged({ value in
                    let newUpperValue = min(max(0, value.translation.width + upperKnobPosition - knobRadius * 2), width) / width
                    upperValue = range.lowerBound + (range.upperBound - range.lowerBound) * newUpperValue
                }))
                .offset(x: upperKnobPosition - knobRadius * 2)
        }
    }
}

struct AlertConfigView: View {
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var appConfigManage: AppConfigManage
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @State var etCo2Lower: CGFloat = 0
    @State var etCo2Upper: CGFloat = 0
    @State var rrLower: CGFloat = 0
    @State var rrUpper: CGFloat = 0
    
    var body: some View {
        return BaseConfigContainerView(configType: ConfigItemTypes.Alert) {
            VStack(alignment: .leading) {
                RangeSlider(
                    title: "\(appConfigManage.getTextByKey(key: "AlertETCO2"))(\(bluetoothManager.CO2Unit.rawValue))",
                    lowerValue: $etCo2Lower,
                    upperValue: $etCo2Upper,
                    range: bluetoothManager.etco2Min...bluetoothManager.etco2Max,
                    unit: bluetoothManager.CO2Unit.rawValue
                )
                
                RangeSlider(
                    title: "\(appConfigManage.getTextByKey(key: "AlertRR"))(bpm)",
                    lowerValue: $rrLower,
                    upperValue: $rrUpper,
                    range: bluetoothManager.rrMin...bluetoothManager.rrMax
                )

                Spacer()
                HStack {
                    Spacer()
                    Button(appConfigManage.getTextByKey(key: "CommonUpdateBtn")) {
                        appConfigManage.loadingMessage = appConfigManage.getTextByKey(key: "UpdateSetting")

                        let isRangeValid = bluetoothManager.checkAlertRangeValid()
                        if !isRangeValid {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                appConfigManage.loadingMessage = ""
                                appConfigManage.toastType = .FAIL
                                appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingException")
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                    appConfigManage.toastMessage = ""
                                }
                            }
                            return
                        }
                        
                        if let isPass = bluetoothManager.checkBluetoothStatus(), !isPass {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                appConfigManage.loadingMessage = ""
                                appConfigManage.toastType = .FAIL
                                appConfigManage.toastMessage = appConfigManage.getTextByKey(key: "UpdateSettingFail")
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                    appConfigManage.toastMessage = ""
                                }
                            }
                            return
                        }
                        UserDefaults.standard.set(etCo2Lower, forKey: "etCo2Lower")
                        UserDefaults.standard.set(etCo2Upper, forKey: "etCo2Upper")
                        UserDefaults.standard.set(rrLower, forKey: "rrLower")
                        UserDefaults.standard.set(rrUpper, forKey: "rrUpper")
                        UserDefaults.standard.synchronize()
                        // 修改报警范围
                        let isSuccess = bluetoothManager.updateAlertRange(
                            co2Low: etCo2Lower,
                            co2Up: etCo2Upper,
                            rrLow: rrLower,
                            rrUp: rrUpper
                        )
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                            appConfigManage.loadingMessage = ""
                            appConfigManage.toastMessage = isSuccess
                                ? appConfigManage.getTextByKey(key: "UpdateSettingFinished")
                                : appConfigManage.getTextByKey(key: "UpdateSettingFail")
                            appConfigManage.toastType = isSuccess ? .SUCCESS : .FAIL
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                appConfigManage.toastMessage = ""
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
        .navigationTitle("CapnoGraph\(appConfigManage.getTextByKey(key: "TitleAlertParams"))")
        .onAppear {
            etCo2Lower = bluetoothManager.etCo2Lower
            etCo2Upper = bluetoothManager.etCo2Upper
            rrLower = bluetoothManager.rrLower
            rrUpper = bluetoothManager.rrUpper
        }
        .onDisappear {
            presentationMode.wrappedValue.dismiss()
        }
    }
}
