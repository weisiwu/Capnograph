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
import com.wldmedical.hotmeltprint.PrintSetting

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
        val showTrendingChart = remember { mutableStateOf(viewModel.showTrendingChart.value) }
        val pdfTemplateMode = remember { mutableStateOf(viewModel.pdfTemplateMode.value) }
        val pdfWatermarkEnabled = remember { mutableStateOf(viewModel.pdfWatermarkEnabled.value) }
        val pdfWatermarkText = remember { mutableStateOf(viewModel.pdfWatermarkText.value) }
        val pdfWatermarkOpacity = remember {
            mutableStateOf(formatWatermarkOpacity(viewModel.pdfWatermarkOpacity.value))
        }
        val pdfEventContextSeconds = remember {
            mutableStateOf(viewModel.pdfEventContextSeconds.value.toString())
        }

        val context = this

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()) // 使用 verticalScroll
        ) {
            // 医院名称
            CustomTextField(
                title = getStringAcitivity(R.string.print_pdf_hospital_name),
                defaultText = getStringAcitivity(R.string.print_input_name),
                value = pdfHospitalName,
                onValueChange = {
                    pdfHospitalName = it
                }
            )
            // 报告名称
            CustomTextField(
                title = getStringAcitivity(R.string.print_pdf_report_name),
                defaultText = getStringAcitivity(R.string.print_input_name),
                value = pdfReportName,
                onValueChange = {
                    pdfReportName = it
                }
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // PDF报告模板
            TypeSwitch(
                selectedIndex = if (pdfTemplateMode.value == PrintSetting.PDF_TEMPLATE_DEBUG) 1 else 0,
                onTypeClick = { type ->
                    pdfTemplateMode.value = type.id
                    pdfWatermarkEnabled.value = type.id == PrintSetting.PDF_TEMPLATE_DEBUG
                },
                types = arrayOf(
                    OutputType(
                        name = getStringAcitivity(R.string.print_pdf_template_official),
                        id = PrintSetting.PDF_TEMPLATE_OFFICIAL,
                        index = 0,
                    ),
                    OutputType(
                        name = getStringAcitivity(R.string.print_pdf_template_debug),
                        id = PrintSetting.PDF_TEMPLATE_DEBUG,
                        index = 1,
                    ),
                )
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // PDF水印开关
            TypeSwitch(
                selectedIndex = if (pdfWatermarkEnabled.value) 0 else 1,
                onTypeClick = { type ->
                    pdfWatermarkEnabled.value = type.id == "是"
                },
                types = arrayOf(
                    OutputType(
                        name = getStringAcitivity(R.string.print_pdf_watermark_enable),
                        id = "是",
                        index = 0,
                    ),
                    OutputType(
                        name = getStringAcitivity(R.string.print_pdf_watermark_disable),
                        id = "否",
                        index = 1,
                    ),
                )
            )

            CustomTextField(
                title = getStringAcitivity(R.string.print_pdf_watermark_text),
                defaultText = PrintSetting.DEFAULT_PDF_WATERMARK_TEXT,
                value = pdfWatermarkText.value,
                onValueChange = {
                    pdfWatermarkText.value = it
                }
            )

            CustomTextField(
                title = getStringAcitivity(R.string.print_pdf_watermark_opacity),
                defaultText = getStringAcitivity(R.string.print_input_watermark_opacity),
                value = pdfWatermarkOpacity.value,
                onValueChange = {
                    pdfWatermarkOpacity.value = it
                }
            )

            CustomTextField(
                title = getStringAcitivity(R.string.print_pdf_event_context_seconds),
                defaultText = getStringAcitivity(R.string.print_input_event_context_seconds),
                value = pdfEventContextSeconds.value,
                onValueChange = {
                    pdfEventContextSeconds.value = it
                }
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // 输出类型: 保存PDF或者热熔打印
            TypeSwitch(
                selectedIndex = if (isPDF.value) 0 else 1,
                onTypeClick = { type ->
                    isPDF.value = type.id == "是"
                },
                types = arrayOf(
                    OutputType(
                        name = getStringAcitivity(R.string.print_output_pdf),
                        id = "是",
                        index = 0,
                    ),
                    OutputType(
                        name = getStringAcitivity(R.string.print_output_hotmelt),
                        id = "否",
                        index = 1,
                    ),
                )
            )

            Spacer(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // 是否展示趋势图？
            TypeSwitch(
                selectedIndex = if (showTrendingChart.value) 0 else 1,
                onTypeClick = { type ->
                    showTrendingChart.value = type.id == "是"
                },
                types = arrayOf(
                    OutputType(
                        name = getStringAcitivity(R.string.print_show_trend_yes),
                        id = "是",
                        index = 0,
                    ),
                    OutputType(
                        name = getStringAcitivity(R.string.print_show_trend_no),
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
                                text = getStringAcitivity(R.string.print_is_setting),
                                duration = 800,
                            )
                        )

                        viewModel.updatePdfHospitalName(pdfHospitalName)
                        viewModel.updatePdfReportName(pdfReportName)
                        viewModel.updateIsPDF(isPDF.value)
                        viewModel.updateShowTrendingChart(showTrendingChart.value)
                        val normalizedWatermarkOpacity = parseWatermarkOpacity(pdfWatermarkOpacity.value)
                        val normalizedEventContextSeconds =
                            parseEventContextSeconds(pdfEventContextSeconds.value)
                        viewModel.updatePdfTemplateMode(pdfTemplateMode.value)
                        viewModel.updatePdfWatermarkEnabled(pdfWatermarkEnabled.value)
                        viewModel.updatePdfWatermarkText(pdfWatermarkText.value)
                        viewModel.updatePdfWatermarkOpacity(normalizedWatermarkOpacity)
                        viewModel.updatePdfEventContextSeconds(normalizedEventContextSeconds)
                        // 将打印设置存储到用户偏好中
                        localStorageKit.saveUserPrintSettingToPreferences(
                            context = context,
                            hospitalName = pdfHospitalName,
                            reportName = pdfReportName,
                            isPDF = isPDF.value,
                            pdfTemplateMode = pdfTemplateMode.value,
                            pdfWatermarkEnabled = pdfWatermarkEnabled.value,
                            pdfWatermarkText = pdfWatermarkText.value,
                            pdfWatermarkOpacity = normalizedWatermarkOpacity,
                            pdfEventContextSeconds = normalizedEventContextSeconds,
                            showTrendingChart = showTrendingChart.value
                        )

                        viewModel.updateToastData(
                            ToastData(
                                text = getStringAcitivity(R.string.print_setting_success),
                                showMask = false,
                                duration = 600,
                            )
                        )
                    }
                ) {
                    Text(
                        text = getStringAcitivity(R.string.print_save),
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

    private fun parseWatermarkOpacity(value: String): Float {
        return value.toFloatOrNull()
            ?.coerceIn(0f, 1f)
            ?: PrintSetting.DEFAULT_PDF_WATERMARK_OPACITY
    }

    private fun formatWatermarkOpacity(value: Float): String {
        return value.coerceIn(0f, 1f).toString()
    }

    private fun parseEventContextSeconds(value: String): Int {
        return value.toIntOrNull()
            ?.coerceIn(
                PrintSetting.MIN_PDF_EVENT_CONTEXT_SECONDS,
                PrintSetting.MAX_PDF_EVENT_CONTEXT_SECONDS
            )
            ?: PrintSetting.DEFAULT_PDF_EVENT_CONTEXT_SECONDS
    }
}
