package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType

/***
 * 设置二级页 - 报警
 */
class AlertSettingActivity : BaseActivity() {
    @Composable
    override fun Content() {
        RangeSelector(
            title = "ETCO2 范围",
            type = RangeType.BOTH,
            startValue = 2.5f,
            endValue = 10f,
            valueRange = 0.3f..30f,
        )

        RangeSelector(
            title = "ETCO2 范围",
            type = RangeType.BOTH,
            startValue = 2.5f,
            endValue = 10f,
            valueRange = 0.3f..30f,
        )
    }
}