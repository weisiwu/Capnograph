package com.wldmedical.capnoeasy.pages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.kits.filterData
import com.wldmedical.capnoeasy.recordIdParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.UUID

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    override var pageScene = PageScene.HISTORY_DETAIL_PAGE

    override fun onNavBarRightClick() {
        if (viewModel.isPDF.value) {
            this.onSavePDFClick()
        } else {
            this.onPrintTicketClick()
        }
    }

    private var saveFileName: String = ""

    private var currentRecord: Record? = null

    private val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                savePdfToUri(sourceFilePath, uri)
            }
        }
    }

    private fun createPdfDocument() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "${saveFileName}.pdf") // 设置默认文件名
        }
        createDocumentLauncher.launch(intent)
    }

    private fun savePdfToUri(sourceFilePath: String, uri: Uri) {
        var outputStream: OutputStream? = null
        var inputStream: FileInputStream? = null
        try {
            outputStream = contentResolver.openOutputStream(uri)
            inputStream = FileInputStream(File(sourceFilePath))
            inputStream.copyTo(outputStream!!)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
            inputStream?.close()
        }
    }

    // 保存PDF文件
    override fun onSavePDFClick() {
        if (blueToothKit.connectedCapnoEasy.value == null) {
            viewModel.updateToastData(
                ToastData(
                    text = getStringAcitivity(R.string.recorddetail_record_fail),
                    showMask = false,
                    duration = 1000,
                )
            )
            return
        }

        if (this.sourceFilePath.isNotEmpty()) {
            createPdfDocument()
        }
    }

    override fun onPrintTicketClick() {
        val currentData = currentRecord?.data ?: return
        if (currentData.isEmpty()) { return }

        if (!blueToothKit.gpPrinterManager.getConnectState()) {
            viewModel.updateToastData(
                ToastData(
                    text = getStringAcitivity(R.string.recorddetail_print_fail),
                    showMask = false,
                    duration = 1000,
                )
            )
            return
        }

        // 对波形数据进行过滤
        val filteredData = filterData(currentData, viewModel.CO2Scale.value.value)
        val allPoints: List<Float> = filteredData.map {
            it.co2
        }

        blueToothKit.gpPrinterManager.print(
            this,
            allPoints,
            localStorageKit.loadPrintSettingFromPreferences(this),
            viewModel.CO2Scale.value.value
        )
    }

    @Composable
    override fun Content() {
        val pdfFilePath: MutableState<String> = remember { mutableStateOf<String>("") }
        val recordId = intent.getStringExtra(recordIdParams)
        val context = this;
        var currentPage by remember { mutableStateOf(0) }

        LaunchedEffect(recordId) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
                    if (record != null) {
//                        if (record.previewPdfFilePath != null) {
//                            if (record.previewPdfFilePath!!.isNotEmpty()) {
//                                pdfFilePath.value = record.previewPdfFilePath!!
//                                context.sourceFilePath = record.pdfFilePath!!
//                                context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
//                                context.currentRecord = record
//                            }
//                        }
                    }
                }
            }
        }



//        if (pdfFilePath.value != "") {
        // 放弃通过PDF预览历史波形数据，但是保留代码
        if (false) {
            AndroidView(
                factory = { context ->
                    val pdfFile = File(pdfFilePath.value)
                    PDFView(context, null).apply {
                        fromFile(pdfFile)
                            .defaultPage(currentPage) // 设置默认显示的页面
                            .onPageChange { page, pageCount ->
                                currentPage = page
                            }
                            .enableAnnotationRendering(true) // 启用注释渲染（如果有）
                            .swipeHorizontal(true) // 启用水平滑动
                            .pageSnap(true) // 每次只显示一页
                            .autoSpacing(true) // 自动调整页面间距
                            .pageFling(true) // 启用页面快速滑动
                            .load()

                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
//        } else {
//            Text(getStringAcitivity(R.string.recorddetail_loading))
        }
    }
}