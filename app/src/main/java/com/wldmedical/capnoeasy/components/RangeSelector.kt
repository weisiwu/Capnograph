package com.wldmedical.capnoeasy.components

import android.service.controls.templates.RangeTemplate
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import com.wldmedical.capnoeasy.R

enum class RangeType{
    BOTH,
    ONESIDE
}

/**
 * App 通用范围选择器
 * 适用于
 * 1、报警参数
 * 2、模块餐宿
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSelector(
    title: String = "",
    type: RangeType = RangeType.ONESIDE,
    value: Float = 0f,
    startValue: Float = 0f,
    endValue: Float = 0f,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 1,
) {
    val singlePosition = remember { mutableFloatStateOf(value) }
    val bothPosition = remember { mutableStateOf(startValue..endValue) }
    val thumbColors = SliderDefaults.colors(
        activeTrackColor = Color(0xff00CEC9),
        inactiveTrackColor = Color(0xffDFE6E9),
        thumbColor = Color(0xff00CEC9)
    );

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        if(title.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // TODO: 值的位置不对，现在覆盖在滑块上
        if(type == RangeType.ONESIDE) {
            Slider(
                value = singlePosition.value,
                valueRange = valueRange,
                steps = steps,
                thumb = {
                    Image(
                        painterResource(id = R.drawable.oneside_range_thumb),"选择器滑块",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        text = singlePosition.value.toString(),
                    )
                },
                colors = thumbColors,
                onValueChange = { singlePosition.value = it }
            )
        } else if(type == RangeType.BOTH) {
            RangeSlider(
                value = bothPosition.value,
                valueRange = valueRange,
                steps = steps,
                colors = thumbColors,
                startThumb = {
                    Image(
                        painterResource(id = R.drawable.both_range_left_thumb),"选择器左侧滑块",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(0.dp)
                    )
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        text = bothPosition.value.start.toString(),
                    )
                },
                endThumb = {
                    Image(
                        painterResource(id = R.drawable.both_range_right_thumb),"选择器右侧滑块",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(0.dp)
                    )
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        text = bothPosition.value.endInclusive.toString()
                    )
                },
                onValueChange = { range -> bothPosition.value = range },
                onValueChangeFinished = {
                },
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xffDFE6E9))
                .alpha(0.4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RangeSelectorPreview() {
    CapnoEasyTheme {
        Column {
            RangeSelector(
                title = "大气压(mmHg)",
                value = 12.3f,
                type = RangeType.ONESIDE,
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
}