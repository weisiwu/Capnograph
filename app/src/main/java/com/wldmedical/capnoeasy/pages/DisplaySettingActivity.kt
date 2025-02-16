package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.WF_SPEED
import com.wldmedical.capnoeasy.co2Units
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.wfSpeeds
import com.wldmedical.capnoeasy.wfSpeedsObj
import com.wldmedical.capnoeasy.wheelPickerConfig

/***
 * 设置二级页 - 展示
 */
class DisplaySettingActivity : BaseActivity() {
    override var pageScene = PageScene.DISPLAY_CONFIG_PAGE

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    override fun Content() {
        // TODO: 修改单位后，sacale么有主动刷新
        val co2Scales = remember { mutableListOf(CO2_SCALE.SMALL, CO2_SCALE.MIDDLE, CO2_SCALE.LARGE) }
        val defaultUnit = remember { mutableStateOf(viewModel.CO2Unit.value) }
        val defaultScale = remember { mutableStateOf(viewModel.CO2Scale.value) }
        val defaultWFSpeed = remember { mutableStateOf(viewModel.WFSpeed.value) }

        Column {
            WheelPicker(
                config = wheelPickerConfig(items = co2Units, title = "CO2 单位", defaultValue = defaultUnit.value),
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
                        defaultScale.value = co2Scales[1]
                    }
                }
            )

            WheelPicker(
                config = wheelPickerConfig(items = co2Scales, title = "CO2 Scale", defaultValue = defaultScale.value),
                onValueChange = {
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

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card (
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable {
                        checkHasConnectDevice {
                            viewModel.updateCO2Unit(defaultUnit.value)
                            viewModel.updateCo2Scales(co2Scales)
                            viewModel.updateCO2Scale(defaultScale.value)
                            viewModel.updateWFSpeed(defaultWFSpeed.value)
                            viewModel.updateLoadingData(
                                LoadingData(
                                    text = "正在设置",
                                    duration = InfinityDuration,
                                )
                            )
                            blueToothKit.updateCO2UnitScale(
                                co2Scale = defaultScale.value,
                                co2Unit = defaultUnit.value,
                                callback = {
                                    viewModel.clearXData()
                                    viewModel.updateToastData(
                                        ToastData(
                                            text = "设置成功",
                                            showMask = false,
                                            duration = 600,
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
                            .padding(horizontal = 30.dp, vertical = 16.dp)
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