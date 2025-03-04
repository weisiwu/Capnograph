package com.wldmedical.capnoeasy.components

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
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
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
    onTabClick: ((index: Int) -> Boolean)? = null,
    modifier: Modifier = Modifier,
    viewModel: AppStateModel = hiltViewModel(),
    float: @Composable (AppStateModel) -> Unit,
    content: @Composable (AppStateModel) -> Unit,
) {
    val options = ActivityOptionsCompat.makeCustomAnimation(context, 0, 0)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Column {
        ActionBar(
            viewModel = viewModel,
            onTabClick = { index ->
                // 如果当前正在记录中，不允许跳转
                if (viewModel.isRecording.value) {
                    viewModel.updateAlertData(
                        AlertData(
                            text = getString(R.string.baselayout_recording_in_progress),
                            ok_btn_text = getString(R.string.baselayout_stop),
                            cancel_btn_text = getString(R.string.baselayout_think_again),
                            onOk = { onNavBarRightClick?.invoke() },
                            onCancel = { viewModel.updateAlertData(null) },
                        )
                    )
                    return@ActionBar
                }
                onTabClick?.invoke(index)
                var intent = Intent(context, MainActivity::class.java)
                when(index) {
                    0 -> intent = Intent(context, SearchActivity::class.java)
                    1 -> intent = Intent(context, MainActivity::class.java)
                    2 -> intent = Intent(context, SettingActivity::class.java)
                }
                launcher.launch(intent, options)
                context.finish()
            },
            isInPreview = true
        )

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                content(
                    viewModel
                )
            }
        }

        NavBar(
            viewModel = viewModel,
            onRightClick = {
                onNavBarRightClick?.invoke()
            }
        )

    }
    float(viewModel)
}