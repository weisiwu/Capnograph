package com.wldmedical.capnoeasy.kits

/***
 * PDF相关能力
 * 1、展示PDF文件
 * 2、保存为PDF格式
 */

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfWriter
import com.wldmedical.capnoeasy.models.CO2WavePointData
import java.io.FileOutputStream

fun createPdf(dataList: List<CO2WavePointData>, filePath: String, watermarkText: String) {
    val document = Document()
    try {
        val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
        document.open()

        // 添加数据
        for (data in dataList) {
            val paragraph = Paragraph(
                "Index: ${data.index}, CO2: ${data.co2}, RR: ${data.RR}, ETCO2: ${data.ETCO2}, FiCO2: ${data.FiCO2}"
            )
            document.add(paragraph)
        }

//        val canvas: PdfContentByte = writer.directContentUnder
//        canvas.saveState()
//        canvas.beginText()
//
//        val font = Font(Font.FontFamily.HELVETICA, 40f, Font.BOLD, BaseColor.LIGHT_GRAY) // 自定义水印字体
//        canvas.setFontAndSize(font.baseFont, font.size)
//        canvas.showTextAligned(
//            Element.ALIGN_CENTER,
//            watermarkText,
//            300f,
//            400f,
//            45f // 旋转角度
//        )
//
//        canvas.endText()
//        canvas.restoreState()

        document.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}