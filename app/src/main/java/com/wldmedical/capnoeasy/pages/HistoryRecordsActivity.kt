package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.HistoryList

/***
 * 历史记录列表页
 */
class HistoryRecordsActivity : BaseActivity() {
    override val pageScene = PageScene.HISTORY_LIST_PAGE

    @Composable
    override fun Content() {
        HistoryList(
            records = localStorageKit.records   ,
            state = localStorageKit.state,
            context = this
        )
    }
}