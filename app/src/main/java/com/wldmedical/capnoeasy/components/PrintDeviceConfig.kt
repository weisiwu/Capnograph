package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SupportQRCodeType(
    override val name: String,
    override val id: String,
    override val index: Int
): CustomType

val SupportQRCodeTypes: Array<CustomType> = arrayOf(
    SupportQRCodeType(name = "是", id = "是", index = 0),
    SupportQRCodeType(name = "否", id = "否", index = 1),
)

// TODO: 缺少一个选择图片的能力
@Composable
fun CustomTextField(
    title: String? = null,
    defaultText: String = "",
) {
    val inputText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        if(title != null) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = Color(0xff666666),
                modifier = Modifier.padding(top = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
        TextField(
            value = inputText.value,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(Color.Transparent),
            onValueChange = { newVal ->
                inputText.value = newVal
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                color = Color(0xff1D2129)
            ),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = Color.White,
//                focusedIndicatorColor = Color.White,
//                unfocusedIndicatorColor = Color.White
//            ),
            placeholder = {
                Text(
                    text = defaultText,
                    fontSize = 17.sp,
                    color = Color(0xffCCCCCC),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                )
            },
        )
        HorizontalDivider(
            modifier = Modifier.run {
                fillMaxWidth()
                    .height(2.dp)
                    .alpha(0.4f)
            }
        )
    }
}


/**
 * App 打印设置页提交表单
 */
@Composable
fun PrintDeviceConfig(
    onSelectLogo: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null,
) {
    val isSupportQRCode = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomTextField(title = "地址", defaultText = "请输入地址" )
        CustomTextField(title = "电话", defaultText = "请输入电话" )
        CustomTextField(title = "网址", defaultText = "请输入网址" )
        Column {
            Text(
                text = "是否展示网址二维码",
                color = Color(0xff666666),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TypeSwitch(
                    onTypeClick = { type ->
                        isSupportQRCode.value = type.id == "是"
                    },
                    types = SupportQRCodeTypes
                )
            }
            HorizontalDivider(
                modifier = Modifier.run {
                    fillMaxWidth()
                        .height(2.dp)
                        .alpha(0.4f)
                        .padding(horizontal = 18.dp)
                }
            )
        }
        Column {
            Text(
                text = "Logo上传",
                color = Color(0xff666666),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(top = 18.dp)
            )
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xffF5F5F5))
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onSelectLogo?.invoke()
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "选择图片",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            HorizontalDivider(
                modifier = Modifier.run {
                    fillMaxWidth()
                        .height(2.dp)
                        .alpha(0.4f)
                        .padding(top = 18.dp)
                        .padding(horizontal = 18.dp)
                }
            )
        }

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
                    // TODO: 保存所有配置
                    onSave?.invoke()
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

@Preview(showBackground = true)
@Composable
fun PrintDeviceConfigPreview() {
    CapnoEasyTheme {
        PrintDeviceConfig()
    }
}