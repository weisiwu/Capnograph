package com.wldmedical.capnoeasy.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.CustomTextField
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.SupportQRCodeTypes
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.components.TypeSwitch
import com.wldmedical.capnoeasy.kits.logoImgName
import com.wldmedical.capnoeasy.kits.saveImageToInternalStorage
import java.io.File

/***
 * 设置二级页 - 打印
 */
class PrintSettingActivity : BaseActivity() {
    override var pageScene = PageScene.PRINT_CONFIG_PAGE

    var pdfHospitalName: String = ""
    var pdfDepart: String = ""
    var pdfBedNumber: String = ""
    var pdfIDNumber: String = ""
    var printAddress: String = ""
    var printPhone: String = ""
    var printUrl: String = ""
    var printLogo: Uri? = null

    @Composable
    override fun Content() {
        pdfHospitalName = viewModel.pdfHospitalName.value
        pdfDepart = viewModel.pdfDepart.value
        pdfBedNumber = viewModel.pdfBedNumber.value
        pdfIDNumber = viewModel.pdfIDNumber.value
        
        printAddress = viewModel.printAddress.value
        printPhone = viewModel.printPhone.value
        printUrl = viewModel.printUrl.value
        printLogo = viewModel.printLogo.value
        val printUrlQRCode = remember { mutableStateOf(viewModel.printUrlQRCode.value) }
        val selectedImageUri = remember { mutableStateOf<Uri?>(viewModel.printLogo.value) }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri.value = result.data?.data
            }
        }
        val context = this

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()) // 使用 verticalScroll
        ) {
            CustomTextField(
                title = getString(R.string.print_pdf_bed_number),
                defaultText = getString(R.string.print_input_name),
                value = pdfHospitalName,
                onValueChange = {
                    pdfHospitalName = it
                }
            )
            CustomTextField(
                title = getString(R.string.print_pdf_depart),
                defaultText = getString(R.string.print_input_depart),
                value = pdfDepart,
                onValueChange = {
                    pdfDepart = it
                }
            )
            CustomTextField(
                title = getString(R.string.print_pdf_bed_number),
                defaultText = getString(R.string.print_input_bed_number),
                value = pdfBedNumber,
                onValueChange = {
                    pdfBedNumber = it
                }
            )
            CustomTextField(
                title = getString(R.string.print_pdf_id),
                defaultText = getString(R.string.print_input_id),
                value = pdfIDNumber,
                onValueChange = {
                    pdfIDNumber = it
                }
            )
            Spacer(
                modifier = Modifier.weight(1f).padding(bottom = 16.dp)
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
                        var success: Boolean = true
                        // 保存图片到本地
                        try {
                            if (selectedImageUri.value != null) {
                                success = saveImageToInternalStorage(context, selectedImageUri.value!!, logoImgName)
                            }
                        } catch (e: Exception) {
                            success = false
                        }
                        if (success) {
                            val directory = context.getDir("images", Context.MODE_PRIVATE)
                            val file = File(directory, logoImgName)
                            val imagePath = file.absolutePath

                            viewModel.updatePdfHospitalName(pdfHospitalName)
                            viewModel.updatePdfDepart(pdfDepart)
                            viewModel.updatePdfBedNumber(pdfBedNumber)
                            viewModel.updatePdfIDNumber(pdfIDNumber)

                            viewModel.updatePrintAddress(printAddress)
                            viewModel.updatePrintPhone(printPhone)
                            viewModel.updatePrintUrl(printUrl)
                            viewModel.updatePrintUrlQRCode(printUrlQRCode.value)
                            viewModel.updatePrintLogo(file.toUri())
                            // 将打印设置存储到用户偏好中
                            localStorageKit.saveUserPrintSettingToPreferences(
                                context = context,
                                macAddress = printPhone,
                                printPhone = printPhone,
                                printAddress = printAddress,
                                printUrl = printUrl,
                                printLogo = file.toUri(),
                                printUrlQRCode = printUrlQRCode.value,
                            )
                            // PDF偏好存储到用户偏好中
                            localStorageKit.saveUserPDFSettingToPreferences(
                                context = context,
                                pdfHospitalName = pdfHospitalName,
                                pdfDepart = pdfDepart,
                                pdfBedNumber = pdfBedNumber,
                                pdfIDNumber = pdfIDNumber,
                            )

                            viewModel.updateToastData(
                                ToastData(
                                    text = getString(R.string.print_setting_success),
                                    showMask = false,
                                    duration = 600,
                                )
                            )
                        } else {
                            viewModel.updateToastData(
                                ToastData(
                                    text = getString(R.string.print_setting_fail),
                                    type = ToastType.FAIL,
                                    showMask = false,
                                    duration = 600,
                                )
                            )
                        }
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