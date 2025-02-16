package com.wldmedical.hotmeltprint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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

    /**
     * 打印数据
     */
    fun print(context: Context, allPoints: List<Float>) {
        esc.addInitializePrinter()
        esc.addSetLineSpacing(100)
        val bitImg = BitmapFactory.decodeResource(context.resources, R.drawable.logo) //二维码图片，图片类型bitmap
        // 打印图片,58打印机图片宽度最大为384dot  1mm=8dot 用尺子量取图片的宽度单位为Xmm  传入宽度值为 X*8
        esc.drawImage(bitImg, 384)
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        esc.addText("电话: 13501129344\n")
        esc.addText("地址: 北京市北京经济技术开发区宏达南路3号院2号楼3层301室\n")
        esc.addText("网址: https://www.wldyq.com\n")
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER)
        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode(0x31.toByte())
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode(4.toByte())
        // 设置qrcode内容
        esc.addStoreQRCodeData("https://www.wldyq.com")
        // 打印QRCode
        esc.addPrintQRCode()
        esc.addText("\n")
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT)
        esc.addText("********************************\n")
        esc.addText("时间: ${getCurrentFormattedDateTime()}\n")
        esc.addText("设备: CapnoEasy\n")
        esc.addText("序号: $macAddress\n")
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