package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType
import com.wldmedical.capnoeasy.models.AppStateModel

/***
 * 设置二级页 - 模块
 */
class ModuleSettingActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.MODULE_CONFIG_PAGE)
        RangeSelector(
            title = "大气压(mmHg)",
            value = 12.3f,
            type = RangeType.ONESIDE,
            valueRange = 0.3f..30f,
        )

        RangeSelector(
            title = "大气压(mmHg)",
            value = 12.3f,
            type = RangeType.ONESIDE,
            valueRange = 0.3f..30f,
        )
    }
}