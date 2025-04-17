package com.wldmedical.capnoeasy.kits

/***
 * PDF相关能力
 * 1、展示PDF文件
 * 2、保存为PDF格式
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.compose.ui.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfGState
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.PrintSetting
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

// 使用支持中文的字体
val fontPath = "assets/fonts/SimSun.ttf"
val baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

/**
 * 过滤数据，消除数据中连续超过 10 个小于最大值 10% 的数据
 * @param data 原始数据列表
 * @param maxValue 最大值
 * @param consecutiveThreshold 连续低值数据的阈值，默认为 10
 * @param lowValuePercentage 低值百分比，默认为 0.1 (10%)
 * @return 过滤后的数据列表
 */
fun filterData(
    data: List<CO2WavePointData>,
    maxValue: Float,
    consecutiveThreshold: Int = 10,
    lowValuePercentage: Float = 0.1f
): List<CO2WavePointData> {
    if (data.isEmpty()) return data

    val filteredData = mutableListOf<CO2WavePointData>()
    var consecutiveLowValueCount = 0
    for (i in data.indices) {
        val current = data[i]
        if (current.co2 < maxValue * lowValuePercentage) {
            consecutiveLowValueCount++
            if (consecutiveLowValueCount <= consecutiveThreshold) {
                filteredData.add(current)
            }
        } else {
            consecutiveLowValueCount = 0
            filteredData.add(current)
        }
    }
    return filteredData.takeLast(maxXPoints * 2)
}

