package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

data class TabItem(
    val text: String,
    val icon: ImageVector? = null, // 可选的图标
    val index: Int = 0,
)

val tabs = listOf(
    TabItem(text = "搜索设备", icon = Icons.Filled.Search, index = 0 ),
    TabItem(text = "主页", icon = Icons.Filled.Home, index = 1 ),
    TabItem(text = "设置", icon = Icons.Filled.Settings, index = 2 ),
)

/**
 * App底部导航条
 * 所有一级页和二级页使用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBar(
    selectedIndex: MutableState<Int>,
    onTabClick: ((index: Int) -> Unit)? = null,
    isInPreview: Boolean = false
) {
    Column(
        modifier = if (isInPreview) Modifier.fillMaxWidth() else Modifier.fillMaxSize()
    ) {
        if (!isInPreview) {
            Spacer(modifier = Modifier.weight(1f))
        }
        PrimaryTabRow(selectedTabIndex = selectedIndex.value) {
            tabs.forEachIndexed { index, tab ->
                val selected = selectedIndex.value == index
                val selectedColor = if (selected) Color(0xFF0256FF) else Color(0xff86909C)
                Tab(
                    selected = selected,
                    selectedContentColor = selectedColor,
                    onClick = {
                        selectedIndex.value = index
                        onTabClick?.invoke(index)
                    },
                    icon = { tab.icon?.let {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.text,
                        )
                    } },
                    text = {
                        Text(
                            text = tab.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionBarPreview() {
    val start = remember { mutableStateOf(0) }
    val mid = remember { mutableStateOf(1) }
    val last = remember { mutableStateOf(2) }

    CapnoEasyTheme {
        Column {
            ActionBar(
                selectedIndex = start,
                onTabClick = {},
                isInPreview = true
            )

            ActionBar(
                selectedIndex = mid,
                onTabClick = {},
                isInPreview = true
            )

            ActionBar(
                selectedIndex = last,
                isInPreview = true
            )
        }
    }
}