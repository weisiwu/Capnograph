package com.wldmedical.capnoeasy.pages

import android.content.Intent
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityOptionsCompat
import com.wldmedical.capnoeasy.InfinityDuration
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.SettingList
import com.wldmedical.capnoeasy.components.SettingType
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.settings

/***
 * 设置一级页
 */
// TODO: 这里的中英文需要思考如何解决
class SettingActivity : BaseActivity() {
    override val pageScene = PageScene.SETTING_PAGE

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

         SettingList(
            settings = settings,
            onSettingClick = { setting ->
                var couldJump = true
                var intent = Intent(this, SettingActivity::class.java)
                when(setting.type) {
                    SettingType.ALERT_PARAM -> intent = Intent(this, AlertSettingActivity::class.java)
                    SettingType.DISPLAY_PARAM -> intent = Intent(this, DisplaySettingActivity::class.java)
                    SettingType.MODULE_PARAM -> intent = Intent(this, ModuleSettingActivity::class.java)
                    SettingType.SYSTEM_SETTING -> intent = Intent(this, SystemSettingActivity::class.java)
                    SettingType.PRINT_SETTING -> intent = Intent(this, PrintSettingActivity::class.java)
                    SettingType.HISTORY_RECORD -> intent = Intent(this, HistoryRecordsActivity::class.java)
                    SettingType.ZERO -> {
                        couldJump = false
                        viewModel.updateLoadingData(
                            LoadingData(
                                text = "正在校零",
                                duration = InfinityDuration,
                            )
                        )
                        blueToothKit.correctZero() {
                            viewModel.clearXData()
                            viewModel.updateToastData(
                                ToastData(
                                    text = "成功校零",
                                    showMask = false,
                                    duration = 800,
                                )
                            )
                        }
                    }
                    SettingType.KEEP_LIGHT -> {
                        couldJump = false
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        viewModel.updateToastData(
                            ToastData(
                                text = "成功设置屏幕常量",
                                showMask = false,
                                duration = 800
                            )
                        )
                    }
                    SettingType.SHUTDOWN -> {
                        couldJump = false
                        viewModel.updateLoadingData(
                            LoadingData(
                                text = "正在关机",
                                duration = InfinityDuration,
                            )
                        )
                        blueToothKit.shutdown() {
                            viewModel.clearXData()
                            viewModel.updateToastData(
                                ToastData(
                                    text = "成功关机",
                                    showMask = false,
                                    duration = 800,
                                )
                            )
                        }
                    }
                }
                if (couldJump) {
                    launcher.launch(intent, options)
                }
            }
        )
    }
}