package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.wldmedical.capnoeasy.maskOpacity
import com.wldmedical.capnoeasy.maxMaskZIndex

data class LoadingData(
    val text: String,
)

/**
 * App 全剧loading
 * 所有一级页和二级页使用
 */
@Composable
fun Loading(
    data: LoadingData? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    if (data == null) {
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(maxMaskZIndex)
            .background(Color.Black.copy(alpha = maskOpacity))
            .clickable {
                onClick?.invoke()
            },
    ) {
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier.width(36.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(
                    text = data.text,
                    modifier = Modifier.align(Alignment.CenterVertically).padding(start = 12.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    CapnoEasyTheme {
        Column {
            Loading(LoadingData(text = "搜索设备中"))
        }
    }
}