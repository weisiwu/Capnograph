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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

/**
 * App 全局确认弹框，底部有一个按钮
 * 所有一级页和二级页使用
 */
@Composable
fun ConfirmModal(
    title: String? = null,
    text: String? = null,
    confirm_btn_text: String? = null,
    onClick: ((Boolean) -> Boolean)? = null
) {
    var showAlert = remember { mutableStateOf(true) }

    if (!showAlert.value) {
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(999f)
            .background(Color.Black.copy(alpha = 0.2f))
            .clickable {
                var showLoadingTmp = false;

                if (onClick != null) {
                    showLoadingTmp = onClick(showAlert.value)
                }
                showAlert.value = showLoadingTmp
            },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Dialog(onDismissRequest = { showAlert.value = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column {
                            if (title != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 20.dp),
                                        text = title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            if (text != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = text,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 12.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                                        maxLines = 2,
                                        fontSize = 17.sp,
                                        color = Color(0xff333333)
                                    )
                                }
                            }
                            if (confirm_btn_text != null) {
                                Row(
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f).height(44.dp).border(width = 1.dp, color = Color(0x224E5969), shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 2.dp)),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = confirm_btn_text,
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
            title = "记录保存成功",
            text = "保存的记录可在设置>历史记录中查看",
            confirm_btn_text = "确认"
        )
    }
}