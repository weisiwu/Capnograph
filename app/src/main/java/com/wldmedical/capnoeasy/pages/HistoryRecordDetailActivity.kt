package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.recordIdParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    override val pageScene = PageScene.HISTORY_DETAIL_PAGE

    @Composable
    override fun Content() {
        val pdfFilePath: MutableState<String> = remember { mutableStateOf<String>("") }
        val recordId = intent.getStringExtra(recordIdParams)
        println("wswTest recordId $recordId")

        LaunchedEffect(recordId) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
                    if (record != null) {
                        if (record.pdfFilePath != null) {
                            if (record.pdfFilePath!!.isNotEmpty()) {
                                pdfFilePath.value = record.pdfFilePath!!
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
            // TODO: 历史
            Text("正在加载中")
        }
    }
}