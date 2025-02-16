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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.kits.Record
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
        viewModel.updateShowActionModal(true)
    }

    private var saveFileName: String = ""

    private var currentRecord: Record? = null

    val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                savePdfToUri(sourceFilePath, uri)
            }
        }
    }

    fun createPdfDocument() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "${saveFileName}.pdf") // 设置默认文件名
        }
        createDocumentLauncher.launch(intent)
    }

    fun savePdfToUri(sourceFilePath: String, uri: Uri) {
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

    override fun onSavePDFClick() {
        if (this.sourceFilePath.isNotEmpty()) {
            createPdfDocument()
        }
    }

    override fun onPrintTicketClick() {
        if (currentRecord?.data == null) { return }
        if (currentRecord?.data!!.size <= 0) { return }
        val allPoints: List<Float> = currentRecord!!.data!!.map {
            it.co2
        }
        println("wswTest 一共有多少点 point ${allPoints.size}")
        blueToothKit.gpPrinterManager.print(this, allPoints)
    }

    @Composable
    override fun Content() {
        val pdfFilePath: MutableState<String> = remember { mutableStateOf<String>("") }
        val recordId = intent.getStringExtra(recordIdParams)
        val context = this;

        LaunchedEffect(recordId) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
                    if (record != null) {
                        if (record.pdfFilePath != null) {
                            if (record.pdfFilePath!!.isNotEmpty()) {
                                pdfFilePath.value = record.pdfFilePath!!
                                context.sourceFilePath = pdfFilePath.value
                                context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
                                context.currentRecord = record
                            }
                        }
                    }
                }
            }
        }

        if (pdfFilePath.value != "") {
            AndroidView(
                factory = { context ->
                    val pdfFile = File(pdfFilePath.value)
                    PDFView(context, null).apply {
                        fromFile(pdfFile)
                            .load()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Text("正在加载中")
        }
    }
}