import UIKit
import SwiftUI
import Combine
import Foundation
import PDFKit

// 支持的保存类型
enum SaveTypes: String {
    case PDF = "pdf"
}

// 错误类型
enum SaveErrorTypes: Error {
    case NoData // 无保存数据
    case InvalidDirectory // 无法获取文档目录
    case UnSupportFileType // 不支持的导出类型
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
    private func _savePDFToLocal(fileUrl: URL) -> SaveResultTypes {
        print("开始保存PDF数据===> \(self.data)")
        // 保存到PDF中
        // PDFKitView(pdfData: PDFDocument(data: "测试文案==测试文案".data(using: .utf8)))
//        let pdfData = PDFDocument(data: "测试文案==测试文案".data(using: .utf8)!)
//        let url = Bundle.main.url(forResource: "sample", withExtension: "pdf")!

//        do {
//            try pdfData!.write(to: fileUrl)
//            print("PDF saved at: \(fileUrl)")
//        } catch {
//            print("Error saving PDF: \(error.localizedDescription)")
//        }
        // 分享文件数据
        // 思路参考: https://www.cnblogs.com/qqcc1388/p/17586849.html

        return SaveResultTypes.Success
    }

    private func createSampleFile() {
        let fileURL = pdfURL
        
        let pdfMetaData = [
            kCGPDFContextCreator: "MyApp",
            kCGPDFContextAuthor: "MyCompany",
            kCGPDFContextTitle: "Sample Document"
        ]
        
        let format = UIGraphicsPDFRendererFormat()
        format.documentInfo = pdfMetaData as [String: Any]
        
        let pageWidth = 8.5 * 72.0
        let pageHeight = 11.0 * 72.0
        let pageSize = CGSize(width: pageWidth, height: pageHeight)
        
        let renderer = UIGraphicsPDFRenderer(bounds: CGRect(origin: .zero, size: pageSize), format: format)
        
        let pdfData = renderer.pdfData { context in
            context.beginPage()
            let text = "This is a sample PDF document."
            let attributes: [NSAttributedString.Key: Any] = [.font: UIFont.systemFont(ofSize: 24)]
            let textRect = CGRect(x: 20, y: 20, width: pageWidth - 40, height: pageHeight - 40)
            text.draw(in: textRect, withAttributes: attributes)
        }
        
        do {
            try pdfData.write(to: fileURL!)
            print("File created at: \(fileURL!)") // 确认文件路径
        } catch {
            print("Could not create file: \(error)")
        }
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

        // 获取本地文档目录的 URL
        guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            throw SaveErrorTypes.InvalidDirectory
        }

        // 标记记录数据结束时间
        data!.markEndTime()
        
        // 文件名称格式: 20240501182530_20240502192544.pdf 每部分含义如下
        // 第一部分: 波形数据起始时间: 2024年5月1日18点25分30秒
        // 第二部分: 波形数据结束时间: 2024年5月2日19点25分44秒
        let localFileName = "\(data!.recordStartDateStr)-\(data!.recordEndDateStr)"

        // 创建完整的文件路径
        pdfURL = documentsDirectory.appendingPathComponent("\(localFileName).\(type.rawValue)")
        
        createSampleFile() // 创建一个示例文件
        
        switch type {
            case SaveTypes.PDF:
                return _savePDFToLocal(fileUrl: pdfURL!)
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
