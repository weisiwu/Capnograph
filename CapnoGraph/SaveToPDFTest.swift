//https://pspdfkit.com/blog/2019/how-to-show-a-pdf-in-swiftui/
//https://pspdfkit.com/blog/2019/how-to-show-a-pdf-in-swiftui/

//import SwiftUI
//import PDFKit
//import Combine
//import Foundation
//
//struct CO2WavePointData {
//    let RR: Double
//    let ETCO2: Double
//    let FiCO2: Double
//}
//
//// func saveToLocal() {
////     let data = [
////         CO2WavePointData(RR: 1, ETCO2: 1, FiCO2: 1),
////         CO2WavePointData(RR: 2, ETCO2: 2, FiCO2: 2),
////         CO2WavePointData(RR: 3, ETCO2: 3, FiCO2: 3),
////         CO2WavePointData(RR: 4, ETCO2: 4, FiCO2: 4),
////         CO2WavePointData(RR: 5, ETCO2: 5, FiCO2: 5),
////         CO2WavePointData(RR: 6, ETCO2: 6, FiCO2: 6),
////         CO2WavePointData(RR: 7, ETCO2: 7, FiCO2: 7),
////     ]
//
////     // 获取本地文档目录的 URL
////     guard let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
////         print("路径有问题")
////         return
////     }
//
////     let fileURL: URL = documentsDirectory.appendingPathComponent("20240501182530_20240502192544.pdf")
////     print("保存的文件url \(fileURL)")
//// }
//
//// struct PDFViewer: UIViewRepresentable {
////     let pdfURL: URL
//
////     func makeUIView(context: Context) -> PDFView {
////         let pdfView = PDFView()
////         pdfView.autoScales = true // 自动缩放以适合视图
////         return pdfView
////     }
//
////     func updateUIView(_ uiView: PDFView, context: Context) {
////         if let document = PDFDocument(url: pdfURL) {
////             uiView.document = document
////         }
////     }
//// }
//
//
//struct PDFContentView: View {
//    var body: some View {
//        VStack {
//            Button(action: {
//                saveJSONAndCreatePDF()
//            }) {
//                Text("保存 JSON 并生成 PDF")
//            }
//        }
//        .padding()
//    }
//
//    func saveJSONAndCreatePDF() {
//        // 示例 JSON 数据
//        let jsonData: [String: Any] = [
//            "name": "张三",
//            "age": 30,
//            "city": "北京"
//        ]
//
//        // 将 JSON 数据转换为 Data
//        guard let jsonData = try? JSONSerialization.data(withJSONObject: jsonData, options: .prettyPrinted) else {
//            print("JSON 转换出错")
//            return
//        }
//
//        // 保存 JSON 数据为文件
//        let fileURL = FileManager.default.temporaryDirectory.appendingPathComponent("data.json")
//        try? jsonData.write(to: fileURL)
//
//        // 生成 PDF 文件
//        createPDF(from: jsonData)
//    }
//
//    func createPDF(from jsonData: Data) {
//        // 创建 PDF 文档
//        let pdfDocument = PDFDocument()
//
//        // 创建 PDF 页面
//        let pdfPage = PDFPage()
//        let text = String(data: jsonData, encoding: .utf8) ?? "加载失败"
//
//        let pdfBounds = CGRect(x: 0, y: 0, width: 595, height: 842) // A4 尺寸
//        let page = PDFPage()
//
//        let renderer = UIGraphicsImageRenderer(bounds: pdfBounds)
//        let image = renderer.image { context in
//            let attrs: [NSAttributedString.Key: Any] = [
//                .font: UIFont.systemFont(ofSize: 12),
//                .foregroundColor: UIColor.black
//            ]
//            text.draw(in: CGRect(x: 20, y: 20, width: pdfBounds.width - 40, height: pdfBounds.height - 40), withAttributes: attrs)
//        }
//
//        // 设置 PDF 页面的内容
//        page.image = image
//        pdfDocument.insert(page, at: 0)
//
//        // 设置保存路径
//        let pdfURL = FileManager.default.temporaryDirectory.appendingPathComponent("data.pdf")
//
//        // 保存 PDF 文件
//        pdfDocument.write(to: pdfURL)
//
//        print("PDF 文件保存至：\(pdfURL.path)")
//    }
//}
//
//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        PDFContentView()
//    }
//}
