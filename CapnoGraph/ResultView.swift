import SwiftUI
import Charts

struct DataPoint: Identifiable {
    let id = UUID()
    let date: Int
    let value: Double
}

struct LineChartView: View {
    let data: [DataPoint] = [
        DataPoint(
            date: 1,
            value: 12.1
        ),
        DataPoint(
            date: 2,
            value: 4.56
        ),
        DataPoint(
            date: 3,
            value: 47.8
        ),
        DataPoint(
            date: 4,
            value: 41.7
        ),
        DataPoint(
            date: 5,
            value: 37.1
        ),
        DataPoint(
            date: 6,
            value: 31.1
        ),
        DataPoint(
            date: 7,
            value: 25.8
        )
    ]

    var body: some View {
        Chart(data) { point in
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
    var body: some View {
        HStack(spacing:0) {
            Text("PR")
                .font(.system(size: 16))
                .fontWeight(.bold)
            Spacer()
            Text("76/min")
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
            Text("0mmHg")
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
        VStack(spacing: 0){
            LineChartView()
            TableView()
            Spacer()
        }
    }
}


#Preview {
    ResultView()
}
