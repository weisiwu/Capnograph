package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

enum class PageScene(val title: String) {
    HOME_PAGE("CapnoGraph"), // 主页
    SETTING_PAGE("CapnoGraph-设置"), // 设置页
    DEVICES_LIST_PAGE("CapnoGraph-附近设备"), // 设备列表页
    SYSTEM_CONFIG_PAGE("CapnoGraph-系统设置"), // 设置页 - 系统设置
    ALERT_CONFIG_PAGE("CapnoGraph-报警参数"), // 设置页 - 报警参数
    DISPLAY_CONFIG_PAGE("CapnoGraph-显示参数"), // 设置页 - 显示参数
    MODULE_CONFIG_PAGE("CapnoGraph-模块参数"), // 设置页 - 模块参数
    PRINT_CONFIG_PAGE("CapnoGraph-打印设置"), // 设置页 - 打印设置
    HISTORY_LIST_PAGE("CapnoGraph-历史记录"), // 设置页 - 历史记录列表
    HISTORY_DETAIL_PAGE("CapnoGraph-记录详情"), // 设置页 - 历史记录详情
}

data class NavBarComponentState(
    val currentPage: PageScene = PageScene.HOME_PAGE
)

//TODO: 缺少对三点水的支持
//TODO: 缺少开始记录的支持
/**
 * App顶部导航条
 * 所有一级页和二级页使用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    state: State<NavBarComponentState>,
    onRightClick: () -> Unit
) {
    val navController = rememberNavController()
    val isFocus = remember { mutableStateOf(false) }
    val showSearch = remember { derivedStateOf { state.value.currentPage == PageScene.HISTORY_LIST_PAGE } }
    val showBack = remember { derivedStateOf { state.value.currentPage != PageScene.HOME_PAGE } }

    TopAppBar(
        navigationIcon = {
            if (showBack.value && !isFocus.value) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        title = {
            if (showSearch.value && isFocus.value) {
                SearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    enabled = true,
                    leadingIcon = { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "ArrowBack") },
                    trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search") },
                    placeholder = {
                        Text(
                            text = "请输入要搜索的病人名称或者日期",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    content = {},
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.value.currentPage.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
        actions = {
            if (showSearch.value && !isFocus.value) {
                IconButton(onClick = {
                    onRightClick()
                    isFocus.value = !isFocus.value
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    val homepage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.HOME_PAGE)) }
    val settingpage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.SETTING_PAGE)) }
    val historypage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.HISTORY_LIST_PAGE)) }

    CapnoEasyTheme {
        Column {
            NavBar(
                state = homepage,
                onRightClick = {
                    // 处理点击事件的逻辑
                    Log.d("TAG", "Button clicked")
                }
            )

            NavBar(
                state = settingpage,
                onRightClick = {
                    // 处理点击事件的逻辑
                    Log.d("TAG", "Button clicked")
                }
            )

            NavBar(
                state = historypage,
                onRightClick = {
                    // 处理点击事件的逻辑
                    Log.d("TAG", "Button clicked")
                }
            )
        }
    }
}