package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.PrintDeviceConfig
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 设置二级页 - 打印
 */
class PrintSettingActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.PRINT_CONFIG_PAGE)
        PrintDeviceConfig()
    }
}