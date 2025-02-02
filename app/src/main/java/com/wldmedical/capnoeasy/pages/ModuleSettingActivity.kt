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
import com.wldmedical.capnoeasy.O2_UNIT
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.TIME_UNIT
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
    override val pageScene = PageScene.MODULE_CONFIG_PAGE

    var asphyxiationTime: Int = 0
    var o2Compensation: Float = 0f

    @Composable
    override fun Content() {
        asphyxiationTime = viewModel.asphyxiationTime.value
        o2Compensation = viewModel.o2Compensation.value

        Column {
            RangeSelector(
                title = "窒息时间(${TIME_UNIT})",
                value = asphyxiationTime.toFloat(),
                unit = TIME_UNIT,
                type = RangeType.ONESIDE,
                valueRange = asphyxiationTimeRange,
                onValueChange = { newVal, _ ->
                    asphyxiationTime = newVal.toInt()
                }
            )

            RangeSelector(
                title = "氧气补偿(${O2_UNIT})",
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

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card (
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable {
                        viewModel.updateAsphyxiationTime(asphyxiationTime)
                        viewModel.updateO2Compensation(o2Compensation)
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