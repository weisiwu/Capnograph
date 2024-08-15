import SwiftUI
import UIKit
import Combine
import Foundation
import PDFKit
import Charts

// 支持的保存类型
enum SaveTypes: String {
    case PDF = "pdf"
}

// 错误类型
enum SaveErrorTypes: Error {
    case NoData // 无保存数据
    case InvalidDirectory // 无法获取文档目录
    case UnSupportFileType // 不支持的导出类型
    case SaveFailed // 保存失败
}

protocol SuccessProtocol {
    var Success: Bool { get }
}

// 保存结果类型
enum SaveResultTypes: SuccessProtocol {
    case Success
    case Error(SaveErrorTypes)
    
    var Success: Bool {
        switch self {
            case .Success:
                return true
            default:
                return true
        }
    }
}

/**
* CO2 波形单点数据，包含以下
* 1、呼吸率（RR），如无值，置0
* 2、ETCO2，无值，置0
*/
struct CO2WavePointData {
    let RR: Int
    let ETCO2: Float
    let FiCO2: Int
}

/**
* 波形历史数据类
*/
class HistoryData {
    // 波形数据记录起始日期，启动时候自动获取
    let recordStartDate: Date
        // 波形数据起始时间
    let recordStartDateStr: String
    // 波形数据记录结束日期，导出时候自动获取
    lazy var recordEndDate: Date = {
        return Date()
    }()
    // 波形数据结束时间
    lazy var recordEndDateStr: String = {
        return dateFormatter.string(from: recordEndDate)
    }()
    // 时间格式化
    let dateFormatter = DateFormatter()
    // RR/ETCO2合理最大最小值，绘图辅助使用
    var minRR: Double
    var maxRR: Double
    var minETCO2: Double
    var maxETCO2: Double
    // 波形数据列表
    var CO2WavePoints: [CO2WavePointData]

    init(
        minRR: Double,
        maxRR: Double,
        minETCO2: Double, 
        maxETCO2: Double, 
        CO2WavePoints: [CO2WavePointData]
    ) {
        // 记录的时间信息
        dateFormatter.dateFormat = "yyyyMMDDHHmmSS"
        self.recordStartDate = Date()
        self.recordStartDateStr = dateFormatter.string(from: recordStartDate)
        // 初始化辅助数据，这些数据并不参加渲染pdf，而是由于后续支持pdf数据细节渲染
        self.minRR = minRR
        self.maxRR = maxRR
        self.minETCO2 = minETCO2
        self.maxETCO2 = maxETCO2
        // pdf主体数据
        self.CO2WavePoints = CO2WavePoints
    }
  
    // 更新历史波形数据
    func updateWavePoints(newPoint: CO2WavePointData) {
        CO2WavePoints.append(newPoint)
    }

    // 终止记录数据
    func markEndTime() {
        recordEndDate = Date()
        self.recordEndDateStr = dateFormatter.string(from: recordEndDate)
    }
}

/**
 * 折线图，用于生成pdf中image使用
 * 和ResultView中的LineChartView类似，只是为了生成图片做了必要的删减
 */
struct LineChartViewForImage: View {
    var data: HistoryData
    var blm: BluetoothManager
    @EnvironmentObject var appConfigManage: AppConfigManage

    var body: some View {
        return Chart {
            ForEach(Array(data.CO2WavePoints.enumerated()), id: \.offset) { index, point in
                LineMark(
                    x: .value("Index", index),
                    y: .value("Value", point.ETCO2)
                )
                .interpolationMethod(.cardinal)
            }
        }
        .chartXAxis {
            AxisMarks(preset: .aligned, values: Array(stride(from: 0, through: maxXPoints, by: xPointStep))) { value in
                AxisValueLabel {
                    if let intValue = value.as(Int.self) {
                        Text("\(intValue / 100)S")
                    }
                }
            }
        }
        .chartYScale(domain: 0...Double(blm.CO2Scale.rawValue))
        .chartYAxis {
            AxisMarks(
                preset: .aligned,
                position: .leading,
                values: generateYAxis(scale: blm.CO2Scale)
            )
        }
        .frame(height: 300)
        .padding()
    }
}

/**
 * asImage 修饰符
 * SwiftUI View 导出Image
 */
extension View {
    func asImage() -> UIImage {
        let controller = UIHostingController(rootView: self)
        // 确保视图大小合适
        controller.view.frame = CGRect(x: 0, y: 0, width: 612, height: 792) // A4 页面大小
        let targetSize = controller.view.intrinsicContentSize
        controller.view.bounds = CGRect(origin: .zero, size: targetSize)
        controller.view.backgroundColor = .clear

        let renderer = UIGraphicsImageRenderer(size: targetSize)
        return renderer.image { _ in
            controller.view.drawHierarchy(in: controller.view.bounds, afterScreenUpdates: true)
        }
    }
}

/**
* 历史数据管理类，支持以下
* 1、历史数据导出为pdf
* =============
* 1. 初始化时，绑定BLM(BlutoothManager),对BLM的绘图数据绑定监听，并根据BLM的状态,更新历史波形数据
* 2. 用户界面点击导出时，停止保存，将数据转化为pdf数据，保存到本地
*/
class HistoryDataManage: ObservableObject {
    var data: HistoryData?
    var blm: BluetoothManager?
    // 监听波形数据是否发生变化
    var CO2WavePointWatcher: AnyCancellable?
    @Published var pdfURL: URL?

