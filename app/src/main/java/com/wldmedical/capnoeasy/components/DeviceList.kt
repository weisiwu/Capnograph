package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.kits.unkownName

data class Device(
    val name: String,
    val mac: String,
    val type: CustomType = DeviceTypeList.BLE.deviceType
)

val emptyAlert = "附近没有可配对设备！"

/**
 * App 设备列表页 - 设备列表
 * 表格等待外部注入数据，本身不请求数据
 * 有数据的时候展示列表
 * 无数据的时候展示空数据兜底样式
 */
@SuppressLint("MissingPermission")
@Composable
fun DeviceList(
    devices: List<BluetoothDevice>,
    onSearch: ((CustomType) -> Unit)? = null,
    onDeviceClick: ((device: BluetoothDevice) -> Unit)? = null,
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val searchBtnHeight = 65.dp;
    val deviceListMinHeight = 100.dp
    val deviceListMaxHeight = max(screenHeightDp * 0.6f, 500.dp)

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = deviceListMinHeight, max = deviceListMaxHeight)
    ) {
        if(devices.isEmpty()) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.empty_device_list),
                    contentDescription = emptyAlert,
                    modifier = Modifier.size(150.dp).padding(bottom = 16.dp, top = 16.dp)
                )
                Text(
                    text = emptyAlert,
                    fontSize = 20.sp,
                    color = Color(0xFF687383)
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.heightIn(max = deviceListMaxHeight - searchBtnHeight)
            ) {
                items(devices) { device ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 27.dp)
                    ) {
                        Column {
                            Text(
                                text = device.name ?: unkownName,
                                fontSize = 17.sp,
                            )
                            Text(
                                text = device.address,
                                fontSize = 15.sp,
                                color = Color(0xff888888)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            colors = ButtonColors(
                                containerColor = Color(0xffE8F3FF),
                                contentColor = Color(0xffE8F3FF),
                                disabledContainerColor = Color(0xffE8F3FF),
                                disabledContentColor = Color(0xffE8F3FF)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xffE8F3FF))
                                .size(width = 68.dp, height = 32.dp),
                            onClick = {
                                onDeviceClick?.invoke(device)
                            },
                            contentPadding = PaddingValues(0.dp),
                            content = {
                                Text(
                                    text = "链接",
                                    modifier = Modifier.clip(RoundedCornerShape(2.dp)),
                                    color = Color(0xff165DFF),
                                )
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(searchBtnHeight)
        ) {
            Button(
                colors = ButtonColors(
                    containerColor = Color(0xffE8F3FF),
                    contentColor = Color(0xffE8F3FF),
                    disabledContainerColor = Color(0xffE8F3FF),
                    disabledContentColor = Color(0xffE8F3FF)
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xffE8F3FF))
                    .size(width = 104.dp, height = 35.dp),
                onClick = {
                    // TODO: 这里临时写死，后续需要从外部传入
                    onSearch?.invoke(DeviceTypeList.BLE.deviceType)
                },
                contentPadding = PaddingValues(0.dp),
                content = {
                    Text(
                        text = "重新搜索",
                        modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                        color = Color(0xff165DFF),
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceListPreview() {
    val devices = listOf<BluetoothDevice>()
    CapnoEasyTheme {
        Row(
            modifier = Modifier.background(Color.White).fillMaxSize()
        ) {
            DeviceList(devices = devices)
        }
    }
}