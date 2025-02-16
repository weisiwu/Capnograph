package com.wldmedical.capnoeasy.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.maskOpacity
import com.wldmedical.capnoeasy.maxMaskZIndex
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import kotlinx.coroutines.launch

//TODO: 这里需要做成可以通过传递图片和文字来控制展示的效果，而不是每次往里面手动写
/**
 * App 历史记录页，点击右上角三点水，弹出的弹框
 * 里面有打印小票，导出pdf
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionModal(
    viewModel: AppStateModel,
    onCancelClick: (() -> Unit)? = null,
    onSavePDFClick: (() -> Unit)? = null,
    onPrintTicketClick: (() -> Unit)? = null
) {
    //起初以为是m3的ModalBottomSheet在Preview模式下不展示
    //https://issuetracker.google.com/issues/283843380
    //按照问题描述，问题已经修复，但实际上我的机器仍然会展示（我的m3版本是正确的）
    //后续按照下面的链接进行修改，将最开始用的是rememberModalBottomSheetState，替换为rememberStandardBottomSheetState
    // https://stackoverflow.com/questions/78329804/how-to-create-preview-function-to-modalbottomsheet
    // 在preview模式就可以展示，
    // val sheetState = rememberModalBottomSheetState()
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        skipHiddenState = false
    )
    val scope = rememberCoroutineScope()
    val alpha = if (viewModel.showActionModal.value) maskOpacity else 0f

    if (!viewModel.showActionModal.value) {
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(maxMaskZIndex)
            .background(Color.Black.copy(alpha = alpha))
            .clickable {
                viewModel.updateShowActionModal(false)
            }
    ) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateShowActionModal(false)
            },
            sheetState = sheetState,
            content = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "操作",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).padding(16.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(end = 40.dp).clickable {
                            onSavePDFClick?.invoke()
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.print_pdf),
                            modifier = Modifier.height(62.dp).padding(bottom = 12.dp),
                            contentDescription = "导出PDF"
                        )
                        Text(
                            text = "导出PDF",
                            color = Color(0xFF86909C)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable {
                            onPrintTicketClick?.invoke()
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.print_ticket),
                            modifier = Modifier.height(62.dp).padding(bottom = 12.dp),
                            contentDescription = "导出PDF"
                        )
                        Text(
                            text = "打印小票",
                            color = Color(0xFF86909C)
                        )
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xAA86909C))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(54.dp).clickable {
                        onCancelClick?.invoke()
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.updateShowActionModal(false)
                            }
                        }
                    },
                ) {
                    Text(
                        text = "取消",
                        color = Color(0xFF86909C),
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActionModalPreview() {
    CapnoEasyTheme {
        ActionModal(
            viewModel = AppStateModel(AppState())
        )
    }
}