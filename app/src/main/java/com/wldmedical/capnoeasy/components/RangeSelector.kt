package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.github.mikephil.charting.formatter.ValueFormatter
import com.wldmedical.capnoeasy.FloatToFixed
import com.wldmedical.capnoeasy.R
import kotlin.math.absoluteValue

enum class RangeType{
    BOTH,
    ONESIDE
}

val titleFontSize = 16.sp
val thumbFontSize = 14.sp

/**
 * App 通用范围选择器
 * 适用于
 * 1、报警参数
 * 2、模块餐宿
 */
@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSelector(
    title: String = "",
    unit: String = "",
    format: ((Float) -> String)? = null,
    enabled: Boolean = true,
    type: RangeType = RangeType.ONESIDE,
    value: Float = 0f,
    startValue: Float = 0f,
    endValue: Float = 0f,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: ((Float, Float?) -> Unit)? = null,
) {
    val _startValue = startValue.coerceAtLeast(valueRange.start).coerceAtMost(valueRange.endInclusive)
    val _endValue = endValue.coerceAtLeast(valueRange.start).coerceAtMost(valueRange.endInclusive)
    val _value = value.coerceAtLeast(valueRange.start).coerceAtMost(valueRange.endInclusive)
    val totalOffset = if (unit != "") 200 else 100
    val singlePosition = remember { mutableFloatStateOf(_value) }
    val bothPosition = remember { mutableStateOf(_startValue.._endValue) }
    val startTextMeasurer = rememberTextMeasurer()
    val endTextMeasurer = rememberTextMeasurer()
    val thumbColors = SliderDefaults.colors(
        activeTrackColor = Color(0xff00CEC9),
        inactiveTrackColor = Color(0xffDFE6E9),
        thumbColor = Color(0xff00CEC9),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xffDFE6E9))
                .alpha(0.4f)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        if(title.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 因为滑块值会向上移动-30.dp，所以要预留空间出来
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )

        if(type == RangeType.ONESIDE) {
            Slider(
                enabled = enabled,
                value = singlePosition.floatValue,
                valueRange = valueRange,
                thumb = {
                    Image(
                        painterResource(id = R.drawable.oneside_range_thumb),"选择器滑块",
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                colors = thumbColors,
                onValueChange = {
                    singlePosition.floatValue = it
                    onValueChange?.invoke(it, null)
                },
                modifier = Modifier.drawWithContent {
                    drawContent()

                    val offset = singlePosition.floatValue / valueRange.endInclusive * totalOffset
                    val thumbX = (singlePosition.floatValue - valueRange.start) / (valueRange.endInclusive - valueRange.start) * size.width
                    val valStr = format?.invoke(singlePosition.floatValue) ?: FloatToFixed.format(singlePosition.floatValue)

                    drawText(
                        textMeasurer = startTextMeasurer,
                        text = valStr + unit,
//                        text = FloatToFixed.format(singlePosition.floatValue) + unit,4
                        topLeft = Offset(thumbX - offset, -30f),
                        style = TextStyle(fontSize = thumbFontSize)
                    )
                }
            )
        } else if(type == RangeType.BOTH) {
            RangeSlider(
                value = bothPosition.value,
                valueRange = valueRange,
                colors = thumbColors,
                modifier = Modifier.height(60.dp).drawWithContent {
                    drawContent()

                    // TODO: 这里正确的思路是，thumbStart 值存在问题，而不是估算偏移来弥补误差
                    val startOffset = bothPosition.value.start / valueRange.endInclusive * totalOffset
                    val endOffset = bothPosition.value.endInclusive / valueRange.endInclusive * totalOffset
                    val thumbStart = (bothPosition.value.start - valueRange.start) / (valueRange.endInclusive - valueRange.start) * size.width

                    drawText(
                        textMeasurer = startTextMeasurer,
                        text = FloatToFixed.format(bothPosition.value.start) + unit,
                        topLeft = Offset(thumbStart - startOffset, 0f),
                        style = TextStyle(fontSize = thumbFontSize)
                    )

                    val thumbEnd = (bothPosition.value.endInclusive - valueRange.start) / (valueRange.endInclusive - valueRange.start) * size.width

                    drawText(
                        textMeasurer = endTextMeasurer,
                        text = FloatToFixed.format(bothPosition.value.endInclusive) + unit,
                        topLeft = Offset(thumbEnd - endOffset, 0f),
                        style = TextStyle(fontSize = thumbFontSize)
                    )
                },
                startThumb = {
                    Image(
                        painterResource(id = R.drawable.both_range_left_thumb),"选择器左侧滑块",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(0.dp)
                    )
                },
                endThumb = {
                    Image(
                        painterResource(id = R.drawable.both_range_right_thumb),"选择器右侧滑块",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(0.dp)
                            .background(Color.Transparent)
                    )
                },
                onValueChange = { range -> bothPosition.value = range
                    onValueChange?.invoke(range.start, range.endInclusive)
                },
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RangeSelectorPreview() {
    CapnoEasyTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            RangeSelector(
                title = "大气压(mmHg)",
                unit = "mmHg",
                value = 18f,
                type = RangeType.ONESIDE,
                valueRange = 0f..30f,
            )

            RangeSelector(
                title = "ETCO2 范围",
                unit = "mmHg",
                type = RangeType.BOTH,
                startValue = 11f,
                endValue = 25f,
                valueRange = 0f..30f,
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