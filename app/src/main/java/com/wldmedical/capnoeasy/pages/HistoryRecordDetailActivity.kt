package com.wldmedical.capnoeasy.pages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
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
        val recordId = intent.getStringExtra(recordIdParams)

        LaunchedEffect(recordId) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
                    if (record != null) {

                    }
                }
            }
        }

        // 渲染波形图
    }
}