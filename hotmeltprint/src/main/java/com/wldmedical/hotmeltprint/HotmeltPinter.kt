package com.wldmedical.hotmeltprint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.Utils
import com.gprinter.bean.PrinterDevices
import com.gprinter.command.EscCommand
import com.gprinter.io.BluetoothPort
import com.gprinter.io.PortManager
import com.gprinter.utils.Command
import com.gprinter.utils.ConnMethod
import kotlinx.coroutines.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread
import kotlin.math.min
import kotlin.math.roundToInt

public object PrintSetting {
    var hospitalName: String? = null
    var reportName: String? = null
    var isPDF: Boolean = true
    var pdfDepart: String? = null
    var pdfBedNumber: String? = null
    var pdfIDNumber: String? = null
    var name: String? = null
    var gender: String? = null
    var age: Int? = null
}

object Printer {
    private var portManager: PortManager? = null

    // 获取打印机管理类
    fun getPortManager(): PortManager? {
        return portManager
    }

    // 获取连接状态
    fun getConnectState(): Boolean {
        return portManager?.getConnectStatus() ?: false
    }

    // 链接设备
    fun connect(devices: PrinterDevices?): Unit {
        thread {
            portManager?.closePort() // 关闭上次链接
            Thread.sleep(2000)

            // 设备非空，判断是否蓝牙连接，如是，进行连接
            devices?.let {
                portManager = BluetoothPort(it)
                portManager!!.openPort()
            }
        }
    }

    /**
     * 获取打印机状态
     * @param printerCommand 打印机命令 ESC为小票，TSC为标签 ，CPCL为面单
     * @return 返回值常见文档说明
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getPrinterState(printerCommand: Command, delayMillis: Long): Int {
        return portManager!!.getPrinterStatus(printerCommand)
    }

    /**
     * 获取打印机电量
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getPower(): Int {
        return portManager!!.getPower()
    }

    /**
     * 获取打印机指令
     * @return
     */
    fun getPrinterCommand(): Command {
        return portManager!!.getCommand()
    }

    /**
     * 设置使用指令
     * @param printerCommand
     */
    fun setPrinterCommand(printerCommand: Command): Unit {
        portManager?.setCommand(printerCommand)
    }

    /**
     * 发送数据到打印机 指令集合内容
     * @param vector
     * @return true发送成功 false 发送失败
     * 打印机连接异常或断开发送时会抛异常，可以捕获异常进行处理
     */
    @Throws(IOException::class)
    fun sendDataToPrinter(vector: ByteArray): Boolean {
        return portManager?.writeDataImmediately(vector) ?: false
    }

    /**
     * 关闭连接
     * @return
     */
    fun close(): Unit {
        portManager?.closePort()
        portManager = null
    }
}

@SuppressLint("NewApi")
fun getCurrentFormattedDateTime(): String {
    val currentDateTime = LocalDateTime.now()  // 获取当前日期和时间
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")  // 定义格式
    return currentDateTime.format(formatter)  // 格式化为字符串
}

class HotmeltPinter {
    var printer = Printer // 获取管理对象
    var macAddress: String = ""
    private var esc: EscCommand = EscCommand()