class SaveChartToPdfTask(
    private val originalLineChart: LineChart,
    private val data: List<CO2WavePointData>,
    private val filePath: String,
    private val maxETCO2: Float,
    private val currentETCO2: Float,
    private val currentRR: Int,
    private val record: Record? = null,
    private val context: Context,
    private val showTrendingChart: Boolean,
    private val printSetting: PrintSetting? = null,
    private val onComplete: (Boolean) -> Unit // 添加回调
) : AsyncTask<Void, Void, Boolean>() {

    private val latch = CountDownLatch(1) // 添加 CountDownLatch

    // 复制图表，并将整体数据切分为段落渲染为bitmap
    override fun onPreExecute() {
        Handler(Looper.getMainLooper()).post {
            try {
                println("wswTest 保存的pdf位置 ${filePath}")
                savePDF(filePath)
                latch.countDown()
            } catch (e: Exception) {
                println("wswTest PDFKit 遇到了错误 ${e.message}")
                e.printStackTrace()
                latch.countDown()
            }
        }
    }

    // 在后台执行，将bitmap转换为pdf文件
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): Boolean {
        try {
            latch.await() // 等待 Bitmap 初始化完成
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        }
        return try {
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Boolean) {
        onComplete(result) // 调用回调函数
    }

    private fun addPDFHeader(
        document: Document,
    ) {
        val titleFont = Font(baseFont, 20f, Font.BOLD)
        val contentFont = Font(baseFont, 12f, Font.NORMAL)

        // 医院名称
        printSetting?.hospitalName?.let {
            val title1 = Paragraph(it ?: "", titleFont)
            title1.alignment = Element.ALIGN_CENTER
            document.add(title1)
        }

        // 记录名称
        printSetting?.reportName?.let {
            val title2 = Paragraph(it ?: "", titleFont)
            title2.alignment = Element.ALIGN_CENTER
            document.add(title2)            
        }

        // 添加标题和表格之间的间距
        val spacingParagraph = Paragraph(" ", contentFont)
        spacingParagraph.spacingBefore = 10f // 设置标题后的间距
        document.add(spacingParagraph)
    }

    private fun addPDFDetail(document: Document) {
        val contentFont = Font(baseFont, 12f, Font.NORMAL)

        // 创建一个三列的表格
        val table = PdfPTable(3)
        // 设置表格宽度为页面宽度
        table.widthPercentage = 100f
        table.horizontalAlignment = Element.ALIGN_CENTER  // 关键：设置表格整体水平居中
        // 设置表格的列宽比例，这里设置为 1:1:1，表示三列等宽
        table.setWidths(floatArrayOf(1f, 1f, 1f))

        // 添加表头单元格
        val nameCell = PdfPCell(Paragraph("${getString(R.string.pdf_patient_name, context)}${printSetting?.name ?: ""}", contentFont))
        nameCell.horizontalAlignment = Element.ALIGN_CENTER
        nameCell.border = Rectangle.NO_BORDER
        table.addCell(nameCell)

        val genderCell = PdfPCell(Paragraph("${getString(R.string.pdf_patient_gender, context)}${printSetting?.gender ?: ""}", contentFont))
        genderCell.horizontalAlignment = Element.ALIGN_CENTER
        genderCell.border = Rectangle.NO_BORDER
        table.addCell(genderCell)

        val ageCell = PdfPCell(Paragraph("${getString(R.string.pdf_patient_age, context)}${printSetting?.age ?: ""}${getString(R.string.pdf_patient_age_unit, context)}", contentFont))
        ageCell.horizontalAlignment = Element.ALIGN_CENTER
        ageCell.border = Rectangle.NO_BORDER
        table.addCell(ageCell)

        val departCell = PdfPCell(Paragraph("${getString(R.string.pdf_department, context)}${printSetting?.pdfDepart ?: ""}", contentFont))
        departCell.horizontalAlignment = Element.ALIGN_CENTER
        departCell.border = Rectangle.NO_BORDER
        table.addCell(departCell)

        val idCell = PdfPCell(Paragraph("${getString(R.string.pdf_id_number, context)}${printSetting?.pdfIDNumber ?: ""}", contentFont))
        idCell.horizontalAlignment = Element.ALIGN_CENTER
        idCell.border = Rectangle.NO_BORDER
        table.addCell(idCell)

        val bedCell = PdfPCell(Paragraph("${getString(R.string.pdf_bed_number, context)}${printSetting?.pdfBedNumber ?: ""}", contentFont))
        bedCell.horizontalAlignment = Element.ALIGN_CENTER
        bedCell.border = Rectangle.NO_BORDER
        table.addCell(bedCell)

        // 将表格添加到文档中
        document.add(table)
    }

    private fun addPDFETCO2(document: Document) {
        val contentFont = Font(baseFont, 12f, Font.NORMAL)

        // 设置上间距
        val upperSpacingParagraph = Paragraph(" ", contentFont)
        upperSpacingParagraph.spacingBefore = 10f
        document.add(upperSpacingParagraph)

        val table = PdfPTable(2)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1f, 1f))

        val ETCO2Cell = PdfPCell(Paragraph("ETCO2：${currentETCO2}", contentFont))
        ETCO2Cell.horizontalAlignment = Element.ALIGN_CENTER
        ETCO2Cell.border = Rectangle.NO_BORDER
        table.addCell(ETCO2Cell)

        val RRCell = PdfPCell(Paragraph("${getString(R.string.pdf_respiratory_rate, context)}${currentRR}", contentFont))
        RRCell.horizontalAlignment = Element.ALIGN_CENTER
        RRCell.border = Rectangle.NO_BORDER
        table.addCell(RRCell)

        document.add(table)
    }

    private fun addRepeatedWatermark(writer: PdfWriter, pageSize: Rectangle) {
        val fontPath = "assets/fonts/SimSun.ttf" // 确保路径正确
        val baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        val fontSize = 24f

        val text = "万联达仪器"
        val watermarkWidth = baseFont.getWidthPoint(text, fontSize) // 计算文本宽度
        val watermarkHeight = fontSize * 1.2f // 粗略计算文本高度
        val rotation = 45f // 旋转角度

        val xSpacing = watermarkWidth * 2f // 水印之间的水平间距
        val ySpacing = watermarkHeight * 5f  // 水印之间的垂直间距

        val startX = -pageSize.width / 4 // 起始位置
        val startY = -pageSize.height / 4 // 起始位置

        val canvas = writer.directContentUnder

        // 设置字体透明度
        val gState = PdfGState()
        gState.setFillOpacity(0.3f) // 设置填充透明度，0.0（完全透明）到 1.0（不透明）
        gState.setStrokeOpacity(0.3f) // 设置描边透明度（可选）

        // 应用透明度
        canvas.saveState()
        canvas.setGState(gState)

        canvas.beginText()
        canvas.setFontAndSize(baseFont, fontSize)

        for (x in generateSequence(startX) { it + xSpacing }.takeWhile { it < pageSize.width * 1.5 }) {
            for (y in generateSequence(startY) { it + ySpacing }.takeWhile { it < pageSize.height * 1.5 }) {
                canvas.showTextAligned(Element.ALIGN_CENTER, text, x, y, rotation)
            }
        }

        canvas.endText()
        // 恢复状态（必须与 saveState 成对出现）
        canvas.restoreState()
    }

    private val width = 1000 // 设置宽度
    private val height = 800 // 设置高度
    private val imgRatio = 0.6f // 折线图相对于页面的比例

    private fun addETCO2LineChart(document: Document) {
        val filteredData = filterData(data, maxETCO2)
        val totalPoints = filteredData.size
        val currentPageData = data.subList(0, totalPoints.coerceAtLeast(0))

        // 生成当前页的 Bitmap
        val currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(currentPageBitmap)

        // 绘制当前页的图表
        val copyLineChart = LineChart(originalLineChart.context)
        copyLineChart.setBackgroundColor(Color.Transparent.value.toInt())
        copyLineChart.xAxis.position = originalLineChart.xAxis.position
        copyLineChart.axisRight.isEnabled = originalLineChart.axisRight.isEnabled
        copyLineChart.description.isEnabled = originalLineChart.description.isEnabled
        copyLineChart.axisLeft.axisMinimum = originalLineChart.axisLeft.axisMinimum
        copyLineChart.axisLeft.axisMaximum = originalLineChart.axisLeft.axisMaximum
        copyLineChart.xAxis.valueFormatter = originalLineChart.xAxis.valueFormatter
        copyLineChart.axisLeft.valueFormatter = originalLineChart.axisLeft.valueFormatter
        copyLineChart.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        copyLineChart.layout(0, 0, width, height)
        copyLineChart.requestLayout()

        // 绘制当前页的数据
        val segment = currentPageData.map {
            Entry(it.index.toFloat(), it.co2)
        }

        val dataSet = LineDataSet(segment, "ETCO2")
        dataSet.lineWidth = 1f
        dataSet.setDrawCircles(false) // 不绘制圆点
        dataSet.setDrawValues(false) // 不绘制具体的值
        val lineData = LineData(dataSet)
        copyLineChart.data = lineData
        copyLineChart.invalidate()
        copyLineChart.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        copyLineChart.layout(0, 0, width, height)
        copyLineChart.draw(canvas)

        // 添加图像到文档
        val stream = ByteArrayOutputStream()
        currentPageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())

        image.scaleToFit(document.pageSize.width * imgRatio, document.pageSize.height * imgRatio)

        // 计算水平居中位置
        val xPosition = (document.pageSize.width - image.scaledWidth) / 2

        // 设置图片位置（保持当前Y位置不变，只调整X）
        image.setAbsolutePosition(xPosition, image.absoluteY)

        document.add(image)
    }

    private fun addETCO2TrendChart(document: Document) {
        // 生成趋势数据
        val newTrendEntries = mutableListOf<Entry>()
        var sequentialTrendIndex = 0
        for (i in data.indices step 50) {
            try {
                // 安全访问数组元素
                val item = data.getOrNull(i)
                if (item != null) {
                    newTrendEntries.add(Entry(sequentialTrendIndex.toFloat(), item.ETCO2))
                    sequentialTrendIndex++
                } else {
                    println("Warning: Index $i out of bounds")
                }
            } catch (e: Exception) {
                println("Error at index $i: ${e.javaClass.simpleName} - ${e.message}")
            }
        }

        // 生成当前页的 Bitmap
        val currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(currentPageBitmap)

        // 绘制当前页的图表
        val copyLineChart = LineChart(originalLineChart.context)
        copyLineChart.setBackgroundColor(Color.Transparent.value.toInt())
        copyLineChart.xAxis.position = originalLineChart.xAxis.position
        copyLineChart.axisRight.isEnabled = originalLineChart.axisRight.isEnabled
        copyLineChart.description.isEnabled = originalLineChart.description.isEnabled
        copyLineChart.axisLeft.axisMinimum = originalLineChart.axisLeft.axisMinimum
        copyLineChart.axisLeft.axisMaximum = originalLineChart.axisLeft.axisMaximum
        copyLineChart.xAxis.valueFormatter = originalLineChart.xAxis.valueFormatter
        copyLineChart.axisLeft.valueFormatter = originalLineChart.axisLeft.valueFormatter
        copyLineChart.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        copyLineChart.layout(0, 0, width, height)
        copyLineChart.requestLayout()

        // 绘制当前页的数据
        val dataSet = LineDataSet(newTrendEntries, getString(R.string.chart_trending, context))
        dataSet.lineWidth = 1f
        dataSet.setDrawCircles(false) // 不绘制圆点
        dataSet.setDrawValues(false) // 不绘制具体的值
        val lineData = LineData(dataSet)
        copyLineChart.data = lineData
        copyLineChart.invalidate()
        copyLineChart.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        copyLineChart.layout(0, 0, width, height)
        copyLineChart.draw(canvas)

        // 添加图像到文档
        val stream = ByteArrayOutputStream()
        currentPageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.scaleToFit(document.pageSize.width * imgRatio, document.pageSize.height * imgRatio)

        // 计算水平居中位置
        val xPosition = (document.pageSize.width - image.scaledWidth) / 2

        // 设置图片位置（保持当前Y位置不变，只调整X）
        image.setAbsolutePosition(xPosition, image.absoluteY)

        document.add(image)
    }

    private fun addPDFFooter(document: Document) {
        val contentFont = Font(baseFont, 12f, Font.NORMAL)

        val table = PdfPTable(2)
        table.widthPercentage = 100f
        table.horizontalAlignment = Element.ALIGN_CENTER  // 关键：设置表格整体水平居中
        table.setWidths(floatArrayOf(1f, 1f))

        val doctorCell = PdfPCell(Paragraph(getString(R.string.pdf_reporter, context), contentFont))
        doctorCell.horizontalAlignment = Element.ALIGN_CENTER
        doctorCell.border = Rectangle.NO_BORDER
        table.addCell(doctorCell)

        val formatter = DateTimeFormatter.ofPattern(getString(R.string.pdf_date_format, context))
        val endTimeStr = if (record?.endTime != null) record.endTime.format(formatter) else ""
        val timeCell = PdfPCell(Paragraph("${getString(R.string.pdf_report_time, context)}${endTimeStr}", contentFont))
        timeCell.horizontalAlignment = Element.ALIGN_LEFT
        timeCell.border = Rectangle.NO_BORDER
        table.addCell(timeCell)

        // 将表格添加到文档中
        document.add(table)
    }

    private fun savePDF(filePath: String, watermark: Boolean = false) {
        try {
            val document = Document()
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))

            document.open()

            // 添加文件标题: 医院名称和报告名称
            addPDFHeader(document)

            // 添加记录基础信息：姓名、性别、年龄、科室、ID、床号
            addPDFDetail(document)

            // 打印波形图相关的ETCO2、RR值
            addPDFETCO2(document)

            // 趋势图
            if (showTrendingChart) {
                addETCO2TrendChart(document)
            }

            // 保存ETCO2曲线图
            // 当前会压缩波形数据，将波形数据中，连续多少个小于XX的数据去除
            addETCO2LineChart(document)

            // PDF页脚
            addPDFFooter(document)

            // 添加重复水印
            if (watermark) {
                addRepeatedWatermark(writer, document.pageSize)
            }

            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun saveChartToPdfInBackground(
    lineChart: LineChart,
    data: List<CO2WavePointData>,
    filePath: String,
    maxETCO2: Float = 0f,
    currentETCO2: Float = 0f,
    currentRR: Int = 0,
    printSetting: PrintSetting? = null,
    record: Record? = null,
    showTrendingChart: Boolean = true,
    context: Context,
) {
    // 反转列表
    val reversedList = data.filterNotNull().asReversed()

    // 更新 index 值
    for (i in reversedList.indices) {
        val item = reversedList[i]
        if (item != null) {
            item.index = i
        }
    }

    SaveChartToPdfTask(
        lineChart,
        data = reversedList,
        filePath = filePath,
        record = record,
        maxETCO2 = maxETCO2,
        currentETCO2 = currentETCO2,
        currentRR = currentRR,
        printSetting = printSetting,
        context = context,
        showTrendingChart = showTrendingChart,
        onComplete = {},
    ).execute()
}