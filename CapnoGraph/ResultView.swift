import SwiftUI
import Charts
import Foundation

// 最多展示的横向点数量，每10ms接收到一帧数据，横坐标展示20s的波形图，则共1000个横坐标
let maxXPoints: Int = 2000
let xPointStep: Int = 500 // 步长，每100取一个坐标点
let unRealValue: Float = -1 // 初始线表时，所有点的初始值

struct DataPoint: Identifiable {
    let id = UUID()
    let value: Float
}

struct LineChartView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    var body: some View {
        Chart {
            ForEach(Array(bluetoothManager.receivedCO2WavedData.enumerated()), id: \.offset) { index, point in
                LineMark(
                    x: .value("Index", index),
                    y: .value("Value", point.value)
                )
                .interpolationMethod(.cardinal)
            }
        }
        .chartXAxis {
            AxisMarks(values: Array(stride(from: 0, to: maxXPoints, by: xPointStep))) { value in
                AxisValueLabel {
                    if let intValue = value.as(Int.self) {
                        Text("\(intValue / 100 + 1)\(appConfigManage.getTextByKey(key: "MainLineCharUnit"))")
                    }
                }
            }
        }
        .chartYAxis {
            AxisMarks(position: .leading, values: Array(stride(from: 0.0, through: bluetoothManager.CO2Scale.rawValue, by: min(ceil(bluetoothManager.CO2Scale.rawValue / 5), 10.0))))
        }
        .frame(height: 300)
        .padding()
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
            Text(bluetoothManager.RespiratoryRate == 0 ? "--/min" : "\(bluetoothManager.RespiratoryRate)/min")
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
            Text(bluetoothManager.ETCO2 == 0 ? "--/\(bluetoothManager.CO2Unit.rawValue)" : "\(bluetoothManager.ETCO2.formatted(.number.precision(.fractionLength(0...2))))/\(bluetoothManager.CO2Unit.rawValue)")
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
    var body: some View {
        NavigationView() {
            VStack(spacing: 0){
                LineChartView()
                TableView()
                Spacer()
            }
            .navigationTitle("CapnoGraph")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(false)
        }
    }
}
