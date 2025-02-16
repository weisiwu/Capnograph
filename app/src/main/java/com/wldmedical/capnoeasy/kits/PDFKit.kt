package com.wldmedical.capnoeasy.kits

/***
 * PDF相关能力
 * 1、展示PDF文件
 * 2、保存为PDF格式
 */

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfGState
import com.itextpdf.text.pdf.PdfWriter
import com.wldmedical.capnoeasy.models.CO2WavePointData
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch

class SaveChartToPdfTask(
    private val originalLineChart: LineChart,
    private val data: List<CO2WavePointData>,
    private val segmentSize: Int,
    private val filePath: String,
    private val onComplete: (Boolean) -> Unit // 添加回调
) : AsyncTask<Void, Void, Boolean>() {

    private lateinit var bitmap: Bitmap
    private val latch = CountDownLatch(1) // 添加 CountDownLatch

    // 复制图表，并将整体数据切分为段落渲染为bitmap
    override fun onPreExecute() {
        Handler(Looper.getMainLooper()).post {
            try {
                val copyLineChart = LineChart(originalLineChart.context)
                val chartWidth  = Resources.getSystem().displayMetrics.widthPixels
                val width = 1000 // 设置宽度
                val height = 800 // 设置高度

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

                val processedData = if (data.size < segmentSize) {
                    data.toMutableList().apply {
                        val padding = segmentSize - data.size
                        addAll(List(padding) { CO2WavePointData(0f, 0, 0f, 0f, 0) })
                    }
                } else {
                    data
                }

                for (i in processedData.indices step segmentSize) {
                    val segment = processedData.subList(i, minOf(i + segmentSize, processedData.size)).map {
                        Entry(it.index.toFloat(), it.co2)
                    }
                    val dataSet = LineDataSet(segment, "ETCO2")
                    dataSet.lineWidth = 2f
                    dataSet.setDrawCircles(false) // 不绘制圆点
                    val lineData = LineData(dataSet)
                    copyLineChart.data = lineData
                    copyLineChart.invalidate()
                    copyLineChart.requestLayout()
                }

                val bitmap = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                copyLineChart.draw(canvas)
                this.bitmap = bitmap // 将 bitmap 赋值给成员变量
                latch.countDown()
            } catch (e: Exception) {
                println("wswTest 遇到了错误")
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
            convertBitmapToPdf(bitmap, filePath)
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
        canvas.restoreState() // 🟢 恢复状态（必须与 saveState 成对出现）
    }

    private fun convertBitmapToPdf(bitmap: Bitmap, filePath: String) {
        val document = Document()
        try {
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image = Image.getInstance(stream.toByteArray())

            val pageWidth = document.pageSize.width
            val imageHeight = document.pageSize.height
            image.scaleToFit(pageWidth, imageHeight)
            image.setAbsolutePosition(
                (pageWidth - image.scaledWidth) / 2,
                (imageHeight - image.scaledHeight) / 2
            )

            document.add(image)

            // 添加重复水印
            try {
                addRepeatedWatermark(writer, document.pageSize)
            } catch (e: Exception) {
                println("wswTest 一大批； 是， 刺配")
                e.printStackTrace()
            }


            document.close()
            println("wswTest 绘制结束")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun saveChartToPdfInBackground(
    lineChart: LineChart,
    data: List<CO2WavePointData>,
    segmentSize: Int,
    filePath: String
) {
    SaveChartToPdfTask(
        lineChart,
        data,
        segmentSize,
        filePath = filePath,
        onComplete = {}
    ).execute()
}
