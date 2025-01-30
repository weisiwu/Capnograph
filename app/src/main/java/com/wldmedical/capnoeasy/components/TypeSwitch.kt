package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

interface CustomType {
    val name: String
    val id: String
    val index: Int
}

data class DeviceType(
    override val name: String,
    override val id: String,
    override val index: Int
): CustomType

enum class DeviceTypeList(val deviceType: CustomType) {
    BLE(DeviceType(name = "蓝牙", id = "BLUETOOTH_LOWENERGY", index = 0)),
    WIFI(DeviceType(name = "WIFI", id = "WIFI", index = 1)),
    USB(DeviceType(name = "USB", id = "USB", index = 2)),
    BLUETHOOTH(DeviceType(name = "经典蓝牙", id = "BLUETOOTH_CLASSIC", index = 3)),
}

val DeviceTypes: Array<CustomType> = arrayOf(
    DeviceTypeList.BLE.deviceType,
    DeviceTypeList.WIFI.deviceType,
    DeviceTypeList.USB.deviceType,
//    DeviceTypeList.BLUETHOOTH.deviceType,
)

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

/**
 * App 设备列表页，切换设备类型
 */
@Composable
fun TypeSwitch(
    modifier: Modifier = Modifier,
    types: Array<CustomType>,
    onTypeClick: ((type: CustomType) -> Unit)? = null,
) {
    val selectedIndex = remember { mutableIntStateOf(0) }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        for (type in types) {
            DeviceType(
                text = type.name,
                isSelected = type.index == selectedIndex.intValue,
                onClick = {
                    selectedIndex.intValue = 0
                    onTypeClick?.invoke(type)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TypeSwitchPreview() {
    CapnoEasyTheme {
        TypeSwitch(
            types = DeviceTypes
        )
    }
}