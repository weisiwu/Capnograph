package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerFocusVertical
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import com.wldmedical.capnoeasy.WF_SPEED
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.wfSpeeds
import com.wldmedical.capnoeasy.wfSpeedsObj
import com.wldmedical.capnoeasy.wheelPickerConfig

/**
 * App 通用滚动选择器
 * 适用于
 * 1、显示参数
 */
@Composable
fun WheelPicker(
    onValueChange: ((Int) -> Unit)? = null,
    config: wheelPickerConfig<*,*>,
) {
    val (title, items, defaultValue) = config
    val defaultIndex = items.indexOf(defaultValue)
    val state = rememberFWheelPickerState(defaultIndex)

    LaunchedEffect(state.currentIndex) {
        onValueChange?.invoke(state.currentIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 0.dp)
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

        FVerticalWheelPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            count = 3,
            itemHeight = 40.dp,
            state = state ,
            focus = {
                FWheelPickerFocusVertical(dividerColor = Color(0xffDFE6E9), dividerSize = 1.dp)
            }
        ) { index ->
            Text(
                fontSize = 18.sp,
                color = Color(0xff1D2129),
                text = items[index].value.toString()
            )
        }

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
                .height(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RollerSelectorPreview() {
    CapnoEasyTheme {
        Column {
            WheelPicker(
                config = co2UnitsObj
            )

            WheelPicker(
                config = wheelPickerConfig(items = wfSpeeds, title = "WF Speed", defaultValue = WF_SPEED.MIDDLE)
            )

            WheelPicker(
                config = wfSpeedsObj
            )
        }
    }
}