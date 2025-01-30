package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.maskOpacity
import com.wldmedical.capnoeasy.maxMaskZIndex

data class AlertData(
    val title: String? = null,
    val text: String? = null,
    val ok_btn_text: String? = null,
    val cancel_btn_text: String? = null
)

/**
 * App 全剧报警弹框，底部有两个按钮，可选择样式
 * 所有一级页和二级页使用
 */
@Composable
fun AlertModal(
    data: AlertData?,
    onOk: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    if (data == null) {
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(maxMaskZIndex)
            .background(Color.Black.copy(alpha = maskOpacity))
            .clickable {
                onCancel?.invoke()
            },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Dialog(onDismissRequest = {
                    onCancel?.invoke()
                }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column {
                            if (data.title != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        text = data.title,
                                    )
                                }
                            }
                            if (data.text != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = data.text,
                                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp, bottom = 20.dp),
                                        fontSize = 17.sp,
                                        color = Color(0xff1D2129)
                                    )
                                }
                            }
                            if (data.ok_btn_text != null && data.cancel_btn_text != null) {
                                Row(
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .border(width = 1.dp, color = Color(0x224E5969), shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 2.dp))
                                            .clickable {
                                                onCancel?.invoke()
                                            },
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = data.cancel_btn_text,
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontSize = 16.sp,
                                            color = Color(0xff4E5969)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp).
                                            border(width = 1.dp, color = Color(0x224E5969), shape = RoundedCornerShape(0.dp, 0.dp, 2.dp, 0.dp))
                                            .clickable {
                                                onOk?.invoke()
                                            },
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = data.ok_btn_text,
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontSize = 16.sp,
                                            color = Color(0xff165DFF)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlertModalPreview() {
    CapnoEasyTheme {
        Column {
            AlertModal(
                AlertData(
                    text = "确认要链接此设备？",
                    ok_btn_text = "链接",
                    cancel_btn_text = "取消",
                )
            )
        }
    }
}