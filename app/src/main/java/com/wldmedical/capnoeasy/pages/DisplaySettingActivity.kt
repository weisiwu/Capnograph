package com.wldmedical.capnoeasy.pages

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
import com.wldmedical.capnoeasy.CO2_SCALE
import com.wldmedical.capnoeasy.CO2_UNIT
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.WF_SPEED
import com.wldmedical.capnoeasy.co2Scales
import com.wldmedical.capnoeasy.co2ScalesObj
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
    override val pageScene = PageScene.DISPLAY_CONFIG_PAGE

    // TODO: 这里没有很好的初始哈
    var CO2Unit: CO2_UNIT = CO2_UNIT.MMHG
    var CO2Scale: CO2_SCALE = CO2_SCALE.MIDDLE
    var WFSpeed: WF_SPEED = WF_SPEED.MIDDLE
    
    @Composable
    override fun Content() {
        val defaultUnitIndex = co2Units.indexOfFirst { co2Unit ->  co2Unit == viewModel.CO2Unit.value }
        val defaultScaleIndex = co2Scales.indexOfFirst { co2Scale ->  co2Scale == viewModel.CO2Scale.value }
        val defaultWFIndex = wfSpeeds.indexOfFirst { wfSpeed ->  wfSpeed == viewModel.WFSpeed.value }

        Column {
            WheelPicker(
                config = wheelPickerConfig(items = co2Units, title = "CO2 单位", defaultValue = co2Units[defaultUnitIndex]),
                onValueChange = {
                    if (it >= 0 && it < co2UnitsObj.items.size) {
                        CO2Unit = co2UnitsObj.items[it]
                    }
                }
            )

            WheelPicker(
                config = wheelPickerConfig(items = co2Scales, title = "CO2 Scale", defaultValue = co2Scales[defaultScaleIndex]),
                onValueChange = {
                    if (it >= 0 && it < co2ScalesObj.items.size) {
                        CO2Scale = co2ScalesObj.items[it]
                    }
                }
            )

            WheelPicker(
                config = wheelPickerConfig(items = wfSpeeds, title = "WF Speed", defaultValue = wfSpeeds[defaultWFIndex]),
                onValueChange = {
                    if (it >= 0 && it < wfSpeedsObj.items.size) {
                        WFSpeed = wfSpeedsObj.items[it]
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
                        viewModel.updateCO2Unit(CO2Unit)
                        viewModel.updateCO2Scale(CO2Scale)
                        viewModel.updateWFSpeed(WFSpeed)
                        viewModel.updateLoadingData(
                            LoadingData(
                                text = "正在设置",
                                duration = 800,
                                callback = {
                                    viewModel.updateToastData(
                                        ToastData(
                                            text = "设置成功",
                                            showMask = false,
                                            duration = 600,
                                        )
                                    )
                                }
                            )
                        )
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