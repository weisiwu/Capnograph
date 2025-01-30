package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.co2ScalesObj
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.wfSpeedsObj

/***
 * 设置二级页 - 展示
 */
class DisplaySettingActivity : BaseActivity() {
    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.DISPLAY_CONFIG_PAGE)
        WheelPicker(
            config = co2UnitsObj
        )

        WheelPicker(
            config = co2ScalesObj
        )

        WheelPicker(
            config = wfSpeedsObj
        )
    }
}