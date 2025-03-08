package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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

@Composable
fun CustomTextField(
    title: String? = null,
    value: String = "",
    defaultText: String = "",
    onValueChange: ((String) -> Unit)? = null
) {
    val inputText = remember { mutableStateOf(value) }

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
                onValueChange?.invoke(newVal)
                inputText.value = newVal
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin,
                color = Color(0xff1D2129)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
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

@Preview(showBackground = true)
@Composable
fun PrintDeviceConfigPreview() {
    CapnoEasyTheme {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            CustomTextField(title = "地址", defaultText = "请输入地址" )
            CustomTextField(title = "电话", defaultText = "请输入电话" )
            CustomTextField(title = "网址", defaultText = "请输入网址" )
        }
    }
}