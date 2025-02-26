package com.wldmedical.capnoeasy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel

data class NavBarComponentState(
    val currentPage: PageScene = PageScene.HOME_PAGE
)

// TODO: 加返回按钮的交互
// TODO: 记录按钮的交互
/**
 * App顶部导航条
 * 所有一级页和二级页使用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    viewModel: AppStateModel,
    onLeftClick: ((NavBarComponentState) -> Unit)? = null,
    onRightClick: (() -> Unit)? = null
) {
    val currentPage = viewModel.currentPage
    val showSearch = remember { derivedStateOf { false } }
    val showBack = remember { derivedStateOf { currentPage.value.currentPage != PageScene.HOME_PAGE } }
    var rightIcon: ImageVector? = null
    val rightImage: MutableState<Int?> = remember { mutableStateOf(null) }
    val rightDesc: MutableState<String> = remember { mutableStateOf("") }

    when(currentPage.value.currentPage) {
        PageScene.HISTORY_LIST_PAGE -> {
            rightIcon = Icons.Filled.Search
            rightDesc.value = "搜索"
        }
        PageScene.HISTORY_DETAIL_PAGE -> {
            rightIcon = Icons.Filled.MoreVert
            rightDesc.value = "更多操作"
        }
        PageScene.HOME_PAGE ->
            if (viewModel.isRecording.value) {
                rightImage.value = R.drawable.nav_print_stop_btn
                rightDesc.value = "记录中"
            } else {
                rightImage.value = R.drawable.nav_print_btn
                rightDesc.value = "开始记录"
            }
        else -> {
            println("No Use")
        }
    }

    TopAppBar(
        navigationIcon = {
            if (showBack.value) {
                IconButton(onClick = {
                    onLeftClick?.invoke(currentPage.value)
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        title = {
            if (showSearch.value) {
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
                    if(rightImage.value != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = rightImage.value!!),
                                contentDescription = rightDesc.value,
                                modifier = Modifier.clickable {
                                    onRightClick?.invoke()
                                }
                            )
                            Text(
                                text = rightDesc.value,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(top = 4.dp).clickable {
                                    onRightClick?.invoke()
                                }
                            )
                        }
                    }
                }
            }
        },
        actions = {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                if (rightIcon != null) {
//                    IconButton(
//                        modifier = Modifier.padding(0.dp),
//                        onClick = {
//                            onRightClick?.invoke()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = rightIcon,
//                            contentDescription = rightDesc.value
//                        )
//                    }
//                } else if(rightImage.value != null) {
//                    Image(
//                        painter = painterResource(id = rightImage.value!!),
//                        contentDescription = rightDesc.value,
//                        modifier = Modifier.clickable {
//                            onRightClick?.invoke()
//                        }
//                    )
//                    Text(
//                        text = rightDesc.value,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Light,
//                        modifier = Modifier.padding(top = 4.dp).clickable {
//                            onRightClick?.invoke()
//                        }
//                    )
//                } else {
//                    Spacer(
//                        modifier = Modifier.width(60.dp)
//                    )
//                }
//            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    CapnoEasyTheme {
        Column {
            NavBar(
                viewModel = AppStateModel(
                    AppState()
                )
            )

//            NavBar(
//                viewModel = AppStateModel(
//                    AppState(currentPage = PageScene.HOME_PAGE)
//                )
//            )

//            NavBar(
//                viewModel = AppStateModel(
//                    AppState(currentPage = PageScene.SETTING_PAGE)
//                )
//            )
//
//            NavBar(
//                viewModel = AppStateModel(
//                    AppState(currentPage = PageScene.HISTORY_LIST_PAGE)
//                )
//            )
//
//            NavBar(
//                viewModel = AppStateModel(
//                    AppState(currentPage = PageScene.HISTORY_DETAIL_PAGE)
//                )
//            )
        }
    }
}