package com.wldmedical.capnoeasy.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.patientParams

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    override val pageScene = PageScene.HISTORY_DETAIL_PAGE

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Composable
    override fun Content() {
        val patient = intent.getSerializableExtra(patientParams) as Record
        println("wswTest 接收到的病人是 ${patient.patient.name}")
    }
}

//import android.graphics.Bitmap
//import android.graphics.pdf.PdfRenderer
//import android.os.Bundle
//import android.os.ParcelFileDescriptor
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_pdf_renderer.*
//
//class PdfRendererActivity : AppCompatActivity() {
//    private lateinit var pdfRenderer: PdfRenderer
//    private lateinit var currentPage: PdfRenderer.Page
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pdf_renderer)
//
//        // 假设 PDF 文件存放在应用的内部存储
//        val filePath = "${filesDir}/sample.pdf"
//        openRenderer(filePath)
//        showPage(0) // 显示第一页
//    }
//
//    private fun openRenderer(filePath: String) {
//        // 打开 PDF 文件
//        val parcelFileDescriptor: ParcelFileDescriptor = ParcelFileDescriptor.open(
//            java.io.File(filePath), ParcelFileDescriptor.MODE_READ_ONLY)
//
//        pdfRenderer = PdfRenderer(parcelFileDescriptor)
//    }
//
//    private fun showPage(index: Int) {
//        // 关闭当前页面（如果已打开）
//        if (::currentPage.isInitialized) {
//            currentPage.close()
//        }
//
//        // 打开指定页面
//        currentPage = pdfRenderer.openPage(index)
//
//        // 创建 Bitmap 来渲染页面
//        val bitmap = Bitmap.createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)
//        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//
//        // 设置 Bitmap 到 ImageView
//        imageView.setImageBitmap(bitmap)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        currentPage.close() // 关闭当前页面
//        pdfRenderer.close()  // 关闭 PdfRenderer
//    }
//}

//private var currentPageIndex = 0
//
//private fun showPage(index: Int) {
//    if (index < 0 || index >= pdfRenderer.pageCount) return // 检查有效索引
//
//    if (::currentPage.isInitialized) {
//        currentPage.close()
//    }
//
//    currentPage = pdfRenderer.openPage(index)
//    val bitmap = Bitmap.createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)
//    currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//    imageView.setImageBitmap(bitmap)
//    currentPageIndex = index
//}
//
//// 翻页按钮点击事件
//fun onNextPage() {
//    showPage(currentPageIndex + 1)
//}
//
//fun onPreviousPage() {
//    showPage(currentPageIndex - 1)
//}