    // 将蓝牙管理实例注入历史数据中
    func listenToBluetoothManager(bluetoothManager: BluetoothManager) {
        self.data = HistoryData(
            minRR: bluetoothManager.rrLower,
            maxRR: bluetoothManager.rrUpper,
            minETCO2: bluetoothManager.etCo2Lower,
            maxETCO2: bluetoothManager.etCo2Upper,
            CO2WavePoints: []
        )
        self.blm = bluetoothManager
        self.CO2WavePointWatcher = bluetoothManager.$currentWavePointData.sink { newValue in
            print("[pdf]接收到新波形值===> \(newValue)")
            if let historyData = self.data {
                historyData.updateWavePoints(newPoint: newValue)
            }
        }
    }

    /**
    * 清空记录
    */
    func clearRecord() {
        self.CO2WavePointWatcher = nil
        self.data = nil
    }

    /**
    * 保存pdf数据到指定路径
    * @params fileUrl: 文件保存的路径
    * @return SaveResultTypes
    */
    private func _savePDFToLocal() throws -> SaveResultTypes {
        print("开始保存PDF数据===> \(self.data)")
        // 获取本地文档目录的 URL
        guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            throw SaveErrorTypes.InvalidDirectory
        }
        // 文件名称格式: 20240501182530_20240502192544.pdf 每部分含义如下
        // 第一部分: 波形数据起始时间: 2024年5月1日18点25分30秒
        // 第二部分: 波形数据结束时间: 2024年5月2日19点25分44秒
        let localFileName = "\(data!.recordStartDateStr)-\(data!.recordEndDateStr)"
        
        // 如果没有blm或者数据，直接返回无数据异常
        guard let data = self.data, let blm = self.blm else {
            return SaveResultTypes.Error(.NoData)
        }

        // 保存到PDF中
        // TODO: demo开始
        // 绘制 Chart View
//        let chartImage = LineChartViewForImage(data: data, blm: blm).asImage()
//        let pdfPage = PDFPage(image: chartImage)!
//
//        // 获取 PDF 上下文
//        let pdfContext = UIGraphicsGetCurrentContext()!
//        pdfContext.beginPDFPage(withInfo: nil, for: pdfPage)
//
//        // 结束 PDF 页面
//        pdfContext.endPDFPage()
//
//        // 创建 PDF 文档
//        let pdfDocument = PDFDocument()
//        pdfDocument.insert(pdfPage, at: 0)
//
//        // 保存 PDF 文件
//        // let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        // let fileURL = documentsURL.appendingPathComponent("chart.pdf")
//
//        guard let pdfData = pdfDocument.dataRepresentation() else {
//            print("无法生成 PDF 数据")
//            return SaveResultTypes.Error(.SaveFailed)
//        }

        // 创建完整的文件路径
        self.pdfURL = documentsDirectory.appendingPathComponent("\(localFileName).\(SaveTypes.PDF.rawValue)")

        
        guard let pdfURL = self.pdfURL,
              let pdfContext = CGContext(url: pdfURL as CFURL, mediaBox: nil, nil) else {
            print("无法创建 PDF 上下文")
            return SaveResultTypes.Error(.SaveFailed)
        }

        // 绘制 Chart View
        let chartImage = LineChartViewForImage(data: data, blm: blm).asImage()
        let pageRect = CGRect(x: 0, y: 0, width: chartImage.size.width, height: chartImage.size.height)

        // 开始 PDF 页面
        pdfContext.beginPage(pageSize: pageRect.size)
        
        // 绘制图片到 PDF
        chartImage.draw(in: pageRect)

        // 结束 PDF 页面
        pdfContext.endPage()

        // 关闭 PDF 上下文
        pdfContext.close()
//                try pdfData.write(to: pdfURL)
        print("PDF 文件已保存到：\(pdfURL)")
        
        // TODO: demo结束

        // TODO:(wsw) 临时注释测试用demo
        // let pdfView = PDFView()
        // let pdfDocument = PDFDocument()
        // let page = PDFPage(image: UIImage(named: "example_image")!)
        // pdfDocument.insert(page!, at: 0)
        // if let _pdfURL = self.pdfURL {
        //     print("最终写入的路径===> \(_pdfURL)")
        //     pdfDocument.write(to: _pdfURL)
        //     self.pdfURL = _pdfURL
        // }

        return SaveResultTypes.Success
    }
    
    /**
    * 保存波形图数据到本地，后续扩展支持多种形式
    * @params type: 波形数据保存的文件类型
    * @params pdfData: 波形数据
    * @return SaveResultTypes
    */
    private func _saveToLocal(type: SaveTypes = SaveTypes.PDF) throws -> SaveResultTypes {
        guard data != nil else {
            throw SaveErrorTypes.NoData
        }

        // 标记记录数据结束时间
        data!.markEndTime()
        
        switch type {
            case SaveTypes.PDF:
                return try _savePDFToLocal()
            default:
                throw SaveErrorTypes.UnSupportFileType
        }
    }

    /**
    * 统一处理保存的结果
    */
    func wrapLogger<T: SuccessProtocol>(_ function: (SaveTypes) throws -> T, args: SaveTypes) {
        do {
            let res = try function(args)
            if let internalRes = res as? SaveResultTypes {
                switch internalRes {
                    case .Success:
                        print("调用函数成功")
                    default:
                        print("无事发生")
                }
            }
        } catch SaveErrorTypes.NoData {
            print("[pdf]保存pdf数据失败，历史数据为空")
        } catch SaveErrorTypes.InvalidDirectory {
            print("[pdf]无法获取文档目录")
        } catch SaveErrorTypes.UnSupportFileType {
            print("[pdf]不支持的保存类型")
        } catch {
            print("[pdf]保存pdf失败")
        }
    }

    /**
    * 外部调用使用
    */
    func saveToLocal(args: SaveTypes = SaveTypes.PDF) {
        wrapLogger(_saveToLocal, args: args)
    }
}
