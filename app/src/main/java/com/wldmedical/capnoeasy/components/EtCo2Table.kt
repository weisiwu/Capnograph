package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// TODO: 默认文字没有距右对齐
// TODO: 无法输入文字
// TODO: 年龄输入法不是数字
// TODO: 无法选择性别 - 性别为什么不做成可以点选的？
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributeLine(
    attribute: Atribute,
    onInputChange: ((newVal: String) -> Unit)? = null,
    isNumber: Boolean = false,
) {
    var inputText = remember { mutableStateOf(attribute.value) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White)
            .padding(start = 28.dp, end = 28.dp, bottom = 12.dp)
    ) {
        Text(
            text = attribute.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .alignByBaseline()
                .fillMaxHeight(),
        )
        if (attribute.editable) {
            TextField(
                value = attribute.value,
                singleLine = true,
                modifier = Modifier
                    .fillMaxHeight()
                    .alignByBaseline()
                    .background(Color.Transparent),
                keyboardOptions = if(isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
                onValueChange = { newVal ->
                    onInputChange?.invoke(newVal)
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color(0xff1D2129)
                ),
//                colors = TextFieldDefaults.textFieldColors(
//                    containerColor = Color.White,
//                    focusedIndicatorColor = Color.White,
//                    unfocusedIndicatorColor = Color.White
//                ),
                placeholder = {
                    Text(
                        text = attribute.defaultValue,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        } else {
            Text(
                text = if (attribute.value != "") attribute.value else attribute.defaultValue,
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
                color = Color(0xff1D2129),
                modifier = Modifier.alignByBaseline()
            )
        }
    }
}

data class Atribute(
    val title: String,
    val value: String,
    val defaultValue: String,
    val editable: Boolean,
    val isNumber: Boolean = false
)

val attributes = listOf(
    Atribute(title = "RR", value = "", defaultValue = "--", editable = false),
    Atribute(title = "ETCO2", value = "", defaultValue = "0 mmHg", editable = false),
    Atribute(title = "姓名", value = "", defaultValue = "请填写", editable = true),
    Atribute(title = "性别", value = "", defaultValue = "请选择", editable = true),
    Atribute(title = "年龄", value = "", defaultValue = "请输入", editable = true, isNumber = true),
)

        /**
 * App 主页，展示呼吸率、ETCO2、姓名、性别、年龄等文字数据
 */
@Composable
fun EtCo2Table(
    onTypeClick: ((device: DeviceType) -> UInt)? = null,
) {
    LazyColumn {
        items(attributes) { attribute ->
            AttributeLine(
                attribute = attribute,
                onInputChange = { newVal ->
                    println("wswTestETCO2Table 输入 $newVal")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EtCo2TablePreview() {
    CapnoEasyTheme {
        EtCo2Table()
    }
}