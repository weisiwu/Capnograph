package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.SettingList
import com.wldmedical.capnoeasy.components.settings
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 设置一级页
 */
class SettingActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.SETTING_PAGE)
        SettingList(
            context = this,
            settings = settings
        )
    }
}