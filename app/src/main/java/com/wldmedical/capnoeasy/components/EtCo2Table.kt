package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.patientAgeRange

val baseRowHeight = 56.dp

@Composable
fun AttributeLine(
    viewModel: AppStateModel,
    attribute: Atribute,
    onInputChange: ((newVal: String) -> Unit)? = null,
) {
    var value = ""
    when(attribute.viewModelName) {
        "rr" -> value = viewModel.rr.value.toString()
        "etCO2" -> value = viewModel.etCO2.value.toString()
        "patientName" -> value = viewModel.patientName.value ?: ""
        "patientGender" -> value = viewModel.patientGender.value.title
        "patientAge" -> value = viewModel.patientAge.value.toString()
        else -> value = viewModel.rr.value.toString()
    }
    
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
            .padding(start = 28.dp, end = 28.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(baseRowHeight)
        ) {
            Text(
                text = attribute.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        if (attribute.isSelect) {
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(baseRowHeight)
                    .clickable {
                        // 直接换出alert弹框，让用户进行选择
                        viewModel.updateAlertData(
                            AlertData(
                                text = "请选择病人性别",
                                ok_btn_text = "男",
                                cancel_btn_text = "女",
                                onOk = {
                                    viewModel.updateAlertData(null)
                                    viewModel.updatePatientGender(GENDER.MALE)
                                },
                                onCancel = {
                                    viewModel.updateAlertData(null)
                                    viewModel.updatePatientGender(GENDER.FORMALE)
                                }
                            ),
                        )
                    }
            ) {
                Text(
                    text = if (value != "") value else attribute.placeholder,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xff1D2129),
                    modifier = Modifier
                )
            }
        }  else if (attribute.editable) {
            // 不纠结在TextField里面的ContentPadding无法调整
            // https://juejin.cn/post/6998038393003180046
            TextField(
                value = value,
                singleLine = true,
                modifier = Modifier
                    .height(baseRowHeight)
                    .padding(0.dp)
                    .alignByBaseline()
                    .weight(1f)
                    .background(Color.Transparent),
                keyboardOptions = if(attribute.isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
                onValueChange = { newVal ->
                    when(attribute.viewModelName) {
                        "patientName" -> viewModel.updatePatientName(newVal)
                        "patientAge" -> {
                            val intVal = newVal.toIntOrNull() ?: 0
                            if (intVal >= patientAgeRange.start && intVal <= patientAgeRange.last) {
                                viewModel.updatePatientAge(intVal)
                            }
                        }
                        else -> println("Attribute cant edit")
                    }
                    onInputChange?.invoke(newVal)
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
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
                        textAlign = TextAlign.End,
                        text = attribute.placeholder,
                        color = Color(0xff1D2129),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        } else {
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(baseRowHeight)
            ) {
                Text(
                    text = if (value != "") value else attribute.placeholder,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xff1D2129),
                    modifier = Modifier
                )
            }
        }
    }
}

data class Atribute(
    val title: String,
    val placeholder: String,
    val viewModelName: String,
    val editable: Boolean,
    val isNumber: Boolean = false,
    val isSelect: Boolean = false
)

val attributes = listOf(
    Atribute(title = "RR", viewModelName = "rr", placeholder = "--", editable = false),
    Atribute(title = "ETCO2", viewModelName = "etCO2", placeholder = "0 mmHg", editable = false),
    Atribute(title = "姓名", viewModelName = "patientName", placeholder = "请填写", editable = true),
    Atribute(title = "性别", viewModelName = "patientGender", placeholder = "请选择", editable = true, isSelect = true),
    Atribute(title = "年龄", viewModelName = "patientAge", placeholder = "请输入", editable = true, isNumber = true),
)

/**
 * App 主页，展示呼吸率、ETCO2、姓名、性别、年龄等文字数据
 */
@Composable
fun EtCo2Table(
    viewModel: AppStateModel,
    onTypeClick: ((device: DeviceType) -> UInt)? = null,
) {
    LazyColumn {
        items(attributes) { attribute ->
            AttributeLine(
                viewModel = viewModel,
                attribute = attribute,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EtCo2TablePreview() {
    CapnoEasyTheme {
        EtCo2Table(viewModel = AppStateModel(appState = AppState()))
    }
}