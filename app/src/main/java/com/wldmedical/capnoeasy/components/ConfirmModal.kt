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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.maskOpacity
import com.wldmedical.capnoeasy.maxMaskZIndex

data class ConfirmData(
    val text: String? = null,
    val title: String? = null,
    val confirm_btn_text: String? = null
)

/**
 * App 全局确认弹框，底部有一个按钮
 * 所有一级页和二级页使用
 */
@Composable
fun ConfirmModal(
    data: ConfirmData? = null,
    onClick: (() -> Unit)? = null
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
                onClick?.invoke()
            },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Dialog(onDismissRequest = { onClick?.invoke() }) {
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
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 20.dp),
                                        text = data.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            if (data.text != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = data.text,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 12.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                                        maxLines = 2,
                                        fontSize = 17.sp,
                                        color = Color(0xff333333)
                                    )
                                }
                            }
                            if (data.confirm_btn_text != null) {
                                Row(
                                    modifier = Modifier
                                        .height(44.dp)
                                        .clickable {
                                            onClick?.invoke()
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .border(width = 1.dp, color = Color(0x224E5969), shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 2.dp)),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = data.confirm_btn_text,
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontSize = 16.sp,
                                            color = Color(0xff4E5969)
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
fun ConfirmModalPreview() {
    CapnoEasyTheme {
        ConfirmModal(
            data = ConfirmData(
                title = "记录保存成功",
                text = "保存的记录可在设置>历史记录中查看",
                confirm_btn_text = "确认"
            )
        )
    }
}