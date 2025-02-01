package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType

/***
 * 设置二级页 - 模块
 */
class ModuleSettingActivity : BaseActivity() {
    override val pageScene = PageScene.MODULE_CONFIG_PAGE

    @Composable
    override fun Content() {
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