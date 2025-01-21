package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.R

enum class ToastType {
    SUCCESS,
    FAIL
}

/**
 * App 全局toast
 * 所有一级页和二级页使用
 */
@Composable
fun Toast(
    text: String,
    type: ToastType = ToastType.SUCCESS,
    showMask: Boolean = false,
) {
    var showToast = remember { mutableStateOf(true) }
    val alpha = if (showMask) 0.2f else 0f

    if (!showToast.value) {
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(999f)
            .background(Color.Black.copy(alpha = alpha))
            .clickable {
                showToast.value = false
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = CardColors(
                    containerColor = Color(0xCC000000),
                    contentColor = Color(0xCC000000),
                    disabledContainerColor = Color(0xCC000000),
                    disabledContentColor = Color(0xCC000000)
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when(type) {
                        ToastType.SUCCESS -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "成功",
                                tint = Color.White
                            )
                        }
                        ToastType.FAIL -> {
                            Image(
                                painter = painterResource(id = R.drawable.fail_icon),
                                alignment = Alignment.Center,
                                contentDescription = "失败"
                            )
                        }
                    }
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 6.dp),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToastPreview() {
    CapnoEasyTheme {
//        Toast(
//            text = "开始记录",
//            type = ToastType.SUCCESS,
//            showMask = true
//        )

//        Toast(
//            text = "链接失败",
//            type = ToastType.FAIL,
//            showMask = true
//        )

        Toast(
            text = "链接失败",
            type = ToastType.FAIL,
            showMask = false
        )
    }
}