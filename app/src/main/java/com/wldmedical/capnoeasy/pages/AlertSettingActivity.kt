package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wldmedical.capnoeasy.ETCO2Range
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
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
                title = getStringAcitivity(R.string.alertsetting_etco2_range),
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
                title = getStringAcitivity(R.string.alertsetting_rr_range),
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

            SaveButton {
                checkHasConnectDevice {
                    viewModel.updateAlertRRRange(minRR, maxRR)
                    viewModel.updateAlertETCO2Range(minETCO2, maxETCO2)
                    viewModel.updateLoadingData(
                        LoadingData(
                            text = getStringAcitivity(R.string.alertsetting_is_setting),
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
                                    text = getStringAcitivity(R.string.alertsetting_setting_success),
                                    showMask = false,
                                    duration = 800,
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}