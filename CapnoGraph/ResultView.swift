import SwiftUI
import Charts

// 最多展示的横向点数量，每10ms接收到一帧数据，横坐标展示10s的波形图，则共1000个横坐标
let maxXPoints: Int = 500
let xPointStep: Int = 100 // 步长，每搁20取一个坐标点

struct DataPoint: Identifiable {
    let id = UUID()
    let value: Float
}

struct LineChartView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager

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
                        Text("\(intValue / 100 + 1)秒") // 自定义标签
                    }
                }
            }
        }
        .chartYAxis {
            AxisMarks(position: .leading, values: [0, 10.0, 20.0, 30.0, 40.0, 50.0])
        }
        .frame(height: 300)
        .padding()
    }
}

struct TableView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager

    var body: some View {
        HStack(spacing:0) {
            Text("设备名称")
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.connectedPeripheral?.name ?? "未知设备")
                .font(.system(size: 16))
                .fontWeight(.thin)
                .foregroundColor(Color(red: 29/255, green: 33/255, blue: 41/255))
        }
        .frame(height: 30)
        .padding(.leading, 28)
        .padding(.trailing, 28)
        
        HStack(spacing:0) {
            Text("设备ID")
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
            Text("PR")
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
            Text("ETCO2")
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text(bluetoothManager.ETCO2 == 0 ? "--/mmHg" : "\(bluetoothManager.ETCO2.formatted(.number.precision(.fractionLength(0...2))))/mmHg")
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


//#Preview {
//    ResultView()
//}
