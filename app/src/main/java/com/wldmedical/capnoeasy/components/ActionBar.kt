package com.wldmedical.capnoeasy.components

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
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel

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
    viewModel: AppStateModel,
    onTabClick: ((index: Int) -> Unit)? = { viewModel.updateCurrentTab(it) },
    isInPreview: Boolean = false
) {
    Column(
        modifier = if (isInPreview) Modifier.fillMaxWidth() else Modifier.fillMaxSize()
    ) {
        if (!isInPreview) {
            Spacer(modifier = Modifier.weight(1f))
        }
        PrimaryTabRow(selectedTabIndex = viewModel.currentTab.value ) {
            tabs.forEachIndexed { index, tab ->
                val selected = viewModel.currentTab.value == index
                val selectedColor = if (selected) Color(0xFF0256FF) else Color(0xff86909C)
                Tab(
                    selected = selected,
                    selectedContentColor = selectedColor,
                    onClick = {
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
    CapnoEasyTheme {
        Column {
            ActionBar(
                viewModel = AppStateModel(appState = AppState()),
                onTabClick = {},
                isInPreview = true
            )
        }
    }
}