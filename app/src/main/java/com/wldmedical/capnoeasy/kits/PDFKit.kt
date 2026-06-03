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
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.RR_UNIT
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.hotmeltprint.PrintSetting
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch
import kotlin.math.max
import kotlin.math.min

// 使用支持中文的字体
val fontPath = "assets/fonts/SimSun.ttf"
val baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

private data class PdfWatermarkConfig(
    val text: String,
    val opacity: Float
)

private data class PdfReportTemplateConfig(
    val mode: String = PrintSetting.PDF_TEMPLATE_OFFICIAL,
    val title: String = "呼气末二氧化碳监测报告单",
    val pageSize: Rectangle = PageSize.A4,
    val pageMarginLeft: Float = 36f,
    val pageMarginRight: Float = 36f,
    val pageMarginTop: Float = 32f,
    val pageMarginBottom: Float = 32f,
    val hospitalFontSize: Float = 12f,
    val titleFontSize: Float = 18f,
    val headerFontSize: Float = 11f,
    val formFontSize: Float = 10.5f,
    val smallFontSize: Float = 9.5f,
    val hospitalSpacingAfter: Float = 2f,
    val titleSpacingAfter: Float = 4f,
    val segmentSeconds: Int = 14,
    val maxSegments: Int = 3,
    val detailSectionHeight: Float = 190f,
    val summarySectionHeight: Float = 44f,
    val waveformSectionHeight: Float = 150f,
    val footerSectionHeight: Float = 92f,
    val detailColumnWidths: List<Float> = listOf(1f, 1f),
    val detailSpacingBefore: Float = 12f,
    val detailSpacingAfter: Float = 22f,
    val fieldPaddingBottom: Float = 14f,
    val horizontalRuleHeight: Float = 8f,
    val horizontalRuleBorderWidth: Float = 1f,
    val summaryColumnWidths: List<Float> = listOf(1f, 1f, 1f),
    val summarySpacingAfter: Float = 12f,
    val summaryTitleSpacingBefore: Float = 0f,
    val summaryTitleSpacingAfter: Float = 4f,
    val summaryCellPaddingBottom: Float = 6f,
    val waveformHeaderSpacingBefore: Float = 14f,
    val waveformHeaderPaddingBottom: Float = 2f,
    val waveformMetricsSpacingBefore: Float = 2f,
    val waveformMetricsSpacingAfter: Float = 4f,
    val waveformImageMaxHeight: Float = 106f,
    val waveformBitmapWidth: Int = 1600,
    val waveformBitmapHeight: Int = 260,
    val waveformPlotLeft: Float = 72f,
    val waveformPlotTop: Float = 12f,
    val waveformPlotRight: Float = 1536f,
    val waveformPlotBottom: Float = 204f,
    val waveformGridVerticalLines: Int = 70,
    val waveformGridHorizontalLines: Int = 18,
    val waveformMajorGridVerticalEvery: Int = 5,
    val waveformMajorGridHorizontalEvery: Int = 3,
    val waveformXAxisLabelStepSeconds: Int = 2,
    val waveformYAxisMajorTicks: Int = 3,
    val waveformAxisTextSize: Float = 24f,
    val waveformGridStrokeWidth: Float = 1f,
    val waveformMajorGridStrokeWidth: Float = 1.3f,
    val waveformAxisStrokeWidth: Float = 2f,
    val waveformLineStrokeWidth: Float = 2.4f,
    val footerReferenceSpacingBefore: Float = 10f,
    val footerReferenceSpacingAfter: Float = 34f,
    val footerSignatureColumnWidths: List<Float> = listOf(2.4f, 1f),
    val defaultWatermarkEnabled: Boolean = false,
    val defaultWatermarkText: String = PrintSetting.DEFAULT_PDF_WATERMARK_TEXT,
    val defaultWatermarkOpacity: Float = PrintSetting.DEFAULT_PDF_WATERMARK_OPACITY,
    val watermarkFontSize: Float = 24f,
    val watermarkRotation: Float = 45f,
    val watermarkHorizontalSpacingMultiplier: Float = 2f,
    val watermarkVerticalSpacingMultiplier: Float = 5f,
) {
    val segmentPoints: Int
        get() = POINTS_PER_SECOND * segmentSeconds

    val segmentMillis: Long
        get() = segmentSeconds * 1000L
}

