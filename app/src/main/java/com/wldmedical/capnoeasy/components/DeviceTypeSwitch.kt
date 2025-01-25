package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

    Box(
        modifier = Modifier
            .width(111.dp)
            .height(37.dp)
            .padding(0.dp, end = 9.dp)
            .background(baseBgColor)
            .clip(RoundedCornerShape(2.dp))
            .clickable {
                onClick?.invoke()
            }
    ) {
        if (isSelected) {
            Image(
                painter = painterResource(R.drawable.device_tyoe_mark),
                contentDescription = "选中",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(108.dp)
                    .height(36.dp)
                    .padding(0.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(2.dp)) // 裁剪图片
            )
        }
        Text(
            text = text,
            color = baseFontColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(0.dp)
                .background(Color(0xffF5F5F5)),
        )
    }
}

data class Device(
    val name: String,
    val id: String,
    val index: Int,
)

val DeviceTypes: Array<Device> = arrayOf(
//    Device(name = "经典蓝牙", id = "BLUETOOTH_CLASSIC"),
    Device(name = "蓝牙", id = "BLUETOOTH_LOWENERGY", index = 0),
    Device(name = "WIFI", id = "WIFI", index = 1),
    Device(name = "USB", id = "USB", index = 2),
)

/**
 * App 设备列表页，切换设备类型
 */
@Composable
fun DeviceTypeSwitch(
    onTypeClick: ((device: Device) -> UInt)? = null,
) {
    val selectedIndex = remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .background(Color.Black)
    ) {
        for (device in DeviceTypes) {
            DeviceType(
                text = device.name,
                isSelected = device.index == selectedIndex.value,
                onClick = {
                    selectedIndex.intValue = 0
                    onTypeClick?.invoke(device)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceTypeSwitchPreview() {
    CapnoEasyTheme {
        DeviceTypeSwitch()
    }
}