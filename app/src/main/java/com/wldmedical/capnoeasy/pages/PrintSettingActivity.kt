package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.CustomTextField
import com.wldmedical.capnoeasy.components.CustomType
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.TypeSwitch

data class OutputType(
    override val name: String,
    override val id: String,
    override val index: Int
): CustomType

/***
 * 设置二级页 - 打印
 */
class PrintSettingActivity : BaseActivity() {
    override var pageScene = PageScene.PRINT_CONFIG_PAGE

    var pdfHospitalName: String = ""
    var pdfReportName: String = ""

    @Composable
    override fun Content() {
        pdfHospitalName = viewModel.pdfHospitalName.value
        pdfReportName = viewModel.pdfReportName.value
        val isPDF = remember { mutableStateOf(viewModel.isPDF.value) }

        val context = this

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()) // 使用 verticalScroll
        ) {
            // 医院名称
            CustomTextField(
                title = getString(R.string.print_pdf_hospital_name),
                defaultText = getString(R.string.print_input_name),
                value = pdfHospitalName,
                onValueChange = {
                    pdfHospitalName = it
                }
            )
            // 报告名称
            CustomTextField(
                title = getString(R.string.print_pdf_report_name),
                defaultText = getString(R.string.print_input_name),
                value = pdfReportName,
                onValueChange = {
                    pdfReportName = it
                }
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // 输出什么类型
            TypeSwitch(
                selectedIndex = if (isPDF.value) 0 else 1,
                onTypeClick = { type ->
                    isPDF.value = type.id == "是"
                },
                types = arrayOf(
                    OutputType(
                        name = getString(R.string.print_output_pdf),
                        id = "是",
                        index = 0,
                    ),
                    OutputType(
                        name = getString(R.string.print_output_hotmelt),
                        id = "否",
                        index = 1,
                    ),
                )
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card (
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable {
                        viewModel.updateLoadingData(
                            LoadingData(
                                text = getString(R.string.print_is_setting),
                                duration = 800,
                            )
                        )

                        viewModel.updatePdfHospitalName(pdfHospitalName)
                        viewModel.updatePdfReportName(pdfReportName)
                        viewModel.updateIsPDF(isPDF.value)
                        // 将打印设置存储到用户偏好中
                        localStorageKit.saveUserPrintSettingToPreferences(
                            context = context,
                            hospitalName = pdfHospitalName,
                            reportName = pdfReportName,
                            isPDF = isPDF.value,
                        )

                        viewModel.updateToastData(
                            ToastData(
                                text = getString(R.string.print_setting_success),
                                showMask = false,
                                duration = 600,
                            )
                        )
                    }
                ) {
                    Text(
                        text = getString(R.string.print_save),
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
}