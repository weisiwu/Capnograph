package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * App 历史记录页，点击右上角三点水，弹出的弹框
 * 里面有打印小票，导出pdf
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionModal(
    title: String? = null,
    text: String? = null,
    cancel_btn_text: String? = null,
    ok_btn_text: String? = null,
    onClick: ((Boolean) -> Boolean)? = null
) {
    var showBottomSheet = remember { mutableStateOf(true) }

    if (!showBottomSheet.value) {
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
                    showLoadingTmp = onClick(showBottomSheet.value)
                }
                showBottomSheet.value = showLoadingTmp
            },
    ) {
        ModalBottomSheet(
            modifier = Modifier.height(150.dp),
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            // 弹窗内容
            Text(text = "这是底部弹窗的内容1212121")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionModalPreview() {
    CapnoEasyTheme {
        Column {
            ActionModal(
                text = "确认要链接此设备？",
                ok_btn_text = "链接",
                cancel_btn_text = "取消"
            )
        }
    }
}