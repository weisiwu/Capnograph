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
    val detailSectionHeight: Float = 190f,
    val waveformSectionHeight: Float = 150f,
    val footerSectionHeight: Float = 92f,
    val detailColumnWidths: List<Float> = listOf(1f, 1f),
    val detailFieldColumnWidths: List<Float> = listOf(1f, 1.8f),
    val detailSpacingBefore: Float = 12f,
    val detailSpacingAfter: Float = 22f,
    val fieldPaddingBottom: Float = 14f,
    val fieldLabelPaddingRight: Float = 2f,
    val fieldValuePaddingBottom: Float = 1.5f,
    val fieldValueBorderWidth: Float = 0.5f,
    val horizontalRuleHeight: Float = 8f,
    val horizontalRuleBorderWidth: Float = 1f,
    val waveformSegmentDurationSeconds: Int = 15,
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
    val waveformXAxisLabelStepSeconds: Int = 5,
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
)

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
        val endMillis: Long? = null,
        val durationMillis: Long
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
    private val usesSampleTimeline = data.isNotEmpty() && data.all { it.sampleTimeMillis > 0L }

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

        val fields = listOf(
            "住院号" to printSetting?.pdfIDNumber,
            "床位号" to printSetting?.pdfBedNumber,
            "姓名" to patientName(),
            "性别" to patientGender(),
            "科室" to printSetting?.pdfDepart
        )
        fields.forEach { (label, value) ->
            addFieldCell(table, label, value)
        }

        val emptyCellCount = (templateConfig.detailColumnWidths.size - fields.size % templateConfig.detailColumnWidths.size)
            .takeIf { it < templateConfig.detailColumnWidths.size }
            ?: 0
        repeat(emptyCellCount) {
            addEmptyCell(table)
        }

        document.add(table)
    }

    private fun addFieldCell(
        table: PdfPTable,
        label: String,
        value: String?
    ) {
        val fieldTable = PdfPTable(templateConfig.detailFieldColumnWidths.size)
        fieldTable.widthPercentage = 100f
        fieldTable.setWidths(templateConfig.detailFieldColumnWidths.toFloatArray())

        val labelCell = PdfPCell(Phrase("$label：", reportFormFont))
        labelCell.border = Rectangle.NO_BORDER
        labelCell.horizontalAlignment = Element.ALIGN_RIGHT
        labelCell.paddingRight = templateConfig.fieldLabelPaddingRight
        labelCell.paddingBottom = templateConfig.fieldValuePaddingBottom
        fieldTable.addCell(labelCell)

        val valueCell = PdfPCell(Phrase(fieldDisplayValue(value), reportFormFont))
        valueCell.border = Rectangle.BOTTOM
        valueCell.setBorderWidthBottom(templateConfig.fieldValueBorderWidth)
        valueCell.horizontalAlignment = Element.ALIGN_LEFT
        valueCell.paddingBottom = templateConfig.fieldValuePaddingBottom
        fieldTable.addCell(valueCell)

        val cell = PdfPCell(fieldTable)
        cell.border = Rectangle.NO_BORDER
        cell.setPadding(0f)
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

    private fun fieldDisplayValue(value: String?): String = value?.takeIf { it.isNotBlank() } ?: ""

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
        if (data.isEmpty()) return

        val segments = buildWaveformSegments()
        segments.forEachIndexed { index, segment ->
            addReportSection(document, writer, templateConfig.waveformSectionHeight) {
                addWaveformHeader(document, segment, index)
                addWaveformChart(document, segment)
                addWaveformMetrics(document, segment)
            }
        }
    }

    private fun buildWaveformSegments(): List<ReportSegment> {
        if (data.isEmpty()) return emptyList()
        val segmentDurationMillis = templateConfig.waveformSegmentDurationSeconds
            .coerceAtLeast(1)
            .toLong() * 1000L
        val segments = mutableListOf<ReportSegment>()
        var segmentStartMillis = pointTimelineMillis(data.first())
        var segmentEndMillis = segmentStartMillis + segmentDurationMillis
        var segmentPoints = mutableListOf<CO2WavePointData>()

        fun addCurrentSegment() {
            if (segmentPoints.isEmpty()) return
            val segmentStartIndex = if (hasSampleTimeline()) {
                segmentPoints.first().index
            } else {
                timelineMillisToPointIndex(segmentStartMillis)
            }
            segments.add(
                ReportSegment(
                    points = segmentPoints.toList(),
                    startIndex = segmentStartIndex,
                    startMillis = displayTimelineMillis(segmentStartMillis),
                    endMillis = displayTimelineMillis(segmentEndMillis),
                    durationMillis = segmentDurationMillis
                )
            )
        }

        data.forEach { point ->
            val pointMillis = pointTimelineMillis(point)
            while (pointMillis >= segmentEndMillis) {
                addCurrentSegment()
                segmentPoints = mutableListOf()
                segmentStartMillis = segmentEndMillis
                segmentEndMillis += segmentDurationMillis
            }
            segmentPoints.add(point)
        }

        addCurrentSegment()
        return segments
    }

    private fun addWaveformHeader(document: Document, segment: ReportSegment, index: Int) {
        val table = PdfPTable(1)
        table.widthPercentage = 100f
        table.spacingBefore = if (index == 0) 0f else templateConfig.waveformHeaderSpacingBefore

        val phrase = Phrase()
        phrase.add(Chunk("测量时间：  ${formatMeasurementRange(segment)}", reportHeaderFont))

        val cell = PdfPCell(phrase)
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

    private fun displayTimelineMillis(timelineMillis: Long): Long? {
        if (hasSampleTimeline()) return timelineMillis
        val recordStart = record?.startTime ?: return null
        return localDateTimeToEpochMillis(recordStart) + timelineMillis
    }

    private fun hasSampleTimeline(): Boolean {
        return usesSampleTimeline
    }

    private fun pointTimelineMillis(point: CO2WavePointData): Long {
        return if (hasSampleTimeline()) {
            point.sampleTimeMillis
        } else {
            point.index.toLong() * 1000L / POINTS_PER_SECOND
        }
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
        val durationSeconds = segmentDurationSeconds(segment)

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
        val xAxisStep = waveformXAxisStepSeconds(durationSeconds)
        for (second in 0..durationSeconds step xAxisStep) {
            val x = plotLeft + plotWidth * second / durationSeconds
            val label = if (second == durationSeconds) "${second}(s)" else second.toString()
            canvas.drawText(label, x, height - 20f, textPaint)
        }

        if (points.size > 1) {
            val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                strokeWidth = templateConfig.waveformLineStrokeWidth
                style = Paint.Style.STROKE
            }
            val path = Path()
            points.forEachIndexed { index, point ->
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
        return if (hasSampleTimeline() && segmentStart != null && point.sampleTimeMillis > 0L) {
            val elapsedMillis = (point.sampleTimeMillis - segmentStart)
                .coerceIn(0L, segment.durationMillis)
            plotLeft + plotWidth * elapsedMillis / segment.durationMillis.toFloat()
        } else {
            val segmentPoints = durationMillisToPoints(segment.durationMillis).coerceAtLeast(1)
            val elapsedPoints = (point.index - segment.startIndex).coerceAtLeast(fallbackIndex)
                .coerceIn(0, segmentPoints)
            plotLeft + plotWidth * elapsedPoints / segmentPoints.toFloat()
        }
    }

    private fun segmentDurationSeconds(segment: ReportSegment): Int {
        return ((segment.durationMillis + 999L) / 1000L).toInt().coerceAtLeast(1)
    }

    private fun durationMillisToPoints(durationMillis: Long): Int {
        return ((durationMillis * POINTS_PER_SECOND) / 1000L).toInt().coerceAtLeast(1)
    }

    private fun timelineMillisToPointIndex(timelineMillis: Long): Int {
        return ((timelineMillis * POINTS_PER_SECOND) / 1000L).toInt().coerceAtLeast(0)
    }

    private fun waveformXAxisStepSeconds(durationSeconds: Int): Int {
        return when {
            durationSeconds <= templateConfig.waveformSegmentDurationSeconds -> templateConfig.waveformXAxisLabelStepSeconds
            durationSeconds <= 60 -> 10
            durationSeconds <= 120 -> 20
            durationSeconds <= 300 -> 60
            else -> max(1, durationSeconds / 5)
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

    private fun formatDurationAxisLabel(durationMillis: Long): String {
        val totalSeconds = (durationMillis / 1000L).coerceAtLeast(0L)
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return when {
            hours > 0L -> "${hours}h${minutes}m"
            minutes > 0L -> "${minutes}m${seconds}s"
            else -> "${seconds}s"
        }
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

    private fun localDateTimeToEpochMillis(value: LocalDateTime): Long {
        return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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

            // 添加记录基础信息：住院号、床位号、科室、姓名、性别等表单字段
            addReportSection(document, writer, templateConfig.detailSectionHeight) {
                addPDFDetail(document)
            }

            // 按完整记录时间轴连续展示波形，每 15 秒一段。
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
