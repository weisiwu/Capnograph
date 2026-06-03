package com.wldmedical.capnoeasy.kits

/***
 * PDF相关能力
 * 1、展示PDF文件
 * 2、保存为PDF格式
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.github.mikephil.charting.charts.LineChart
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfGState
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.wldmedical.capnoeasy.RR_UNIT
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.PrintSetting
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch
import kotlin.math.max
import kotlin.math.min

// 使用支持中文的字体
val fontPath = "assets/fonts/SimSun.ttf"
val baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
private const val REPORT_SEGMENT_SECONDS = 14
private const val REPORT_SEGMENT_POINTS = POINTS_PER_SECOND * REPORT_SEGMENT_SECONDS
private const val REPORT_MAX_SEGMENTS = 3
private const val REPORT_TITLE = "呼气末二氧化碳监测报告单"
private const val REPORT_ETCO2_REFERENCE = "EtCO2 参考值：32-42mmHg"

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
    private val co2Unit: String,
    private val printSetting: PrintSetting? = null,
    private val onComplete: (Boolean) -> Unit // 添加回调
) : AsyncTask<Void, Void, Boolean>() {

    private val latch = CountDownLatch(1) // 添加 CountDownLatch
    @Volatile
    private var pdfGenerationSuccess = false

    // 复制图表，并将整体数据切分为段落渲染为bitmap
    override fun onPreExecute() {
        Handler(Looper.getMainLooper()).post {
            try {
                println("wswTest 保存的pdf位置 ${filePath}")
                pdfGenerationSuccess = savePDF(filePath)
            } catch (e: Exception) {
                println("wswTest PDFKit 遇到了错误 ${e.message}")
                e.printStackTrace()
                pdfGenerationSuccess = false
            } finally {
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
        return pdfGenerationSuccess
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Boolean) {
        onComplete(result) // 调用回调函数
    }

    private data class ReportSegment(
        val points: List<CO2WavePointData>,
        val startIndex: Int
    )

    private val reportTitleFont = Font(baseFont, 18f, Font.BOLD)
    private val reportHeaderFont = Font(baseFont, 11f, Font.BOLD)
    private val reportFormFont = Font(baseFont, 10.5f, Font.NORMAL)
    private val reportSmallFont = Font(baseFont, 9.5f, Font.NORMAL)
    private val decimalFormat = DecimalFormat("0.#")
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    private fun addPDFHeader(document: Document) {
        printSetting?.hospitalName
            ?.takeIf { it.isNotBlank() }
            ?.let {
                val hospital = Paragraph(it, Font(baseFont, 12f, Font.BOLD))
                hospital.alignment = Element.ALIGN_CENTER
                hospital.spacingAfter = 2f
                document.add(hospital)
            }

        val title = Paragraph(REPORT_TITLE, reportTitleFont)
        title.alignment = Element.ALIGN_CENTER
        title.spacingAfter = 4f
        document.add(title)
        addHorizontalRule(document)
    }

    private fun addHorizontalRule(document: Document) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        val cell = PdfPCell(Phrase(""))
        cell.border = Rectangle.TOP
        cell.borderWidth = 1f
        cell.fixedHeight = 8f
        table.addCell(cell)
        document.add(table)
    }

    private fun addPDFDetail(document: Document) {
        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1.2f, 1.2f, 1.45f, 0.9f))
        table.spacingBefore = 12f
        table.spacingAfter = 28f

        addFieldCell(table, "住院号", printSetting?.pdfIDNumber, minChars = 9)
        addFieldCell(table, "床位号", printSetting?.pdfBedNumber, minChars = 8)
        addFieldCell(table, "姓名", patientName(), minChars = 8)
        addFieldCell(table, "性别", patientGender(), minChars = 4)
        addFieldCell(table, "年龄", patientAge(), suffix = "岁", minChars = 5)
        addFieldCell(table, "身高", null, suffix = "厘米", minChars = 6)
        addFieldCell(table, "体重", null, suffix = "千克", minChars = 6)
        addEmptyCell(table)

        document.add(table)
    }

    private fun addFieldCell(
        table: PdfPTable,
        label: String,
        value: String?,
        suffix: String = "",
        minChars: Int = 6
    ) {
        val phrase = Phrase()
        phrase.add(Chunk(label, reportFormFont))
        phrase.add(Chunk("  ", reportFormFont))
        phrase.add(underlinedChunk(value, minChars))
        if (suffix.isNotBlank()) {
            phrase.add(Chunk(" $suffix", reportFormFont))
        }
        val cell = PdfPCell(phrase)
        cell.border = Rectangle.NO_BORDER
        cell.paddingBottom = 14f
        table.addCell(cell)
    }

    private fun addEmptyCell(table: PdfPTable) {
        val cell = PdfPCell(Phrase(""))
        cell.border = Rectangle.NO_BORDER
        table.addCell(cell)
    }

    private fun underlinedChunk(value: String?, minChars: Int): Chunk {
        val normalized = value?.takeIf { it.isNotBlank() } ?: ""
        val text = normalized.padEnd(max(minChars, normalized.length + 2), ' ')
        return Chunk(text, reportFormFont).apply {
            setUnderline(0.5f, -2f)
        }
    }

    private fun patientName(): String? = printSetting?.name
        ?.takeIf { it.isNotBlank() }
        ?: record?.patient?.name

    private fun patientGender(): String? = printSetting?.gender
        ?.takeIf { it.isNotBlank() }
        ?: record?.patient?.gender?.title

    private fun patientAge(): String? {
        val settingAge = printSetting?.age?.takeIf { it > 0 }
        return (settingAge ?: record?.patient?.age?.takeIf { it > 0 })?.toString()
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

    private fun addWaveformSections(document: Document) {
        val segments = buildReportSegments()
        segments.forEachIndexed { index, segment ->
            addWaveformHeader(document, segment, index)
            addWaveformChart(document, segment)
            addWaveformMetrics(document, segment)
        }
    }

    private fun buildReportSegments(): List<ReportSegment> {
        if (data.isEmpty()) return emptyList()
        val pointsPerSegment = REPORT_SEGMENT_POINTS
        val segmentCount = min(REPORT_MAX_SEGMENTS, (data.size + pointsPerSegment - 1) / pointsPerSegment)
        return (0 until segmentCount).map { segmentIndex ->
            val startIndex = segmentIndex * pointsPerSegment
            val endIndex = min(data.size, startIndex + pointsPerSegment)
            ReportSegment(data.subList(startIndex, endIndex), startIndex)
        }
    }

    private fun addWaveformHeader(document: Document, segment: ReportSegment, index: Int) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = if (index == 0) 0f else 14f

        val cell = PdfPCell(Phrase("测量时间：  ${formatMeasurementRange(segment)}", reportHeaderFont))
        cell.border = Rectangle.NO_BORDER
        cell.horizontalAlignment = Element.ALIGN_LEFT
        cell.paddingBottom = 2f
        table.addCell(cell)

        document.add(table)
    }

    private fun addWaveformChart(document: Document, segment: ReportSegment) {
        val bitmap = createWaveformBitmap(segment.points)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.alignment = Element.ALIGN_CENTER
        image.scaleToFit(
            document.pageSize.width - document.leftMargin() - document.rightMargin(),
            106f
        )
        document.add(image)
    }

    private fun addWaveformMetrics(document: Document, segment: ReportSegment) {
        val latest = segment.points.lastOrNull()
        val text = "EtCO2:${formatNumber(latest?.ETCO2)} $co2Unit    " +
            "FiCO2:${formatNumber(latest?.FiCO2)} $co2Unit    " +
            "RR:${latest?.RR?.takeIf { it > 0 }?.toString() ?: "--"} $RR_UNIT    " +
            "SpO2:-- %    PR:-- $RR_UNIT"
        val paragraph = Paragraph(text, reportSmallFont)
        paragraph.spacingBefore = 2f
        paragraph.spacingAfter = 4f
        document.add(paragraph)
    }

    private fun createWaveformBitmap(points: List<CO2WavePointData>): Bitmap {
        val width = 1600
        val height = 260
        val plotLeft = 72f
        val plotTop = 12f
        val plotRight = 1536f
        val plotBottom = 204f
        val plotWidth = plotRight - plotLeft
        val plotHeight = plotBottom - plotTop
        val yMax = reportYAxisMax()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(224, 224, 224)
            strokeWidth = 1f
        }
        val majorGridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(178, 178, 178)
            strokeWidth = 1.3f
        }
        val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.BLACK
            textSize = 24f
        }

        for (i in 0..70) {
            val x = plotLeft + plotWidth * i / 70f
            val paint = if (i % 5 == 0) majorGridPaint else gridPaint
            canvas.drawLine(x, plotTop, x, plotBottom, paint)
        }
        for (i in 0..18) {
            val y = plotBottom - plotHeight * i / 18f
            val paint = if (i % 3 == 0) majorGridPaint else gridPaint
            canvas.drawLine(plotLeft, y, plotRight, y, paint)
        }
        canvas.drawRect(plotLeft, plotTop, plotRight, plotBottom, axisPaint)

        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(formatAxisNumber(yMax), plotLeft - 10f, plotTop + 8f, textPaint)
        canvas.drawText("0", plotLeft - 10f, plotBottom + 8f, textPaint)
        canvas.save()
        canvas.rotate(-90f, 25f, plotTop + plotHeight / 2f)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("($co2Unit)", 25f, plotTop + plotHeight / 2f, textPaint)
        canvas.restore()

        textPaint.textAlign = Paint.Align.CENTER
        for (second in 0..REPORT_SEGMENT_SECONDS step 2) {
            val x = plotLeft + plotWidth * second / REPORT_SEGMENT_SECONDS
            val label = if (second == REPORT_SEGMENT_SECONDS) "${second}(s)" else second.toString()
            canvas.drawText(label, x, height - 20f, textPaint)
        }

        if (points.size > 1) {
            val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                strokeWidth = 2.4f
                style = Paint.Style.STROKE
            }
            val path = Path()
            points.take(REPORT_SEGMENT_POINTS).forEachIndexed { index, point ->
                val x = plotLeft + plotWidth * index / (REPORT_SEGMENT_POINTS - 1).toFloat()
                val normalizedY = min(max(point.co2, 0f), yMax) / yMax
                val y = plotBottom - normalizedY * plotHeight
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            canvas.drawPath(path, linePaint)
        }

        return bitmap
    }

    private fun reportYAxisMax(): Float {
        if (co2Unit == "mmHg") return 90f
        return if (maxETCO2 > 0f) maxETCO2 else 90f
    }

    private fun formatAxisNumber(value: Float): String = if (value % 1f == 0f) {
        value.toInt().toString()
    } else {
        decimalFormat.format(value.toDouble())
    }

    private fun formatNumber(value: Float?): String {
        if (value == null || value.isNaN() || value.isInfinite()) return "--"
        return decimalFormat.format(value.toDouble())
    }

    private fun formatMeasurementRange(segment: ReportSegment): String {
        val start = record?.startTime ?: return "--"
        val segmentStart = start.plusNanos(indexToNanos(segment.startIndex))
        val endIndex = segment.startIndex + segment.points.size.coerceAtLeast(1)
        val segmentEnd = start.plusNanos(indexToNanos(endIndex))
        return "${segmentStart.format(fullDateTimeFormatter)}--${segmentEnd.format(timeFormatter)}"
    }

    private fun indexToNanos(index: Int): Long {
        return index.toLong() * 1_000_000_000L / POINTS_PER_SECOND
    }

    private fun addPDFFooter(document: Document) {
        val reference = Paragraph(REPORT_ETCO2_REFERENCE, reportSmallFont)
        reference.spacingBefore = 10f
        reference.spacingAfter = 34f
        document.add(reference)

        val table = PdfPTable(2)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(2.4f, 1f))
        val blankCell = PdfPCell(Phrase(""))
        blankCell.border = Rectangle.NO_BORDER
        table.addCell(blankCell)

        val signaturePhrase = Phrase()
        signaturePhrase.add(Chunk("签字：", reportFormFont))
        signaturePhrase.add(underlinedChunk(null, 9))
        val signatureCell = PdfPCell(signaturePhrase)
        signatureCell.border = Rectangle.NO_BORDER
        signatureCell.horizontalAlignment = Element.ALIGN_LEFT
        table.addCell(signatureCell)
        document.add(table)
    }

    private fun savePDF(filePath: String, watermark: Boolean = false): Boolean {
        val outputFile = File(filePath)
        val tempFile = File("${filePath}.tmp")
        var document: Document? = null
        return try {
            outputFile.parentFile?.mkdirs()
            if (tempFile.exists()) {
                tempFile.delete()
            }
            document = Document(PageSize.A4, 36f, 36f, 32f, 32f)
            val writer = PdfWriter.getInstance(document, FileOutputStream(tempFile))

            document.open()

            // 添加文件标题: 医院名称和报告名称
            addPDFHeader(document)

            // 添加记录基础信息：住院号、床位号、姓名、性别、年龄等表单字段
            addPDFDetail(document)

            // 按纸质报告单样式从记录开头绘制最多三段连续 14 秒 CO2 波形。
            addWaveformSections(document)

            // PDF页脚
            addPDFFooter(document)

            // 添加重复水印
            if (watermark) {
                addRepeatedWatermark(writer, document.pageSize)
            }

            document.close()
            if (tempFile.exists() && tempFile.length() > 0L) {
                tempFile.copyTo(outputFile, overwrite = true)
                tempFile.delete()
                val success = outputFile.exists() && outputFile.length() > 0L
                println("wswTest PDFKit 生成结果 success=$success path=${outputFile.absolutePath} size=${outputFile.length()}")
                success
            } else {
                println("wswTest PDFKit 临时PDF生成失败 path=${tempFile.absolutePath} exists=${tempFile.exists()} size=${if (tempFile.exists()) tempFile.length() else 0}")
                false
            }
        } catch (e: Exception) {
            println("wswTest PDFKit savePDF失败 path=$filePath error=${e.message}")
            e.printStackTrace()
            if (tempFile.exists()) {
                tempFile.delete()
            }
            false
        } finally {
            try {
                if (document?.isOpen == true) {
                    document.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
    co2Unit: String = "",
    context: Context,
    onComplete: (Boolean) -> Unit = {},
) {
    // PDF 报告按记录时间从左到右绘制，使用副本重排 index，避免修改数据库解压出的对象。
    val reportData = data.mapIndexed { index, item ->
        item.copy(index = index)
    }

    SaveChartToPdfTask(
        lineChart,
        data = reportData,
        filePath = filePath,
        record = record,
        maxETCO2 = maxETCO2,
        currentETCO2 = currentETCO2,
        currentRR = currentRR,
        printSetting = printSetting,
        context = context,
        showTrendingChart = showTrendingChart,
        co2Unit = co2Unit,
        onComplete = onComplete,
    ).execute()
}
