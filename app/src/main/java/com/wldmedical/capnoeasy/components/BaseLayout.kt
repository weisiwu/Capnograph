package com.wldmedical.capnoeasy.components

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityOptionsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.pages.MainActivity
import com.wldmedical.capnoeasy.pages.SearchActivity
import com.wldmedical.capnoeasy.pages.SettingActivity

/**
 * App 页面基础容器
 */
@Composable
fun BaseLayout(
    context: ComponentActivity,
    onNavBarRightClick: (() -> Unit)? = null,
    onTabClick: ((index: Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: AppStateModel = hiltViewModel(),
    float: @Composable (AppStateModel) -> Unit,
    content: @Composable (AppStateModel) -> Unit,
) {
    Column {
        NavBar(
            state = viewModel.currentPage,
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
                content(viewModel)
            }
        }

        ActionBar(
            viewModel = viewModel,
            onTabClick = { index ->
                var intent = Intent(context, MainActivity::class.java)
                viewModel.updateCurrentTab(index)
                when(index) {
                    0 -> intent = Intent(context, SearchActivity::class.java)
                    1 -> intent = Intent(context, MainActivity::class.java)
                    2 -> intent = Intent(context, SettingActivity::class.java)
                }
                context.startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(context, 0, 0).toBundle())
                onTabClick?.invoke(index)
            },
            isInPreview = true
        )
    }
    float(viewModel)
}