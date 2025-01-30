package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 历史记录列表页
 */
class HistoryRecordsActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.HISTORY_LIST_PAGE)
    }
}