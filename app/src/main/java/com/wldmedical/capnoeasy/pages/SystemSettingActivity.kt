package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.SystemInoList
import com.wldmedical.capnoeasy.components.systeminfos
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 设置二级页 - 系统
 */
class SystemSettingActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.SYSTEM_CONFIG_PAGE)
        SystemInoList(systeminfos = systeminfos)
    }
}