private val PDF_OFFICIAL_REPORT_TEMPLATE_CONFIG = PdfReportTemplateConfig()
private val PDF_DEBUG_REPORT_TEMPLATE_CONFIG = PDF_OFFICIAL_REPORT_TEMPLATE_CONFIG.copy(
    mode = PrintSetting.PDF_TEMPLATE_DEBUG,
    defaultWatermarkEnabled = true
)

private fun pdfReportTemplateConfigFor(mode: String?): PdfReportTemplateConfig {
    return when (mode) {
        PrintSetting.PDF_TEMPLATE_DEBUG -> PDF_DEBUG_REPORT_TEMPLATE_CONFIG
        else -> PDF_OFFICIAL_REPORT_TEMPLATE_CONFIG
    }
}

private const val REPORT_ETCO2_REFERENCE_LABEL = "EtCO2 参考值"
private const val REPORT_ETCO2_REFERENCE_MIN_MMHG = 32f
private const val REPORT_ETCO2_REFERENCE_MAX_MMHG = 42f
private const val KPA_PER_MMHG = 0.133322f
private const val PERCENT_PER_MMHG = 0.13157895f
private const val REPORT_UNFILLED_VALUE = "未填写"

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
    private val deviceSerial: String? = null,
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
        val startIndex: Int,
        val startMillis: Long? = null,
        val endMillis: Long? = null
    )

    private data class MetricStats(
        val average: Float?,
        val max: Float?,
        val min: Float?,
        val end: Float?
    )

    private data class ReportMetrics(
        val etco2: MetricStats,
        val fico2: MetricStats,
        val rr: MetricStats
    )

    private val templateConfig = pdfReportTemplateConfigFor(printSetting?.pdfTemplateMode)
    private val reportTitleFont = Font(baseFont, templateConfig.titleFontSize, Font.BOLD)
    private val reportHeaderFont = Font(baseFont, templateConfig.headerFontSize, Font.BOLD)
    private val reportFormFont = Font(baseFont, templateConfig.formFontSize, Font.NORMAL)
    private val reportSmallFont = Font(baseFont, templateConfig.smallFontSize, Font.NORMAL)
    private val decimalFormat = DecimalFormat("0.#")
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val reportGeneratedAt: LocalDateTime = LocalDateTime.now()

    private fun addPDFHeader(document: Document) {
        printSetting?.hospitalName
            ?.takeIf { it.isNotBlank() }
            ?.let {
                val hospital = Paragraph(it, Font(baseFont, templateConfig.hospitalFontSize, Font.BOLD))
                hospital.alignment = Element.ALIGN_CENTER
                hospital.spacingAfter = templateConfig.hospitalSpacingAfter
                document.add(hospital)
            }

        val title = Paragraph(templateConfig.title, reportTitleFont)
        title.alignment = Element.ALIGN_CENTER
        title.spacingAfter = templateConfig.titleSpacingAfter
        document.add(title)
        addHorizontalRule(document)
    }

    private fun addHorizontalRule(document: Document) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        val cell = PdfPCell(Phrase(""))
        cell.border = Rectangle.TOP
        cell.borderWidth = templateConfig.horizontalRuleBorderWidth
        cell.fixedHeight = templateConfig.horizontalRuleHeight
        table.addCell(cell)
        document.add(table)
    }

    private fun addPDFDetail(document: Document) {
        val table = PdfPTable(templateConfig.detailColumnWidths.size)
        table.widthPercentage = 100f
        table.setWidths(templateConfig.detailColumnWidths.toFloatArray())
        table.spacingBefore = templateConfig.detailSpacingBefore
        table.spacingAfter = templateConfig.detailSpacingAfter

        addFieldCell(table, "住院号", printSetting?.pdfIDNumber, minChars = 9)
        addFieldCell(table, "床位号", printSetting?.pdfBedNumber, minChars = 8)
        addFieldCell(table, "科室", displayValue(printSetting?.pdfDepart), minChars = 8)
        addFieldCell(table, "姓名", patientName(), minChars = 8)
        addFieldCell(table, "性别", patientGender(), minChars = 4)
        addFieldCell(table, "年龄", patientAge(), suffix = "岁", minChars = 5)
        addFieldCell(table, "身高", REPORT_UNFILLED_VALUE, suffix = "厘米", minChars = 6)
        addFieldCell(table, "体重", REPORT_UNFILLED_VALUE, suffix = "千克", minChars = 6)
        addFieldCell(table, "开始时间", formatRecordDateTime(record?.startTime), minChars = 19)
        addFieldCell(table, "结束时间", formatRecordDateTime(record?.endTime), minChars = 19)
        addFieldCell(table, "检测时长", formatRecordDuration(), minChars = 8)
        addFieldCell(table, "报告时间", formatRecordDateTime(reportGeneratedAt), minChars = 19)
        addFieldCell(table, "设备编号", displayValue(deviceSerial), minChars = 12)
        addEmptyCell(table)

        document.add(table)
    }

    private fun addReportSummary(document: Document) {
        if (data.isEmpty()) return
        val summary = buildMetrics(data)

        val title = Paragraph("全程摘要（均/最大/最小/段末）", reportHeaderFont)
        title.spacingBefore = templateConfig.summaryTitleSpacingBefore
        title.spacingAfter = templateConfig.summaryTitleSpacingAfter
        document.add(title)

        val table = PdfPTable(templateConfig.summaryColumnWidths.size)
        table.widthPercentage = 100f
        table.setWidths(templateConfig.summaryColumnWidths.toFloatArray())
        table.spacingAfter = templateConfig.summarySpacingAfter

        addSummaryCell(table, "EtCO2", summary.etco2, co2Unit)
        addSummaryCell(table, "FiCO2", summary.fico2, co2Unit)
        addSummaryCell(table, "RR", summary.rr, RR_UNIT)

        document.add(table)
    }

    private fun addSummaryCell(table: PdfPTable, label: String, stats: MetricStats, unit: String) {
        val cell = PdfPCell(Phrase("$label：${formatMetricStats(stats)} $unit", reportSmallFont))
        cell.border = Rectangle.NO_BORDER
        cell.paddingBottom = templateConfig.summaryCellPaddingBottom
        table.addCell(cell)
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
        cell.paddingBottom = templateConfig.fieldPaddingBottom
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

    private fun displayValue(value: String?): String {
        return value?.takeIf { it.isNotBlank() } ?: REPORT_UNFILLED_VALUE
    }

    private fun formatRecordDateTime(value: LocalDateTime?): String {
        return value?.format(fullDateTimeFormatter) ?: REPORT_UNFILLED_VALUE
    }

    private fun formatRecordDuration(): String {
        val start = record?.startTime ?: return REPORT_UNFILLED_VALUE
        val end = record.endTime
        val totalSeconds = Duration.between(start, end).seconds.coerceAtLeast(0L)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return when {
            hours > 0 -> "${hours}小时${minutes}分${seconds}秒"
            minutes > 0 -> "${minutes}分${seconds}秒"
            else -> "${seconds}秒"
        }
    }

    private inner class WatermarkPageEvent(
        private val watermarkConfig: PdfWatermarkConfig
    ) : PdfPageEventHelper() {
        override fun onEndPage(writer: PdfWriter, document: Document) {
            addRepeatedWatermark(writer, document.pageSize, watermarkConfig)
        }
    }

    private fun addRepeatedWatermark(
        writer: PdfWriter,
        pageSize: Rectangle,
        watermarkConfig: PdfWatermarkConfig
    ) {
        val fontSize = templateConfig.watermarkFontSize
        val text = watermarkConfig.text
        val watermarkWidth = baseFont.getWidthPoint(text, fontSize)
        val watermarkHeight = fontSize * 1.2f
        val rotation = templateConfig.watermarkRotation

        val xSpacing = watermarkWidth * templateConfig.watermarkHorizontalSpacingMultiplier
        val ySpacing = watermarkHeight * templateConfig.watermarkVerticalSpacingMultiplier

        val startX = -pageSize.width / 4 // 起始位置
        val startY = -pageSize.height / 4 // 起始位置

        val canvas = writer.directContentUnder

        // 设置字体透明度
        val gState = PdfGState()
        gState.setFillOpacity(watermarkConfig.opacity)
        gState.setStrokeOpacity(watermarkConfig.opacity)

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

    private fun resolveWatermarkConfig(): PdfWatermarkConfig? {
        val enabled = printSetting?.pdfWatermarkEnabled ?: templateConfig.defaultWatermarkEnabled
        if (!enabled) return null

        val text = printSetting?.pdfWatermarkText
            ?.takeIf { it.isNotBlank() }
            ?: templateConfig.defaultWatermarkText
        val opacity = (printSetting?.pdfWatermarkOpacity ?: templateConfig.defaultWatermarkOpacity)
            .coerceIn(0f, 1f)

        if (text.isBlank() || opacity <= 0f) return null
        return PdfWatermarkConfig(text = text, opacity = opacity)
    }

    private fun addWaveformSections(document: Document, writer: PdfWriter) {
        val segments = buildReportSegments()
        segments.forEachIndexed { index, segment ->
            addReportSection(document, writer, templateConfig.waveformSectionHeight) {
                addWaveformHeader(document, segment, index)
                addWaveformChart(document, segment)
                addWaveformMetrics(document, segment)
            }
        }
    }

    private fun buildReportSegments(): List<ReportSegment> {
        if (data.isEmpty()) return emptyList()
        if (data.all { it.sampleTimeMillis > 0L }) {
            return buildTimedReportSegments()
        }
        return buildIndexedReportSegments()
    }

    private fun buildTimedReportSegments(): List<ReportSegment> {
        val firstMillis = data.first().sampleTimeMillis
        val lastMillis = data.last().sampleTimeMillis
        val segmentCount = min(
            templateConfig.maxSegments,
            (((lastMillis - firstMillis).coerceAtLeast(0L) / templateConfig.segmentMillis) + 1).toInt()
        )
        return (0 until segmentCount).mapNotNull { segmentIndex ->
            val segmentStart = firstMillis + segmentIndex * templateConfig.segmentMillis
            val segmentLimit = segmentStart + templateConfig.segmentMillis
            val segmentPoints = data.filter { point ->
                point.sampleTimeMillis >= segmentStart && point.sampleTimeMillis < segmentLimit
            }
            if (segmentPoints.isEmpty()) {
                null
            } else {
                ReportSegment(
                    points = segmentPoints,
                    startIndex = segmentPoints.first().index,
                    startMillis = segmentStart,
                    endMillis = min(segmentLimit, lastMillis)
                )
            }
        }
    }

    private fun buildIndexedReportSegments(): List<ReportSegment> {
        val pointsPerSegment = templateConfig.segmentPoints
        val segmentCount = min(templateConfig.maxSegments, (data.size + pointsPerSegment - 1) / pointsPerSegment)
        return (0 until segmentCount).map { segmentIndex ->
            val startIndex = segmentIndex * pointsPerSegment
            val endIndex = min(data.size, startIndex + pointsPerSegment)
            ReportSegment(data.subList(startIndex, endIndex), startIndex)
        }
    }

    private fun addWaveformHeader(document: Document, segment: ReportSegment, index: Int) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = if (index == 0) 0f else templateConfig.waveformHeaderSpacingBefore

        val cell = PdfPCell(Phrase("测量时间：  ${formatMeasurementRange(segment)}", reportHeaderFont))
        cell.border = Rectangle.NO_BORDER
        cell.horizontalAlignment = Element.ALIGN_LEFT
        cell.paddingBottom = templateConfig.waveformHeaderPaddingBottom
        table.addCell(cell)

        document.add(table)
    }

    private fun addWaveformChart(document: Document, segment: ReportSegment) {
        val bitmap = createWaveformBitmap(segment)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.alignment = Element.ALIGN_CENTER
        image.scaleToFit(
            document.pageSize.width - document.leftMargin() - document.rightMargin(),
            templateConfig.waveformImageMaxHeight
        )
        document.add(image)
    }

    private fun addWaveformMetrics(document: Document, segment: ReportSegment) {
        val metrics = buildMetrics(segment.points)
        val text = "段统计（均/最大/最小/段末）： " +
            "EtCO2 ${formatMetricStats(metrics.etco2)} $co2Unit    " +
            "FiCO2 ${formatMetricStats(metrics.fico2)} $co2Unit    " +
            "RR ${formatMetricStats(metrics.rr)} $RR_UNIT"
        val paragraph = Paragraph(text, reportSmallFont)
        paragraph.spacingBefore = templateConfig.waveformMetricsSpacingBefore
        paragraph.spacingAfter = templateConfig.waveformMetricsSpacingAfter
        document.add(paragraph)
    }

    private fun buildMetrics(points: List<CO2WavePointData>): ReportMetrics {
        return ReportMetrics(
            etco2 = metricStats(points) { it.ETCO2 },
            fico2 = metricStats(points) { it.FiCO2 },
            rr = metricStats(points) { point -> point.RR.takeIf { it > 0 }?.toFloat() }
        )
    }

    private fun metricStats(
        points: List<CO2WavePointData>,
        selector: (CO2WavePointData) -> Float?
    ): MetricStats {
        val values = points.mapNotNull { point ->
            selector(point)?.takeIf { isValidMetricValue(it) }
        }
        if (values.isEmpty()) {
            return MetricStats(average = null, max = null, min = null, end = null)
        }
        return MetricStats(
            average = values.sum() / values.size,
            max = values.maxOrNull(),
            min = values.minOrNull(),
            end = values.lastOrNull()
        )
    }

    private fun isValidMetricValue(value: Float): Boolean {
        return !value.isNaN() && !value.isInfinite()
    }

    private fun createWaveformBitmap(segment: ReportSegment): Bitmap {
        val points = segment.points
        val width = templateConfig.waveformBitmapWidth
        val height = templateConfig.waveformBitmapHeight
        val plotLeft = templateConfig.waveformPlotLeft
        val plotTop = templateConfig.waveformPlotTop
        val plotRight = templateConfig.waveformPlotRight
        val plotBottom = templateConfig.waveformPlotBottom
        val plotWidth = plotRight - plotLeft
        val plotHeight = plotBottom - plotTop
        val yMax = reportYAxisMax()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(224, 224, 224)
            strokeWidth = templateConfig.waveformGridStrokeWidth
        }
        val majorGridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.rgb(178, 178, 178)
            strokeWidth = templateConfig.waveformMajorGridStrokeWidth
        }
        val referenceRangePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(38, 42, 148, 88)
            style = Paint.Style.FILL
        }
        val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.BLACK
            strokeWidth = templateConfig.waveformAxisStrokeWidth
            style = Paint.Style.STROKE
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.BLACK
            textSize = templateConfig.waveformAxisTextSize
        }

        drawEtco2ReferenceRange(canvas, plotLeft, plotRight, plotBottom, plotHeight, yMax, referenceRangePaint)

        for (i in 0..templateConfig.waveformGridVerticalLines) {
            val x = plotLeft + plotWidth * i / templateConfig.waveformGridVerticalLines
            val paint = if (i % templateConfig.waveformMajorGridVerticalEvery == 0) majorGridPaint else gridPaint
            canvas.drawLine(x, plotTop, x, plotBottom, paint)
        }
        for (i in 0..templateConfig.waveformGridHorizontalLines) {
            val y = plotBottom - plotHeight * i / templateConfig.waveformGridHorizontalLines
            val paint = if (i % templateConfig.waveformMajorGridHorizontalEvery == 0) majorGridPaint else gridPaint
            canvas.drawLine(plotLeft, y, plotRight, y, paint)
        }
        canvas.drawRect(plotLeft, plotTop, plotRight, plotBottom, axisPaint)

        textPaint.textAlign = Paint.Align.RIGHT
        yAxisTickValues(yMax).forEach { value ->
            val y = plotBottom - plotHeight * value / yMax
            val baseline = (y + 8f).coerceIn(plotTop + 8f, plotBottom + 8f)
            canvas.drawText(formatAxisNumber(value), plotLeft - 10f, baseline, textPaint)
        }
        canvas.save()
        canvas.rotate(-90f, 25f, plotTop + plotHeight / 2f)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("($co2Unit)", 25f, plotTop + plotHeight / 2f, textPaint)
        canvas.restore()

        textPaint.textAlign = Paint.Align.CENTER
        for (second in 0..templateConfig.segmentSeconds step templateConfig.waveformXAxisLabelStepSeconds) {
            val x = plotLeft + plotWidth * second / templateConfig.segmentSeconds
            val label = if (second == templateConfig.segmentSeconds) "${second}(s)" else second.toString()
            canvas.drawText(label, x, height - 20f, textPaint)
        }

        if (points.size > 1) {
            val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                strokeWidth = templateConfig.waveformLineStrokeWidth
                style = Paint.Style.STROKE
            }
            val path = Path()
            points.take(templateConfig.segmentPoints).forEachIndexed { index, point ->
                val x = waveformX(point, index, segment, plotLeft, plotWidth)
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

    private fun waveformX(
        point: CO2WavePointData,
        fallbackIndex: Int,
        segment: ReportSegment,
        plotLeft: Float,
        plotWidth: Float
    ): Float {
        val segmentStart = segment.startMillis
        return if (segmentStart != null && point.sampleTimeMillis > 0L) {
            val elapsedMillis = (point.sampleTimeMillis - segmentStart)
                .coerceIn(0L, templateConfig.segmentMillis)
            plotLeft + plotWidth * elapsedMillis / templateConfig.segmentMillis.toFloat()
        } else {
            val pointDenominator = (templateConfig.segmentPoints - 1).coerceAtLeast(1).toFloat()
            plotLeft + plotWidth * fallbackIndex / pointDenominator
        }
    }

    private fun reportYAxisMax(): Float {
        if (co2Unit == CO2_UNIT.MMHG.value) return 90f
        return if (maxETCO2 > 0f) maxETCO2 else 90f
    }

    private fun drawEtco2ReferenceRange(
        canvas: Canvas,
        plotLeft: Float,
        plotRight: Float,
        plotBottom: Float,
        plotHeight: Float,
        yMax: Float,
        paint: Paint
    ) {
        if (yMax <= 0f) return
        val (referenceMin, referenceMax) = etco2ReferenceRangeValues() ?: return
        val low = referenceMin.coerceIn(0f, yMax)
        val high = referenceMax.coerceIn(0f, yMax)
        if (high <= low) return

        val top = plotBottom - plotHeight * high / yMax
        val bottom = plotBottom - plotHeight * low / yMax
        canvas.drawRect(plotLeft, top, plotRight, bottom, paint)
    }

    private fun yAxisTickValues(yMax: Float): List<Float> {
        if (yMax <= 0f) return listOf(0f)
        val tickCount = templateConfig.waveformYAxisMajorTicks.coerceAtLeast(1)
        return (0..tickCount).map { index ->
            yMax * index / tickCount
        }
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

    private fun formatMetricStats(stats: MetricStats): String {
        return listOf(stats.average, stats.max, stats.min, stats.end)
            .joinToString("/") { formatNumber(it) }
    }

    private fun etco2ReferenceText(): String? {
        val unit = co2Unit.takeIf { it.isNotBlank() } ?: return null
        val (minValue, maxValue) = etco2ReferenceRangeValues() ?: return null
        return "$REPORT_ETCO2_REFERENCE_LABEL：${formatReferenceNumber(minValue)}-${formatReferenceNumber(maxValue)}$unit"
    }

    private fun etco2ReferenceRangeValues(): Pair<Float, Float>? {
        return when (co2Unit) {
            CO2_UNIT.MMHG.value -> Pair(REPORT_ETCO2_REFERENCE_MIN_MMHG, REPORT_ETCO2_REFERENCE_MAX_MMHG)
            CO2_UNIT.KPA.value -> Pair(
                REPORT_ETCO2_REFERENCE_MIN_MMHG * KPA_PER_MMHG,
                REPORT_ETCO2_REFERENCE_MAX_MMHG * KPA_PER_MMHG
            )
            CO2_UNIT.PERCENT.value -> Pair(
                REPORT_ETCO2_REFERENCE_MIN_MMHG * PERCENT_PER_MMHG,
                REPORT_ETCO2_REFERENCE_MAX_MMHG * PERCENT_PER_MMHG
            )
            else -> null
        }
    }

    private fun formatReferenceNumber(value: Float): String {
        return decimalFormat.format(value.toDouble())
    }

    private fun formatMeasurementRange(segment: ReportSegment): String {
        if (segment.startMillis != null && segment.endMillis != null) {
            val segmentStart = epochMillisToLocalDateTime(segment.startMillis)
            val segmentEnd = epochMillisToLocalDateTime(segment.endMillis)
            return "${segmentStart.format(fullDateTimeFormatter)}--${segmentEnd.format(timeFormatter)}"
        }
        val start = record?.startTime ?: return "--"
        val segmentStart = start.plusNanos(indexToNanos(segment.startIndex))
        val endIndex = segment.startIndex + segment.points.size.coerceAtLeast(1)
        val segmentEnd = start.plusNanos(indexToNanos(endIndex))
        return "${segmentStart.format(fullDateTimeFormatter)}--${segmentEnd.format(timeFormatter)}"
    }

    private fun epochMillisToLocalDateTime(epochMillis: Long): LocalDateTime {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private fun indexToNanos(index: Int): Long {
        return index.toLong() * 1_000_000_000L / POINTS_PER_SECOND
    }

    private fun addPDFFooter(document: Document, writer: PdfWriter) {
        ensurePageSpace(document, writer, templateConfig.footerSectionHeight)
        etco2ReferenceText()?.let {
            val reference = Paragraph(it, reportSmallFont)
            reference.spacingBefore = templateConfig.footerReferenceSpacingBefore
            reference.spacingAfter = templateConfig.footerReferenceSpacingAfter
            document.add(reference)
        }

        val table = PdfPTable(templateConfig.footerSignatureColumnWidths.size)
        table.widthPercentage = 100f
        table.setWidths(templateConfig.footerSignatureColumnWidths.toFloatArray())
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

    private fun addReportSection(
        document: Document,
        writer: PdfWriter,
        estimatedHeight: Float,
        render: () -> Unit
    ) {
        ensurePageSpace(document, writer, estimatedHeight)
        render()
    }

    private fun ensurePageSpace(document: Document, writer: PdfWriter, estimatedHeight: Float) {
        val remainingHeight = writer.getVerticalPosition(true) - document.bottomMargin()
        if (remainingHeight < estimatedHeight) {
            document.newPage()
        }
    }

    private fun savePDF(filePath: String): Boolean {
        val outputFile = File(filePath)
        val tempFile = File("${filePath}.tmp")
        var document: Document? = null
        return try {
            outputFile.parentFile?.mkdirs()
            if (tempFile.exists()) {
                tempFile.delete()
            }
            document = Document(
                templateConfig.pageSize,
                templateConfig.pageMarginLeft,
                templateConfig.pageMarginRight,
                templateConfig.pageMarginTop,
                templateConfig.pageMarginBottom
            )
            val writer = PdfWriter.getInstance(document, FileOutputStream(tempFile))
            resolveWatermarkConfig()?.let {
                writer.pageEvent = WatermarkPageEvent(it)
            }

            document.open()

            // 添加文件标题: 医院名称和报告名称
            addPDFHeader(document)

            // 添加记录基础信息：住院号、床位号、姓名、性别、年龄等表单字段
            addReportSection(document, writer, templateConfig.detailSectionHeight) {
                addPDFDetail(document)
            }

            // 添加全程摘要，避免只依赖各段瞬时末值。
            addReportSection(document, writer, templateConfig.summarySectionHeight) {
                addReportSummary(document)
            }

            // 按纸质报告单样式从记录开头绘制最多三段连续 14 秒 CO2 波形。
            addWaveformSections(document, writer)

            // PDF页脚
            addPDFFooter(document, writer)

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
    deviceSerial: String? = null,
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
        deviceSerial = deviceSerial,
        onComplete = onComplete,
    ).execute()
}
