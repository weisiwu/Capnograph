package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.wldmedical.capnoeasy.ETCO2Range
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.RRRange
import com.wldmedical.capnoeasy.RR_UNIT
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType
import com.wldmedical.capnoeasy.components.ToastData
import kotlin.math.floor

/***
 * 设置二级页 - 报警
 */
class AlertSettingActivity : BaseActivity() {
    override var pageScene = PageScene.ALERT_CONFIG_PAGE

    var minETCO2: Float = 0f
    var maxETCO2: Float = 0f
    var minRR: Float = 0f
    var maxRR: Float = 0f

    @SuppressLint("NewApi")
    @Composable
    override fun Content() {
        minETCO2 = viewModel.alertETCO2Range.value.start
        maxETCO2 = viewModel.alertETCO2Range.value.endInclusive
        minRR = viewModel.alertRRRange.value.start
        maxRR = viewModel.alertRRRange.value.endInclusive

        Column {
            RangeSelector(
                title = "ETCO2 范围",
                unit = viewModel.CO2Unit.value.rawValue,
                type = RangeType.BOTH,
                startValue = minETCO2,
                endValue = maxETCO2,
                valueRange = ETCO2Range,
                onValueChange = { start, end ->
                    minETCO2 = start
                    if (end != null) {
                        maxETCO2 = end
                    }
                }
            )

            RangeSelector(
                title = "RR 范围",
                unit = RR_UNIT,
                type = RangeType.BOTH,
                startValue = minRR,
                endValue = maxRR,
                valueRange = RRRange,
                onValueChange = { start, end ->
                    minRR = start
                    if (end != null) {
                        maxRR = end
                    }
                }
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
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable {
                        checkHasConnectDevice {
                            viewModel.updateAlertRRRange(minRR, maxRR)
                            viewModel.updateAlertETCO2Range(minETCO2, maxETCO2)
                            viewModel.updateLoadingData(
                                LoadingData(
                                    text = "正在设置",
                                    duration = InfinityDuration,
                                )
                            )
                            blueToothKit.updateAlertRange(
                                co2Low = minETCO2,
                                co2Up = maxETCO2,
                                rrLow = floor(minRR).toInt(),
                                rrUp = floor(maxRR).toInt(),
                                callback = {
                                    viewModel.clearXData()
                                    viewModel.updateToastData(
                                        ToastData(
                                            text = "设置成功",
                                            showMask = false,
                                            duration = 800,
                                        )
                                    )
                                }
                            )
                        }
                    }
                ) {
                    Text(
                        text = "保存",
                        letterSpacing = 5.sp,
                        color = Color(0xff165DFF),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xffE0EAFF))
                            .padding(horizontal = 30.dp, vertical = 12.dp)
                            .wrapContentWidth()
                            .wrapContentHeight()
                    )
                }
            }
            
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }
    }
}