    /***
     * 压缩波形数据中为0的数据到原来数量的5%
     */
    private fun compressZeroSegments(allPoints: MutableList<Float>, compressRatio: Float = 0.05f): MutableList<Float> {
        // 定义段的数据类
        data class Segment(val isWaveform: Boolean, var points: MutableList<Float>)

        val segments = mutableListOf<Segment>()
        var currentSegment: Segment? = null

        // 分割原始数据为连续的波形段和零段
        for (point in allPoints) {
            if (point == 0f) {
                if (currentSegment?.isWaveform != false) { // 当前是波形段或null，创建新的零段
                    currentSegment?.let { segments.add(it) }
                    currentSegment = Segment(false, mutableListOf(point))
                } else {
                    currentSegment.points.add(point)
                }
            } else {
                if (currentSegment?.isWaveform != true) { // 当前是零段或null，创建新的波形段
                    currentSegment?.let { segments.add(it) }
                    currentSegment = Segment(true, mutableListOf(point))
                } else {
                    currentSegment.points.add(point)
                }
            }
        }
        currentSegment?.let { segments.add(it) }

        // 处理每个零段
        segments.forEachIndexed { index, segment ->
            if (!segment.isWaveform) {
                val originalSize = segment.points.size
                var newSize = (originalSize * compressRatio).roundToInt()

                // 如果是位于两个波形段之间的零段，至少保留1个点
                val isMiddleZero = index > 0 && index < segments.size - 1 &&
                        segments[index - 1].isWaveform && segments[index + 1].isWaveform
                if (isMiddleZero) {
                    newSize = newSize.coerceAtLeast(1)
                }
                segment.points = segment.points.take(newSize).toMutableList()
            }
        }

        // 合并所有处理后的段
        val newList = mutableListOf<Float>()
        segments.forEach { segment ->
            newList.addAll(segment.points)
        }
        return newList
    }


