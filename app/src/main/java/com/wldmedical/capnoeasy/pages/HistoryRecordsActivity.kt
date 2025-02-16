package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.HistoryList
import com.wldmedical.capnoeasy.kits.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/***
 * 历史记录列表页
 */
class HistoryRecordsActivity : BaseActivity() {
    override var pageScene = PageScene.HISTORY_LIST_PAGE

    @Composable
    override fun Content() {
        val records = remember { mutableStateListOf<Record>() }

        LaunchedEffect(0) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val newRecords = localStorageKit.database.recordDao().getAllRecords()
                    withContext(Dispatchers.Main) {
                        records.addAll(newRecords)
                    }
                }
            }
        }

        HistoryList(
            records = records,
            state = localStorageKit.state,
            context = this
        )
    }
}