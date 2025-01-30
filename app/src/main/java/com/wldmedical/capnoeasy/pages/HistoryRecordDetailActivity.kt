package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.HISTORY_DETAIL_PAGE)
    }
}