package com.wldmedical.capnoeasy.components

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.wldmedical.capnoeasy.pages.MainActivity
import com.wldmedical.capnoeasy.pages.SearchActivity
import com.wldmedical.capnoeasy.pages.SettingActivity

/**
 * App 页面基础容器
 */
@Composable
fun BasePage(
    context: ComponentActivity,
    onNavBarRightClick: (() -> Unit)? = null,
    onTabClick: ((index: Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val homepage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.HOME_PAGE)) }
    val mid = remember { mutableStateOf(1) }

    Column {
        NavBar(
            state = homepage,
            onRightClick = {
                onNavBarRightClick?.invoke()
            }
        )

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                content()
            }
        }

        ActionBar(
            selectedIndex = mid,
            onTabClick = { index ->
                var intent = Intent(context, MainActivity::class.java)
                when(index) {
                    0 -> intent = Intent(context, SearchActivity::class.java)
                    1 -> intent = Intent(context, MainActivity::class.java)
                    2 -> intent = Intent(context, SettingActivity::class.java)
                }
                context.startActivity(intent)
                // TODO: 这里支持外部调用覆盖
//                onTabClick?.invoke(index)
            },
            isInPreview = true
        )
    }
}