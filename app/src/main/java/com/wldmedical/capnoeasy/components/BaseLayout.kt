package com.wldmedical.capnoeasy.components

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    val options = ActivityOptionsCompat.makeCustomAnimation(context, 0, 0)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Column {
        NavBar(
            viewModel = viewModel,
            onLeftClick = {
                val intent = Intent()
                intent.putExtra("result", "back")
                context.setResult(RESULT_OK, intent)
                context.finish()
            },
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
                when(index) {
                    0 -> intent = Intent(context, SearchActivity::class.java)
                    1 -> intent = Intent(context, MainActivity::class.java)
                    2 -> intent = Intent(context, SettingActivity::class.java)
                }
                launcher.launch(intent, options)
                onTabClick?.invoke(index)
            },
            isInPreview = true
        )
    }
    float(viewModel)
}