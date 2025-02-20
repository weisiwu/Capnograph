package com.wldmedical.hotmeltprint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
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

public object PrintSetting {
    var macAddress: String? = null
    var printAddress: String? = null
    var printPhone: String? = null
    var printUrl: String? = null
    var printLogo: Uri? = null
    var printUrlQRCode: Boolean? = true
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

     fun startProcessingData(allPoints: MutableList<Float>) {
         GlobalScope.launch {
             while (allPoints.size > 0) {
                 val dataToPrint = mutableListOf<Float>()
                 while (dataToPrint.size <= 500 && allPoints.size != 0) {
                     val lastValue = allPoints.removeAt(0)
                     dataToPrint.add(lastValue)
                 }
                 // 这里注意，数量足够才开始插值，并且绘图。否则继续等待直到数据量足够。
                 val bitmap = generateWaveformBitmap(dataToPrint)
                 esc.drawImage(bitmap)
                 printer?.getPortManager()?.writeDataImmediately(esc.getCommand())
             }
             esc.addText("\n")
             esc.addText("\n")
             esc.addText("\n")
             esc.addText("\n")
             printer?.getPortManager()?.writeDataImmediately(esc.getCommand())
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
//            inSampleSize = calculateInSampleSize(originalWidth, originalHeight, targetWidth, targetHeight)
            inPreferredConfig = Bitmap.Config.RGB_565 // 减少内存占用
        }

        return context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)?.let { bitmap ->
                // 精确缩放至目标尺寸（避免 inSampleSize 的取整误差）
                Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
            }
        }
    }

//    private fun calculateInSampleSize(
//        originalWidth: Int,
//        originalHeight: Int,
//        reqWidth: Int,
//        reqHeight: Int
//    ): Int {
//        var inSampleSize = 1
//        while (originalWidth / inSampleSize > reqWidth || originalHeight / inSampleSize > reqHeight) {
//            inSampleSize *= 2
//        }
//        return inSampleSize
//    }

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
    ) {
        esc.addInitializePrinter()
        esc.addSetLineSpacing(100)
        val maxSize = 384;
        config.printLogo?.let {
            val bitImg = loadScaledBitmap(context, config.printLogo!!, maxSize, maxSize)
            // 打印图片,58打印机图片宽度最大为384dot  1mm=8dot 用尺子量取图片的宽度单位为Xmm  传入宽度值为 X*8
            esc.drawImage(bitImg, maxSize)
        } ?: run {
            // 默认打印万联达公司logo
            val bitImg = BitmapFactory.decodeResource(context.resources, R.drawable.logo) //二维码图片，图片类型bitmap
            // 打印图片,58打印机图片宽度最大为384dot  1mm=8dot 用尺子量取图片的宽度单位为Xmm  传入宽度值为 X*8
            esc.drawImage(bitImg, maxSize)
        }
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        config.printPhone?.let {
            esc.addText("电话: ${it}\n")
        }
        config.printAddress?.let {
            esc.addText("地址: ${it}\n")
        }
        config.printUrl?.let {
            esc.addText("网址: ${it}\n")
        }
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode(0x31.toByte())
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode(4.toByte())
        // 设置qrcode内容
        config.printUrlQRCode?.let {
            config.printUrl?.let {
                esc.addStoreQRCodeData(it)
                // 打印QRCode
                esc.addPrintQRCode()
            }
        }
        esc.addText("\n")
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        esc.addText("********************************\n")
        esc.addText("时间: ${getCurrentFormattedDateTime()}\n")
        esc.addText("设备: CapnoEasy\n")
        config.macAddress?.let {
            esc.addText("序号: $it\n")
        }
        esc.addText("********************************\n")
        esc.addText("\n")
        esc.addCutPaper()
        printer?.getPortManager()?.writeDataImmediately(esc.getCommand())

        // 读取记录中的数据，并打印出来
        startProcessingData(allPoints.toMutableList())
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

     fun generateWaveformBitmap(data: List<Float>): Bitmap {
         val width = 400  // 打印机宽度 (58mm * 203dpi)
         val height = 400 // 设定波形图的高度
         val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
         val canvas = Canvas(bitmap)
         canvas.drawColor(Color.WHITE)

         // 设置画笔
         val paint = Paint().apply {
             color = Color.BLACK
             strokeWidth = 2f
             isAntiAlias = true
         }

         // 绘制波形
         val pointSpacing = width / data.size.toFloat()

         for (i in 0 until data.size - 1) {
             // 计算 X 和 Y 坐标
             val startX = (i * pointSpacing).toFloat()
             val startY = data[i] * 5  // 将数据映射到 Y 轴高度
             val stopX = ((i + 1) * pointSpacing).toFloat()
             val stopY = data[i + 1] * 5  // 同上
             // 如果存在上次绘制的结束位置，则将其连接到新绘制的部分
             canvas.drawLine(startY, startX, stopY, stopX, paint)
         }

         return bitmap
     }
}