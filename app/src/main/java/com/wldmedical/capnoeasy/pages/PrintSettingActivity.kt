package com.wldmedical.capnoeasy.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import com.wldmedical.capnoeasy.PageScene
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
    override val pageScene = PageScene.PRINT_CONFIG_PAGE

    var printAddress: String = ""
    var printPhone: String = ""
    var printUrl: String = ""
    var printLogo: String = ""

    @Composable
    override fun Content() {
        printAddress = viewModel.printAddress.value
        printPhone = viewModel.printPhone.value
        printUrl = viewModel.printUrl.value
        printLogo = viewModel.printLogo.value
        val printUrlQRCode = remember { mutableStateOf(viewModel.printUrlQRCode.value) }
        val selectedImageUri = remember { mutableStateOf<Uri?>(Uri.fromFile(File(viewModel.printLogo.value))) }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri.value = result.data?.data
            }
        }
        val context = this

        Column {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomTextField(
                    title = "地址",
                    defaultText = "请输入地址",
                    value = printAddress,
                    onValueChange = {
                        printAddress = it
                    }
                )
                CustomTextField(
                    title = "电话",
                    defaultText = "请输入电话",
                    value = printPhone,
                    onValueChange = {
                        printPhone = it
                    }
                )
                if (printUrlQRCode.value) {
                    CustomTextField(
                        title = "网址",
                        defaultText = "请输入网址",
                        value = printUrl,
                        onValueChange = {
                            printUrl = it
                        }
                    )
                }
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
                            selectedIndex = if (printUrlQRCode.value) 0 else 1,
                            onTypeClick = { type ->
                                printUrlQRCode.value = type.id == "是"
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
                                val intent = Intent(Intent.ACTION_GET_CONTENT)
                                intent.type = "image/*"
                                launcher.launch(intent)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "选择图片",
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    // 显示选择的图片
                    if (selectedImageUri.value != null) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                modifier = Modifier.height(150.dp),
                                painter = rememberAsyncImagePainter(model = selectedImageUri.value),
                                contentDescription = "选择的图片"
                            )
                        }
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
                            viewModel.updateLoadingData(
                                LoadingData(
                                    text = "正在设置",
                                    duration = 800,
                                )
                            )
                            // 保存图片到本地
                            val success = saveImageToInternalStorage(context, selectedImageUri.value!!, logoImgName)
                            if (success) {
                                val directory = context.getDir("images", Context.MODE_PRIVATE)
                                val file = File(directory, logoImgName)
                                val imagePath = file.absolutePath
                                println("wswTest 图片的绝对地址是===> ${imagePath}")

                                viewModel.updatePrintAddress(printAddress)
                                viewModel.updatePrintPhone(printPhone)
                                viewModel.updatePrintUrl(printUrl)
                                viewModel.updatePrintUrlQRCode(printUrlQRCode.value)
                                viewModel.updatePrintLogo(imagePath)
                                viewModel.updateToastData(
                                    ToastData(
                                        text = "设置成功",
                                        showMask = false,
                                        duration = 600,
                                    )
                                )
                            } else {
                                viewModel.updateToastData(
                                    ToastData(
                                        text = "设置失败",
                                        type = ToastType.FAIL,
                                        showMask = false,
                                        duration = 600,
                                    )
                                )
                            }
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
    }
}