    /***
     * 格式化数据
     * 1、将所有数值为0的数据全部清除
     */
    private fun washData(allPoints: MutableList<Float>): MutableList<Float> {
        // 目前只有这个策略
        return compressZeroSegments(allPoints)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startProcessingData(
        wavePoints: MutableList<Float>,
        context: Context,
        axisMaximum: Float,
    ) {
        GlobalScope.launch {
            val allPoints = washData(wavePoints)
            // 这里注意，数量足够才开始插值，并且绘图。否则继续等待直到数据量足够。
            var bitmap = withContext(Dispatchers.Main) {
                // 在主线程中调用
                generateWaveformBitmapNew(
                    context = context,
                    currentPageData = allPoints,
                    axisMaximum = axisMaximum
                )
            }
            // 顺时针转90度
            val matrix = Matrix()
            matrix.postRotate(90f)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            esc.drawImage(bitmap)

            printer.getPortManager()?.writeDataImmediately(esc.getCommand())
            esc.addText("\n")
            esc.addText("\n")
            esc.addText("\n")
            esc.addText("\n")
            printer.getPortManager()?.writeDataImmediately(esc.getCommand())
        }
    }

    /***
     * 获取原始图片尺寸（不加载到内存）
     */
    private fun getBitmapDimensions(context: Context, uri: Uri): Pair<Int, Int>? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true // 只解析边界信息
        }
        return context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
            Pair(options.outWidth, options.outHeight)
        }
    }

    /**
     * 计算合适的缩放比例
     */
    private fun calculateScaleFactor(
        originalWidth: Int,
        originalHeight: Int,
        maxWidth: Int = 384,
        maxHeight: Int = 384
    ): Float {
        val widthRatio = maxWidth.toFloat() / originalWidth
        val heightRatio = maxHeight.toFloat() / originalHeight
        // 取最小比例，确保图片不超出目标尺寸
        return min(widthRatio, heightRatio)
    }

    /**
     * 将用户上传的图片按照规格加载为bitmap
     */
    private fun loadScaledBitmap(context: Context, uri: Uri, maxWidth: Int, maxHeight: Int): Bitmap? {
        val (originalWidth, originalHeight) = getBitmapDimensions(context, uri) ?: return null

        // 计算实际缩放比例
        val scale = calculateScaleFactor(originalWidth, originalHeight, maxWidth, maxHeight)
        val targetWidth = (originalWidth * scale).toInt()
        val targetHeight = (originalHeight * scale).toInt()

        // 配置 Bitmap 加载选项
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565 // 减少内存占用
        }

        return context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)?.let { bitmap ->
                // 精确缩放至目标尺寸（避免 inSampleSize 的取整误差）
                Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
            }
        }
    }

    /**
     * 打印数据
     * @param context App上下文
     * @param allPoints 波形数据Y轴值列表
     * @param config 用户打印配置
     */
    fun print(
        context: Context,
        allPoints: List<Float>,
        config: PrintSetting = PrintSetting,
        axisMaximum: Float
    ) {
        esc.addInitializePrinter()
        esc.addSetLineSpacing(100)
        esc.addSelectPrintModes(
            EscCommand.FONT.FONTB,
            EscCommand.ENABLE.OFF,
            EscCommand.ENABLE.ON,
            EscCommand.ENABLE.ON,
            EscCommand.ENABLE.OFF
        )
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
        config.hospitalName?.let {
            esc.addText("${it}\n")
        }
        config.reportName?.let {
            esc.addText("${it}\n")
        }
        esc.addSelectPrintModes(
            EscCommand.FONT.FONTA,
            EscCommand.ENABLE.OFF,
            EscCommand.ENABLE.OFF,
            EscCommand.ENABLE.OFF,
            EscCommand.ENABLE.OFF
        )
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        esc.addText("********************************\n")
        config.name?.let { esc.addText("姓名: ${config.name}\n") }
        config.gender?.let { esc.addText("性别: ${config.gender}\n") }
        config.age?.let { if (config.age != 0) { esc.addText("年龄: ${config.age}\n") } }
        config.pdfIDNumber?.let { esc.addText("ID: ${config.pdfIDNumber}\n") }
        config.pdfDepart?.let { esc.addText("科室: ${config.pdfDepart}\n") }
        config.pdfBedNumber?.let { esc.addText("床号: ${config.pdfBedNumber}\n") }
        esc.addText("时间: ${getCurrentFormattedDateTime()}\n")
        esc.addText("设备: CapnoEasy\n")
        esc.addText("********************************\n")
        esc.addText("\n")
        esc.addCutPaper()
        printer?.getPortManager()?.writeDataImmediately(esc.getCommand())

        // 读取记录中的数据，并打印出来
        startProcessingData(
            allPoints.toMutableList(),
            context,
            axisMaximum
        )
    }

    /**
     * 链接打印机
     */
    fun connect(mac: String) {
        macAddress = mac
        val blueTooth = PrinterDevices.Build()
            .setConnMethod(ConnMethod.BLUETOOTH)
            .setMacAddress(macAddress)
            .setCommand(Command.ESC)
            .build()

        printer.connect(blueTooth)
    }

    // 新版本线图，保持和PDF一致
    fun generateWaveformBitmapNew(
        context: Context,
        currentPageData: List<Float>,
        axisMaximum: Float,
    ): Bitmap {
        val width = (currentPageData.size / 500) * 1000 // 设置宽度
        val height = 450 // 设置高度

        // 生成当前页的 Bitmap
        val currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(currentPageBitmap)
        canvas.drawColor(Color.WHITE)

        // 绘制当前页的图表
        val copyLineChart = LineChart(context)
        copyLineChart.setBackgroundColor(Color.TRANSPARENT)
        copyLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        copyLineChart.xAxis.textColor = Color.BLACK
        copyLineChart.xAxis.textSize = Utils.convertDpToPixel(8f)
        copyLineChart.axisRight.isEnabled = false
        copyLineChart.description.isEnabled = false
        copyLineChart.axisLeft.axisMinimum = 0f
        copyLineChart.axisLeft.axisMaximum = axisMaximum
        copyLineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String { return "" }
        }

        // 绘制当前页的数据
        val segment = currentPageData.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }
        val dataSet = LineDataSet(segment, "ETCO2")
        dataSet.lineWidth = 1f
        dataSet.color = Color.BLACK
        dataSet.setDrawCircles(false) // 不绘制圆点
        val lineData = LineData(dataSet)
        copyLineChart.data = lineData
        copyLineChart.invalidate()
        copyLineChart.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        copyLineChart.layout(0, 0, width, height)
        copyLineChart.draw(canvas)

        // 输出bitmap到热熔打印机
        return currentPageBitmap
    }
}