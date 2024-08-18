import SwiftUI
import UIKit
import Combine
import Foundation
import PDFKit
import Charts
import CoreGraphics

// TODO:(wsw) 参考这个
// 参考这个: https://www.swiftanytime.com/blog/imagerenderer-in-swiftui
enum A4Size: Double {
    case width = 595.2
    case height = 841.8
}

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
    let co2: Float
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
    var fSize: CGFloat = 14

    var body: some View {
        return Chart {
            ForEach(Array(data.CO2WavePoints.enumerated()), id: \.offset) { index, point in
                LineMark(
                    x: .value("Value", point.co2),
                    y: .value("Index", index)
                )
                .interpolationMethod(.cardinal)
            }
        }
        .chartXScale(domain: 0...Double(blm.CO2Scale.rawValue))
        .chartXAxis {
            AxisMarks(
                preset: .aligned,
                position: .top,
                values: generateYAxis(scale: blm.CO2Scale)
            ) { value in
                AxisValueLabel {
                    if let intValue = value.as(Int.self) {
                        Text("\(intValue)")
                            .font(.system(size: fSize))
                            .rotationEffect(.degrees(90))
                            .frame(width: 40)
                    }
                }
            }
        }
        .chartYAxis {
            AxisMarks(
                position: .leading,
                values: Array(
                    stride(
                        from: 0,
                        through: data.CO2WavePoints.count,
                        by: xPointStep
                    )
                )
            ) { value in
                return AxisValueLabel {
                    if let intValue = value.as(Int.self) {
                        Text("\(intValue / 100)S")
                            .font(.system(size: fSize))
                            .rotationEffect(.degrees(90))
                            .frame(width: 40)
                    }
                }
            }
        }
        .frame(
            width: A4Size.width.rawValue, 
            height: A4Size.height.rawValue
        )
        .padding(0)
    }
}

/**
 * asImage 修饰符
 * SwiftUI View 导出Image
 */
extension View {
    func asImage() -> UIImage {
        let padding: CGFloat = 30.0
        let a4Size = CGSize(
            width: A4Size.width.rawValue,
            height: A4Size.height.rawValue
        )
        let viewWidth = A4Size.width.rawValue - 2 * padding
        let viewHeight = A4Size.height.rawValue - 2 * padding
        let controller = UIHostingController(rootView: self)
        // 确保视图大小合适
        controller.view.frame = CGRect(
            x: 0, 
            y: 0, 
            width: viewWidth, 
            height: viewHeight
        )
        let targetSize = controller.view.intrinsicContentSize
         // 提高分辨率
        let highResolutionSize = CGSize(
            width: A4Size.width.rawValue,
            height: A4Size.height.rawValue
        )
        controller.view.bounds = CGRect(origin: .zero, size: targetSize)
        controller.view.backgroundColor = .clear

        let renderer = UIGraphicsImageRenderer(size: highResolutionSize)
        return renderer.image { _ in
            UIColor.white.setFill()
            UIRectFill(CGRect(origin: .zero, size: a4Size))
            
            // 翻转坐标系统
            let context = UIGraphicsGetCurrentContext()
            // 抗锯齿
            context?.setAllowsAntialiasing(true)
            context?.setShouldAntialias(true)
            context?.translateBy(x: 0, y: a4Size.height)
            context?.scaleBy(x: 1, y: -1)
            
            controller.view.drawHierarchy(
                in: CGRect(
                    x: padding,
                    y: padding,
                    width: viewWidth,
                    height: viewHeight
                ),
                afterScreenUpdates: true
            )
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
    @Published var pdfURL: URL?

    func listenToBluetoothManager(bluetoothManager: BluetoothManager) {
        self.blm = bluetoothManager
    }

    // 将蓝牙管理实例注入历史数据中
    func syncBluetoothManagerData() {
        guard let blm = self.blm else {
            return
        }

        self.data = HistoryData(
            minRR: blm.rrLower,
            maxRR: blm.rrUpper,
            minETCO2: blm.etCo2Lower,
            maxETCO2: blm.etCo2Upper,
            CO2WavePoints: []
        )

        guard let historyData = self.data else {
            return
        }

        blm.totalCO2WavedData.forEach { point in
            historyData.updateWavePoints(newPoint: point)
        }
    }

    /**
    * 清空记录
    */
    func clearRecord() {
        self.data = nil
    }

    /**
    * 保存pdf数据到指定路径
    * @params fileUrl: 文件保存的路径
    * @return SaveResultTypes
    */
    private func _savePDFToLocal() throws -> SaveResultTypes {
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
        // 创建完整的文件路径
        self.pdfURL = documentsDirectory.appendingPathComponent("\(localFileName).\(SaveTypes.PDF.rawValue)")
        
        // 定义 PDF 页面尺寸与图像尺寸一致
        var pdfPageBounds = CGRect(x: 0, y: 0, width: A4Size.width.rawValue, height: A4Size.height.rawValue)

        guard let pdfURL = self.pdfURL,
              let pdfContext = CGContext(pdfURL as CFURL, mediaBox: &pdfPageBounds, nil) else {
            print("无法创建 PDF 上下文")
            return SaveResultTypes.Error(.SaveFailed)
        }
        
        // 绘制 Chart View
        let chartImage = LineChartViewForImage(data: data, blm: blm).asImage()

        // 开始 PDF 页面
        pdfContext.beginPDFPage(nil)

        // 将 UIImage 绘制到 PDF 页面
        UIGraphicsPushContext(pdfContext)
        chartImage.draw(in: pdfPageBounds)
        UIGraphicsPopContext()

        // 结束 PDF 页面
        pdfContext.endPDFPage()

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
        // 先同步数据
        syncBluetoothManagerData()
        wrapLogger(_saveToLocal, args: args)
    }
}
