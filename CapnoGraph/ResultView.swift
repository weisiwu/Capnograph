import SwiftUI
import Charts
import Foundation

// 最多展示的横向点数量，每10ms接收到一帧数据，横坐标展示20s的波形图，则共1000个横坐标
let maxXPoints: Int = 400
let xPointStep: Int = 100 // 步长，每100取一个坐标点
let unRealValue: Float = 0 // 初始线表时，所有点的初始值

struct DataPoint: Identifiable {
    let id = UUID()
    let value: Float
}

struct LineChartView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    var body: some View {
        return Chart {
            ForEach(Array(bluetoothManager.receivedCO2WavedData.enumerated()), id: \.offset) { index, point in
                LineMark(
                    x: .value("Index", index),
                    y: .value("Value", point.value)
                )
                .interpolationMethod(.cardinal)
            }
        }
        .chartXAxis {
            // not show last axis marks
            // https://stackoverflow.com/questions/74240487/swift-charts-will-not-display-the-last-x-axis-axisvaluelabel-with-axismarks
            AxisMarks(preset: .aligned, values: Array(stride(from: 0, through: maxXPoints, by: xPointStep))) { value in
                AxisValueLabel {
                    if let intValue = value.as(Int.self) {
                        Text("\(intValue / 100)\(appConfigManage.getTextByKey(key: "MainLineCharUnit"))")
                    }
                }
            }
        }
        .chartYScale(domain: 0...Double(bluetoothManager.CO2Scale.rawValue))
        .chartYAxis {
            AxisMarks(
                preset: .aligned,
                position: .leading,
                values: generateYAxis(scale: bluetoothManager.CO2Scale)
            )
        }
        .frame(height: 300)
        .padding()
    }
}

// 根据最大刻度，生成y轴坐标
func generateYAxis(scale: CO2ScaleEnum) -> [Double] {
    switch scale {
        case .mmHg_Small:
            return [0, 10, 20, 30, 40, 50]
        case .mmHg_Middle:
            return [0, 10, 20, 30, 40, 50, 60]
        case .mmHg_Large:
            return [0, 10, 20, 30, 40, 50, 60, 70, 75]
        case .KPa_Small:
            return [0, 1, 2, 3, 4, 5, 6, 6.7]
        case .KPa_Middle:
            return [0, 1, 2, 3, 4, 5, 6, 7, 8]
        case .KPa_Large:
            return [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        case .percentage_Small:
            return [0, 1, 2, 3, 4, 5, 6, 6.6]
        case .percentage_Middle:
            return [0, 1, 2, 3, 4, 5, 6, 7, 7.9]
        case .percentage_Large:
            return [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9.9]
    }
}

struct TableView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    var body: some View {
        HStack(spacing:0) {
            Text(appConfigManage.getTextByKey(key: "MainDeviceName"))
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.connectedPeripheral?.name ?? appConfigManage.getTextByKey(key: "MainUnknownName"))
                .font(.system(size: 16))
                .fontWeight(.thin)
                .foregroundColor(Color(red: 29/255, green: 33/255, blue: 41/255))
        }
        .frame(height: 30)
        .padding(.leading, 28)
        .padding(.trailing, 28)
        
        HStack(spacing:0) {
            Text(appConfigManage.getTextByKey(key: "MainDeviceID"))
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.connectedPeripheral?.identifier.uuidString ?? "--")
                .font(.system(size: 16))
                .fontWeight(.thin)
                .foregroundColor(Color(red: 29/255, green: 33/255, blue: 41/255))
        }
        .frame(height: 30)
        .padding(.leading, 28)
        .padding(.trailing, 28)
        
        HStack(spacing:0) {
            Text(appConfigManage.getTextByKey(key: "MainPR"))
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.RespiratoryRate == 0 ? "--bpm/min" : "\(bluetoothManager.RespiratoryRate)bpm/min")
                .font(.system(size: 16))
                .fontWeight(.thin)
                .foregroundColor(Color(red: 29/255, green: 33/255, blue: 41/255))
        }
        .frame(height: 30)
        .padding(.leading, 28)
        .padding(.trailing, 28)
        
        HStack(spacing:0) {
            Text(appConfigManage.getTextByKey(key: "MainETCO2"))
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.ETCO2 == 0 ? "--\(bluetoothManager.CO2Unit.rawValue)" : "\(bluetoothManager.ETCO2.formatted(.number.precision(.fractionLength(0...2))))\(bluetoothManager.CO2Unit.rawValue)")
                .font(.system(size: 16))
                .fontWeight(.thin)
                .foregroundColor(Color(red: 29/255, green: 33/255, blue: 41/255))
        }
        .frame(height: 30)
        .padding(.leading, 28)
        .padding(.trailing, 28)
    }
}

struct ResultView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    @State private var hasAppeared = false
    @State private var isVisible = true
    
    let timer = Timer.publish(every: 1.0, on: .main, in: .common).autoconnect()

    // 为什么用NavigationStack而不是NavigationView
    // https://stackoverflow.com/questions/57425921/swiftui-unwanted-split-view-on-ipad
    var body: some View {
        let warningText: String?
        if bluetoothManager.isAsphyxiation {
            warningText = appConfigManage.getTextByKey(key: "AsphyxiationWarning")
        } else if !bluetoothManager.isValidETCO2 {
            warningText = appConfigManage.getTextByKey(key: "ETCO2InvalidWarning")
        } else if !bluetoothManager.isValidRR {
            warningText = appConfigManage.getTextByKey(key: "RRInvalidWarning")
        } else {
            warningText = nil
        }
        return NavigationStack() {
            VStack(spacing: 0){
                LineChartView()
                if warningText != nil {
                    Text(warningText!)
                        .foregroundColor(.red)
                        .font(.system(size: 16))
                        .fontWeight(.bold)
                        .opacity(isVisible ? 1 : 0)
                        .onReceive(timer) { _ in
                            withAnimation(.easeInOut(duration: 0.5)) {
                                self.isVisible.toggle()
                            }
                        }
                        .onDisappear() {
                            self.timer.upstream.connect().cancel()
                        }
                }
                TableView()
                Spacer()
            }
            .navigationTitle("CapnoGraph")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
        }
        .onAppear {            
            // 启动后，将所有本地保存的设置都同步到设备上。
            if !hasAppeared {
                bluetoothManager.initDevice()
                hasAppeared = true
            } else {
                bluetoothManager.sendContinuous()
            }
        }
    }
}
