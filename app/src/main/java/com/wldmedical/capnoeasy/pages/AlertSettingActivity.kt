package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.ETCO2Range
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.RRRange
import com.wldmedical.capnoeasy.RR_UNIT
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType

/***
 * 设置二级页 - 报警
 */
class AlertSettingActivity : BaseActivity() {
    override val pageScene = PageScene.ALERT_CONFIG_PAGE

    @Composable
    override fun Content() {
        Column {
            RangeSelector(
                title = "ETCO2 范围",
                unit = CO2_UNIT.MMHG.rawValue,
                type = RangeType.BOTH,
                startValue = 0f,
                endValue = 10f,
                valueRange = ETCO2Range,
            )

            RangeSelector(
                title = "RR 范围",
                unit = RR_UNIT,
                type = RangeType.BOTH,
                startValue = 0f,
                endValue = 10f,
                valueRange = RRRange,
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card (
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "保存",
                        letterSpacing = 5.sp,
                        color = Color(0xff165DFF),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xffE0EAFF))
                            .padding(horizontal = 30.dp, vertical = 16.dp)
                            .wrapContentWidth()
                            .wrapContentHeight()
                    )
                }
            }
        }
    }
}