import Foundation

// 支持的保存类型
enum SaveTypes: String {
    case PDF = "pdf"
}

/**
  * 保存pdf数据到指定路径
  * @params fileData: pdf文件数据
  * @params fileUrl: 文件保存的路径
  * @return Bool true 保存成功 false 保存失败
 */
//TODO: 临时一个参数
//func savePDFToLocal(fileData: Data, fileUrl: URL) {
func savePDFToLocal(fileUrl: URL) -> Bool {
    return true
}

/**
 * 保存波形图数据到本地，后续扩展支持多种形式
 * @params type: 波形数据保存的文件类型
 * @params pdfData: 波形数据
 */
func saveToLocal(type: SaveTypes = SaveTypes.PDF, pdfData: PDFData) {

    // 获取本地文档目录的 URL
    guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
        print("无法获取文档目录")
        return
    }
    
    // 文件名称格式: 20240501_182530_192544.pdf 每部分含义如下
    // 第一部分: 波形数据记录日期: 2024年5月1日
    // 第二部分: 波形数据起始时间: 18点25分30秒
    // 第三部分: 波形数据结束时间: 19点25分44秒
    let localFileName = "\(pdfData.recordDate)_\(pdfData.startTime)_\(pdfData.stopTime)"

    // 创建完整的文件路径
    let fileURL: URL = documentsDirectory.appendingPathComponent("\(localFileName).\(type.rawValue)")
    print("保存的文件url \(fileURL)")
    
    do {
        switch type {
            case SaveTypes.PDF:
                savePDFToLocal(fileUrl: fileURL)
        }

        print("PDF 已成功保存到")
    } catch {
        print("保存 PDF 过程中发生错误")
    }
}

class PDFData {
    // 波形数据记录日期
    let recordDate: String
    // 波形数据起始时间
    let startTime: String
    // 波形数据结束时间
    let stopTime: String
    
    init() {
        self.recordDate = ""
        self.stopTime = ""
        self.startTime = ""
    }
}
