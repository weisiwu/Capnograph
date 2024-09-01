import SwiftUI
import UIKit
import Charts
import Foundation
import PDFKit

// 最多展示的横向点数量，每10ms接收到一帧数据，横坐标展示20s的波形图，则共1000个横坐标
let maxXPoints: Int = 400
// let xPointStep: Int = 100 // 步长，每100取一个坐标点
let xPointStep: Int = 50 // 步长，每50取一个坐标点
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
                    y: .value("Value", max(point.value, 0))
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
                        Text("\(intValue / xPointStep)\(appConfigManage.getTextByKey(key: "MainLineCharUnit"))")
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

// 扩展view，展示分享链接
extension View {
    // 根据是否有分享url，决定是否可分享
    func shareableView(url: URL?) -> some View {
        Group {
            if let _url = url {
                ShareLink(item: _url) { self }
            } else {
                self
            }
        }
    }
}

struct BottomSheetView: View {
    @Binding var showModal: Bool
    @EnvironmentObject var appConfigManage: AppConfigManage
    @EnvironmentObject var historyDataManage: HistoryDataManage
    @State private var degrees = 0.0

    var body: some View {
        return VStack {
            Text(appConfigManage.getTextByKey(key: "ShareBtn"))
                .font(.system(size: 18))
                .fontWeight(.bold)
                .frame(height: 54, alignment: .center)
            Spacer()
            HStack {
                VStack {
                    ZStack {
                        Image("pdf_icon")
                            .resizable()
                            .frame(width: 50, height: 50)
                            .scaledToFill()
                            .clipped()
                        // 这里只是显示loading，并不代表是否可以导出。
                        if historyDataManage.pdfURL == nil {
                            Color.black.frame(width: 58, height: 70).opacity(0.3).cornerRadius(5)
                            Image("pdf_icon_loading")
                                .resizable()
                                .frame(width: 40, height: 40)
                                .scaledToFill()
                                .clipped()
                                .opacity(0.8)
                                .rotationEffect(Angle(degrees: degrees), anchor: UnitPoint(x: 0.5, y: 0.5))
                                .animation(Animation.linear(duration: 1.5).repeatForever(autoreverses: false), value: self.degrees == 360.0)
                                .onAppear {
                                    self.degrees = 360.0
                                }
                        }
                    }
                    Text(appConfigManage.getTextByKey(key: "ExportPDF"))
                        .foregroundColor(Color(red: 133/255, green: 144/255, blue: 156/255))
                        .font(.system(size: 12))
                    Spacer()
                }
                .shareableView(url: historyDataManage.pdfURL)
                .onTapGesture {
                    // 成功生成导出文件后，出现确定弹框，用户点击分享后，唤起分享面板
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                        showModal = false
                    }
                }
                .padding(.leading, 20)
                Spacer()
            }
            .task {
                historyDataManage.saveToLocal()
            }
            Divider()
                .frame(height: 0.5)
                .background(Color(red: 133/255, green: 144/255, blue: 156/255))
                .opacity(0.2)
            Button(action: {
                showModal.toggle()
            }) {
                Text(appConfigManage.getTextByKey(key: "SearchConfirmNo"))
                    .font(.system(size: 18))
                    .frame(height: 54, alignment: .center)
            }
        }
        .padding(0)
        .padding(.bottom, 20)
        .edgesIgnoringSafeArea(.bottom)  // 忽略底部安全区域以减少间距
    }
}

struct ResultView: View {
    @EnvironmentObject var bluetoothManager: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage
    @EnvironmentObject var historyDataManage: HistoryDataManage
    @State private var hasAppeared = false
    @State private var isVisible = true
    // 更多面板
    @State private var showModal = false
    // TODO:(wsw) 临时
    
    let timer = Timer.publish(
        every: 1.0, 
        on: .main, 
        in: .common
    ).autoconnect()

    // 为什么用NavigationStack而不是NavigationView
    // https://stackoverflow.com/questions/57425921/swiftui-unwanted-split-view-on-ipad
    var body: some View {
        let warningText: String?
        if bluetoothManager.isAsphyxiation {
            warningText = appConfigManage.getTextByKey(key: "AsphyxiationWarning")
        } else if bluetoothManager.isLowerEnergy {
            warningText = appConfigManage.getTextByKey(key: "LowerEnergy")
        } else if bluetoothManager.Breathe && !bluetoothManager.isValidETCO2 {
            if CGFloat(bluetoothManager.ETCO2) > bluetoothManager.etCo2Upper {
                warningText = appConfigManage.getTextByKey(key: "ETCO2InvalidWarningUpper")
            } else {
                warningText = appConfigManage.getTextByKey(key: "ETCO2InvalidWarningLower")
            }
        } else if bluetoothManager.Breathe && !bluetoothManager.isValidRR {
            if CGFloat(bluetoothManager.RespiratoryRate) > bluetoothManager.rrUpper {
                warningText = appConfigManage.getTextByKey(key: "RRInvalidWarningUpper")
            } else {
                warningText = appConfigManage.getTextByKey(key: "RRInvalidWarningLower")
            }
        } else {
            warningText = nil
        }

        return NavigationStack() {
            ZStack {
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
                .navigationBarItems(
                    trailing: Button(action: {
                        showModal.toggle()
                    }) {
                        Image("home_more_btn")
                            .resizable()
                            .frame(width: 20, height: 20)
                            .scaledToFill()
                            .clipped()
                    }
                )
                .navigationBarTitleDisplayMode(.inline)
                .navigationBarBackButtonHidden(false)

                if showModal {
                    Color.black.opacity(0.4)
                        .edgesIgnoringSafeArea(.all)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
            }
        }
        .onAppear {
            // 启动后，将所有本地保存的设置都同步到设备上。
            if !hasAppeared {
                // TODO:(wsw) 同步设备配置不写在app启动，
                // 而是放在蓝牙事件委托中，如果发现central(手机)蓝牙开启，自动重连。
                // 在重连成功的事件委托中，继续初始化设备
                hasAppeared = true
            } else {
                bluetoothManager.sendContinuous()
            }
        }
         .sheet(isPresented: $showModal) {
             BottomSheetView(showModal: $showModal)
                 .presentationDetents([.height(240)])
         }
    }
}
