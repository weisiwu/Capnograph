package com.wldmedical.capnoeasy.components

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.maskOpacity
import com.wldmedical.capnoeasy.maxMaskZIndex
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS,
    FAIL
}

data class ToastData(
    val text: String,
    val type: ToastType = ToastType.SUCCESS,
    val showMask: Boolean = true,
    val duration: Long = 600,
    val callback:  (() -> Unit)? = null,
)

/**
 * App 全局toast
 * 所有一级页和二级页使用
 */
@Composable
fun Toast(
    data: ToastData? = null,
    onClick: (() -> Unit)? = null,
    onTimeout: (() -> Unit)? = null,
) {
    val context: Context = LocalContext.current
    val isTimeout = remember { mutableStateOf(false) }

    if (data == null) {
        return
    }

    // 超时消失
    if (isTimeout.value) {
        isTimeout.value = false
        onTimeout?.invoke()
        return
    }

    if (data.duration > 0) {
        LaunchedEffect(Unit) {
            delay(data.duration)
            data.callback?.invoke()
            isTimeout.value = true
        }
    }

    val alpha = if (data.showMask) maskOpacity else 0f

    Box(
        modifier = Modifier
            .zIndex(maxMaskZIndex)
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha))
            .clickable {
                onClick?.invoke()
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
                    when(data.type) {
                        ToastType.SUCCESS -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = getString(R.string.toast_success, context),
                                tint = Color.White
                            )
                        }
                        ToastType.FAIL -> {
                            Image(
                                painter = painterResource(id = R.drawable.fail_icon),
                                alignment = Alignment.Center,
                                contentDescription = getString(R.string.toast_fail, context)
                            )
                        }
                    }
                    Text(
                        text = data.text,
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
        Toast(
            data = ToastData(
                text = "链接失败",
                type = ToastType.FAIL,
                showMask = false
            ),
        )
    }
}