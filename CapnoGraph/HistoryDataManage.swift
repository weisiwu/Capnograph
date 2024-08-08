//import Combine
//import Foundation
//
//// 支持的保存类型
//enum SaveTypes: String {
//   case PDF = "pdf"
//}
//
//// 错误类型
//enum SaveErrorTypes: Error {
//   case NoData // 无保存数据
//   case InvalidDirectory // 无法获取文档目录
//   case UnSupportFileType // 不支持的导出类型
//}
//
//// 保存结果类型
//enum SaveResultTypes {
//   case Success
//   case Error(SaveErrorTypes)
//}
//
///**
//* CO2 波形单点数据，包含以下
//* 1、呼吸率（RR），如无值，置0
//* 2、ETCO2，无值，置0
//*/
//struct CO2WavePointData {
//   let RR: Double
//   let ETCO2: Double
//   let FiCO2: Double
//}
//
///**
//* 波形历史数据类
//*/
//class HistoryData {
//   // 波形数据记录起始日期，启动时候自动获取
//   let recordStartDate: Date
//   let recordStartDateStr: String
//   // 波形数据记录结束日期，导出时候自动获取
//   let recordEndDate: Date
//   let recordEndDateStr: String
//   // 时间格式化
//   let dateFormatter = DateFormatter()
//   // 波形数据起始时间
//   let startTime: String
//   // 波形数据结束时间
//   let stopTime: String
//   // RR/ETCO2合理最大最小值，绘图辅助使用
//   var minRR: Double
//   var maxRR: Double
//   var minETCO2: Double
//   var maxETCO2: Double
//   // 波形数据列表
//   var CO2WavePoints: [CO2WavePointData]
//
//   init(
//       minRR: Double,
//       maxRR: Double,
//       minETCO2: Double, 
//       maxETCO2: Double, 
//       CO2WavePoints: [CO2WavePointData]
//   ) {
//       // 记录的时间信息
//       dateFormatter.dateFormat = "yyyyMMDD"
//       self.recordStartDate = Date()
//       self.recordStartDateStr = dateFormatter.string(from: recordStartDate)
//       // TODO:(wsw) j
//       self.startTime = startTime
//       // 初始化辅助数据，这些数据并不参加渲染pdf，而是由于后续支持pdf数据细节渲染
//       self.minRR = minRR
//       self.maxRR = maxRR
//       self.minETCO2 = minETCO2
//       self.maxETCO2 = maxETCO2
//       // pdf主体数据
//       self.CO2WavePoints = CO2WavePoints
//   }
//   
//   // 更新历史波形数据
//   func updateWavePoints(newPoint: CO2WavePointData) {
//       CO2WavePoints.append(newPoint)
//   }
//
//   // 终止记录数据
//   func markEndTime() {
//       recordEndDate = Date()
//       self.recordEndDateStr = dateFormatter.string(from: recordEndDate)
//       self.stopTime = stopTime
//   }
//}
//
///**
//* 历史数据管理类，支持以下
//* 1、历史数据导出为pdf
//* =============
//* 1. 初始化时，绑定BLM(BlutoothManager),对BLM的绘图数据绑定监听，并根据BLM的状态,更新历史波形数据
//* 2. 用户界面点击导出时，停止保存，将数据转化为pdf数据，保存到本地
//*/
//class HistoryDataManage {
//   let data: HistoryData?
//   let blm: BluetoothManager
//   // 监听波形数据是否发生变化
//   let CO2WavePointWatcher: AnyCancellable?
//
//   init(blm: BluetoothManager) {
//       self.data = HistoryData(
//           startTime: "2020-01-01 00:00:00",
//           stopTime: "2020-01-01 00:00:00",
//           minRR: blm.rrLower,
//           maxRR: blm.rrUpper,
//           minETCO2: blm.etCo2Lower,
//           maxETCO2: blm.etCo2Upper,
//           CO2WavePoints: []
//       )
//       self.blm = blm
//       self.CO2WavePointWatcher = blm.$currentWavePointData.sink { newValue in
//           print("[pdf]接收到新波形值===> \(newValue)")
//           if let historyData = data {
//               historyData.updateWavePoints(newPoint: newValue)
//           }
//       }
//   }
//   
//   /**
//    * 清空记录
//    */
//   func clearRecord() {
//       self.CO2WavePointWatcher = nil
//       self.data = nil
//   }
//
//   /**
//    * 保存pdf数据到指定路径
//    * @params fileUrl: 文件保存的路径
//    * @return SaveResultTypes
//    */
//   private func _savePDFToLocal(fileUrl: URL) -> SaveResultTypes {
//       return SaveResultTypes.Success
//   }
//
//   /**
//    * 保存波形图数据到本地，后续扩展支持多种形式
//    * @params type: 波形数据保存的文件类型
//    * @params pdfData: 波形数据
//    * @return SaveResultTypes
//    */
//   private func _saveToLocal(type: SaveTypes = SaveTypes.PDF) -> SaveResultTypes {
//       guard data != nil else {
//           throw SaveErrorTypes.NoData
//       }
//
//       // 获取本地文档目录的 URL
//       guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
//           throw SaveErrorTypes.InvalidDirectory
//       }
//
//       // 标记记录数据结束时间
//       data.markEndTime()
//       
//       // 文件名称格式: 20240501182530_20240502192544.pdf 每部分含义如下
//       // 第一部分: 波形数据起始时间: 2024年5月1日18点25分30秒
//       // 第二部分: 波形数据结束时间: 2024年5月2日19点25分44秒
//       let localFileName = "\(data.recordStartDateStr)\(data.startTime)_\(data.recordEndDateStr)\(data.stopTime)"
//
//       // 创建完整的文件路径
//       let fileURL: URL = documentsDirectory.appendingPathComponent("\(localFileName).\(type.rawValue)")
//       print("保存的文件url \(fileURL)")
//       
//       switch type {
//           case SaveTypes.PDF:
//               return _savePDFToLocal(fileUrl: fileURL)
//           default:
//               throw SaveErrorTypes.UnSupportFileType
//       }
//   }
//
//   /**
//    * 统一处理保存的结果
//    */
//   func wrapLogger<T>(_ function: (SaveTypes) throws -> T, args: SaveTypes) -> T {
//       do {
//           let res = try function(args)
//           switch res {
//               case .Success:
//                   print("调用函数成功")
//           }
//       } catch SaveErrorTypes.NoData {
//           print("[pdf]保存pdf数据失败，历史数据为空")
//       } catch SaveErrorTypes.InvalidDirectory {
//           print("[pdf]无法获取文档目录")
//       } catch SaveErrorTypes.UnSupportFileType {
//           print("[pdf]不支持的保存类型")
//       } catch {
//           print("[pdf]保存pdf失败")
//       }
//   }
//
//   /**
//    * 外部调用使用
//    */
//   func saveToLocal(args: SaveTypes) {
//       return wrapLogger(_saveToLocal, args: args)
//   }
//}
//
//// TODO:(wsw) 测试
