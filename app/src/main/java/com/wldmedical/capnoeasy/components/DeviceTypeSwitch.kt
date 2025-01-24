package com.wldmedical.capnoeasy.components

import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.R

@Composable
fun DeviceType(
    text: String,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val baseBgColor = if(isSelected) Color(0xffE7F1FF) else Color(0xffF5F5F5)
    val baseFontColor = if(isSelected) Color(0xff1677FF) else Color.Black

    Button(
        onClick = {
            onClick?.invoke()
        },
        shape = RoundedCornerShape(2.dp),
        colors = ButtonColors(
            containerColor = baseBgColor,
            contentColor = baseBgColor,
            disabledContainerColor = baseBgColor,
            disabledContentColor = baseBgColor
        ),
        modifier = Modifier
            .width(111.dp)
            .height(37.dp)
            .padding(end = 9.dp)
    ) {
        Box {
            if(isSelected) {
                Image(
                    painter = painterResource(R.drawable.device_tyoe_mark),
                    contentDescription = "选中",
                    modifier = Modifier
                        .width(111.dp)
                        .height(37.dp)
                )
            }
            Text(
                text = text,
                color = baseFontColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color(0xffF5F5F5)),
            )
        }
    }
}

/**
 * App 设备列表页，切换设备类型
 */
@Composable
fun DeviceTypeSwitch() {
    val selectedIndex = remember { mutableIntStateOf(0) }
    // TODO: 这里要有一个观察变量，值根据index变化，主要就是是否选中

    Row(
        modifier = Modifier.background(Color.Black)
    ) {
        DeviceType(
            text = "蓝牙",
            isSelected = true,
            onClick = {
                selectedIndex.intValue = 0
            }
        )
        DeviceType(
            text = "WIFI",
            isSelected = false,
            onClick = {
                selectedIndex.intValue = 1
            }
        )
        DeviceType(
            text = "USB",
            isSelected = false,
            onClick = {
                selectedIndex.intValue = 2
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceTypeSwitchPreview() {
    CapnoEasyTheme {
        DeviceTypeSwitch()
    }
}