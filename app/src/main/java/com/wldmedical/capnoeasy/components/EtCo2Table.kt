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
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.kits.BlueToothKit
import com.wldmedical.capnoeasy.kits.BlueToothKitManager.blueToothKit
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.patientAgeRange
import kotlin.math.floor

val baseRowHeight = 56.dp

@Composable
fun AttributeLine(
    viewModel: AppStateModel,
    blueToothKit: BlueToothKit,
    attribute: Atribute,
    modifier: Modifier = Modifier,
    onInputChange: ((newVal: String) -> Unit)? = null,
) {
    val value = when(attribute.viewModelName) {
        "rr" -> blueToothKit.currentRespiratoryRate.value.toString()
        "etCO2" -> blueToothKit.currentETCO2.value.toInt().toString()
        "patientName" -> viewModel.patientName.value ?: ""
        "patientGender" -> if(viewModel.patientGender.value == null) "" else viewModel.patientGender.value!!.title
        "patientAge" -> if(viewModel.patientAge.value == null || viewModel.patientAge.value == 0) "" else viewModel.patientAge.value.toString()
        "patientID" -> if(viewModel.patientID.value == null) "" else viewModel.patientID.value.toString()
        "department" -> if(viewModel.patientDepartment.value == null) "" else viewModel.patientDepartment.value.toString()
        "bedNumber" -> if(viewModel.patientBedNumber.value == null) "" else viewModel.patientBedNumber.value.toString()
        else -> viewModel.rr.value.toString()
    }

    var valueFontWeight = FontWeight.Bold
    var valueText = value
    var valueColor = Color(0xff1D2129)

    if (value.isEmpty()) {
        valueFontWeight = FontWeight.Light
        valueText = attribute.placeholder
        valueColor = Color.Gray
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(if (attribute.fullRow) 1f else 0.4f)
            .wrapContentHeight()
            .background(Color.Transparent)
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
                                text = getString(R.string.etco2table_select_gender),
                                ok_btn_text = getString(R.string.etco2table_male),
                                cancel_btn_text = getString(R.string.etco2table_female),
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
                    text = valueText,
                    fontWeight = valueFontWeight,
                    fontSize = 16.sp,
                    color = valueColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End // 设置 Text 内部文本居左对齐
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
                        "department" -> viewModel.updatePatientDepartment(newVal)
                        "bedNumber" -> viewModel.updatePatientBedNumber(newVal)
                        "patientName" -> viewModel.updatePatientName(newVal)
                        "patientID" -> viewModel.updatePatientID(newVal)
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
                        text = valueText,
                        color = valueColor,
                        fontWeight = valueFontWeight,
                        modifier = Modifier.fillMaxWidth().weight(1f)
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
                    text = if (attribute.viewModelName == "rr")
                        blueToothKit.currentRespiratoryRate.value.toString()
                        else floor(blueToothKit.currentETCO2.value).toString(),
                    fontWeight = valueFontWeight,
                    fontSize = 16.sp,
                    color = valueColor,
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
    val isSelect: Boolean = false,
    val fullRow: Boolean = false,
)

val attributes = listOf(
    Atribute(title = "RR", viewModelName = "rr", placeholder = "--", editable = false, fullRow = true),
    Atribute(title = "ETCO2", viewModelName = "etCO2", placeholder = "0 mmHg", editable = false, fullRow = true),
)

val attributesGroupA = listOf(
    Atribute(
        title = getString(R.string.etco2table_name), 
        viewModelName = "patientName", 
        placeholder = getString(R.string.etco2table_fill_in), 
        editable = true
    ),
    Atribute(
        title = getString(R.string.etco2table_gender), 
        viewModelName = "patientGender", 
        placeholder = getString(R.string.etco2table_select), 
        editable = true,
        isSelect = true
    ),
)

val attributesGroupB = listOf(
    Atribute(
        title = getString(R.string.etco2table_age), 
        viewModelName = "patientAge", 
        placeholder = getString(R.string.etco2table_input), 
        editable = true, 
        isNumber = true
    ),
    Atribute(title = "ID", 
        viewModelName = "patientID", 
        placeholder = getString(R.string.etco2table_input), 
        editable = true
    ),
)

val attributesGroupC = listOf(
    Atribute(
        title = getString(R.string.etco2table_depart),
        viewModelName = "department",
        placeholder = getString(R.string.etco2table_input),
        editable = true,
        isNumber = true
    ),
    Atribute(
        title = getString(R.string.etco2table_bed),
        viewModelName = "bedNumber",
        placeholder = getString(R.string.etco2table_input),
        editable = true,
        isNumber = true
    ),
)

/**
 * App 主页，展示呼吸率、ETCO2、姓名、性别、年龄等文字数据
 */
@Composable
fun EtCo2Table(
    viewModel: AppStateModel,
    blueToothKit: BlueToothKit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupA[0],
            modifier = Modifier.padding(start = 28.dp).weight(1f)
        )
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupA[1],
            modifier = Modifier.padding(end = 29.dp).weight(1f)
        )
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupB[0],
            modifier = Modifier.padding(start = 28.dp).weight(1f)
        )
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupB[1],
            modifier = Modifier.padding(end = 13.dp).weight(1f)
        )
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupC[0],
            modifier = Modifier.padding(start = 28.dp).weight(1f)
        )
        AttributeLine(
            viewModel = viewModel,
            blueToothKit = blueToothKit,
            attribute = attributesGroupC[1],
            modifier = Modifier.padding(end = 13.dp).weight(1f)
        )
    }

    LazyColumn {
        items(attributes) { attribute ->
            AttributeLine(
                viewModel = viewModel,
                blueToothKit = blueToothKit,
                attribute = attribute,
                modifier = Modifier.padding(start = 28.dp, end = 28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EtCo2TablePreview() {
    CapnoEasyTheme {
        Row(
            modifier = Modifier.background(Color.White)
        ) {
            EtCo2Table(
                blueToothKit = blueToothKit,
                viewModel = AppStateModel(appState = AppState())
            )
        }
    }
}