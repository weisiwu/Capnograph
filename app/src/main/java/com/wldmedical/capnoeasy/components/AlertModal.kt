package com.wldmedical.capnoeasy.components

import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/**
 * App 全剧报警弹框，底部有两个按钮，可选择样式
 * 所有一级页和二级页使用
 */
@Composable
fun AlertModal(
    title: String? = null,
    text: String,
    cancel_btn_text: String? = null,
    ok_btn_text: String? = null,
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
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                AlertDialog(
                    onDismissRequest = { showAlert.value = false },
                    title = { if (title != null) Text(text = title) },
//                    title = { if (title != null) Text(text = title, modifier = Modifier.align(Alignment.CenterHorizontally)) },
                    text = {
                        if (text != null) {
                            Text(
                                text = text,
                                modifier = Modifier.weight(1f).border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
                                fontSize = 17.sp,
                                color = Color(0xff1D2129)
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    confirmButton = {
                        if (ok_btn_text != null) {
                            Text(
                                text = ok_btn_text,
                                modifier = Modifier.weight(1f).border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
                                fontSize = 16.sp,
                                color = Color(0xff165DFF)
                            )
                        }
                    },
                    dismissButton = {
                        if (cancel_btn_text != null) {
                            Text(
                                text = cancel_btn_text,
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp,
                                color = Color(0xff4E5969)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlertModalPreview() {
    CapnoEasyTheme {
        Column {
            AlertModal(
                text = "确认要链接此设备？",
                ok_btn_text = "取消",
                cancel_btn_text = "链接"
            )
//            AlertModal(
//                title = "",
//                text = "请确认是否开始记录当前数据？？",
//                ok_btn_text = "取消",
//                cancel_btn_text = "确认"
//            )
//            AlertModal(
//                title = "记录保存成功",
//                text = "保存的记录可在设置>历史记录中查看",
//                cancel_btn_text = "确认"
//            )
        }
    }
}