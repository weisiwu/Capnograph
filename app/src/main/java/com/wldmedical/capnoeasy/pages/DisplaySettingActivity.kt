package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.co2Units
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.wfSpeeds
import com.wldmedical.capnoeasy.wfSpeedsObj
import com.wldmedical.capnoeasy.wheelPickerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/***
 * 设置二级页 - 展示
 */
class DisplaySettingActivity : BaseActivity() {
    override var pageScene = PageScene.DISPLAY_CONFIG_PAGE

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    override fun Content() {
        val co2Scales = remember { mutableListOf(CO2_SCALE.SMALL, CO2_SCALE.MIDDLE, CO2_SCALE.LARGE) }
        val defaultUnit = remember { mutableStateOf(viewModel.CO2Unit.value) }
        val defaultScale = remember { mutableStateOf(viewModel.CO2Scale.value) }
        val defaultWFSpeed = remember { mutableStateOf(viewModel.WFSpeed.value) }
        val context = this

        Column {
            WheelPicker(
                config = wheelPickerConfig(
                    items = co2Units,
                    title = getString(R.string.display_co2_unit),
                    defaultValue = defaultUnit.value
                ),
                onValueChange = {
                    if (it >= 0 && it < co2UnitsObj.items.size) {
                        defaultUnit.value = co2UnitsObj.items[it]
                        co2Scales.clear()
                        if (defaultUnit.value == CO2_UNIT.KPA) {
                            co2Scales.addAll(listOf(
                                CO2_SCALE.KPA_SMALL,
                                CO2_SCALE.KPA_MIDDLE,
                                CO2_SCALE.KPA_LARGE,
                            ))
                        } else if (defaultUnit.value == CO2_UNIT.MMHG) {
                            co2Scales.addAll(listOf(
                                CO2_SCALE.SMALL,
                                CO2_SCALE.MIDDLE,
                                CO2_SCALE.LARGE,
                            ))
                        } else if (defaultUnit.value == CO2_UNIT.PERCENT) {
                            co2Scales.addAll(listOf(
                                CO2_SCALE.PERCENT_SMALL,
                                CO2_SCALE.PERCENT_MIDDLE,
                                CO2_SCALE.PERCENT_LARGE,
                            ))
                        }
                        defaultScale.value = co2Scales[0]
//                        println("wswTest 页面的值是什么 ${co2Scales[0]}")
                    }
                }
            )

            WheelPicker(
                config = wheelPickerConfig(items = co2Scales, title = "CO2 Scale", defaultValue = defaultScale.value),
                unit = defaultUnit.value,
                onValueChange = {
//                    println("wswTest 接收到了组件内传递来的新值 $it")
                    if (it >= 0 && it < co2Scales.size) {
                        defaultScale.value = co2Scales[it]
                    }
                }
            )

            WheelPicker(
                config = wheelPickerConfig(items = wfSpeeds, title = "WF Speed", defaultValue = defaultWFSpeed.value),
                onValueChange = {
                    if (it >= 0 && it < wfSpeedsObj.items.size) {
                        defaultWFSpeed.value = wfSpeedsObj.items[it]
                    }
                }
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            SaveButton {
//                checkHasConnectDevice {
                    viewModel.updateCO2Unit(defaultUnit.value)
                    viewModel.updateCo2Scales(co2Scales)
                    viewModel.updateCO2Scale(defaultScale.value)
                    viewModel.updateWFSpeed(defaultWFSpeed.value)
                    viewModel.updateLoadingData(
                        LoadingData(
                            text = getString(R.string.display_is_setting),
                            duration = InfinityDuration,
                        )
                    )
                    println("wswTest 在这里传入的是什么 ${defaultScale.value} ${defaultUnit.value}")
                    blueToothKit.updateCO2UnitScale(
                        co2Scale = defaultScale.value,
                        co2Unit = defaultUnit.value,
                        callback = {
                            // TODO: 临时测试使用
                            Toast.makeText(context, "Scale:${defaultScale.value} Unit:${defaultUnit.value}", Toast.LENGTH_SHORT).show()
//                            viewModel.clearXData()
//                            viewModel.updateToastData(
//                                ToastData(
//                                    text = getStringAcitivity(R.string.display_setting_success),
//                                    showMask = false,
//                                    duration = 600,
//                                )
//                            )
                        }
                    )
//                }
            }
        }
    }
}