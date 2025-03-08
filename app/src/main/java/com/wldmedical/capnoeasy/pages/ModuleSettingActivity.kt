package com.wldmedical.capnoeasy.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.O2_UNIT
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.TIME_UNIT
import com.wldmedical.capnoeasy.airPressureRange
import com.wldmedical.capnoeasy.asphyxiationTimeRange
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.o2CompensationRange

/***
 * 设置二级页 - 模块
 */
class ModuleSettingActivity : BaseActivity() {
    override var pageScene = PageScene.MODULE_CONFIG_PAGE

    var asphyxiationTime: Int = 0
    var o2Compensation: Float = 0f

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        asphyxiationTime = viewModel.asphyxiationTime.value
        o2Compensation = viewModel.o2Compensation.value

        Column {
            RangeSelector(
                title = "${getStringAcitivity(R.string.module_atmospheric_pressure)}(${viewModel.CO2Unit.value.rawValue})",
                unit = viewModel.CO2Unit.value.rawValue,
                enabled = false,
                value = blueToothKit.airPressure.value,
                type = RangeType.ONESIDE,
                valueRange = airPressureRange,
            )

            RangeSelector(
                title = "${getStringAcitivity(R.string.module_asphyxia_time)}(${TIME_UNIT})",
                value = asphyxiationTime.toFloat(),
                unit = TIME_UNIT,
                type = RangeType.ONESIDE,
                valueRange = asphyxiationTimeRange,
                onValueChange = { newVal, _ ->
                    asphyxiationTime = newVal.toInt()
                }
            )

            RangeSelector(
                title = "${getStringAcitivity(R.string.module_oxygen_compensation)}(${O2_UNIT})",
                unit = O2_UNIT,
                value = o2Compensation,
                type = RangeType.ONESIDE,
                valueRange = o2CompensationRange,
                onValueChange = { newVal, _ ->
                    o2Compensation = newVal
                }
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            SaveButton {
                checkHasConnectDevice {
                    viewModel.updateAsphyxiationTime(asphyxiationTime)
                    viewModel.updateO2Compensation(o2Compensation)
                    viewModel.updateLoadingData(
                        LoadingData(
                            text = getStringAcitivity(R.string.module_is_setting),
                            duration = InfinityDuration,
                        )
                    )
                    blueToothKit.updateNoBreathAndCompensation(
                        asphyxiationTime,
                        o2Compensation,
                        callback = {
                            viewModel.clearXData()
                            viewModel.updateToastData(
                                ToastData(
                                    text = getStringAcitivity(R.string.module_setting_success),
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