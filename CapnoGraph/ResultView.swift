import SwiftUI
import Charts

struct DataPoint: Identifiable {
    let id = UUID()
    let date: Int
    let value: Float
}

struct LineChartView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager

    var body: some View {
        Chart(bluetoothManager.receivedCO2WavedData) { point in
            LineMark(
                x: .value("时间", point.date),
                y: .value("数值", point.value)
            )
            .interpolationMethod(.cardinal)
        }
        .chartXAxis {
            AxisMarks(values: [1,2,3,4,5,6,7])
        }
        .chartYAxis {
            AxisMarks(position: .leading, values: [0,10.0,20.0,30.0,40.0,50.0])
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
//        TODO: 临时注释掉
//            Text(bluetoothManager.RespiratoryRate == 0 ? "--/min" : "\(bluetoothManager.RespiratoryRate)/min")
            Text("--/min")
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
//        TODO: 临时注释掉
//            Text(bluetoothManager.ETCO2 == 0 ? "--/mmHg" : "\(bluetoothManager.ETCO2)/mmHg")
            Text("--mmHg")
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
