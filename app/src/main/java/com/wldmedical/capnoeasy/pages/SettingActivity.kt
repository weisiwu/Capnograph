package com.wldmedical.capnoeasy.pages

import android.content.Intent
import android.view.WindowManager
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.LoadingData
import com.wldmedical.capnoeasy.components.SettingList
import com.wldmedical.capnoeasy.components.SettingType
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.settings

/***
 * 设置一级页
 */
class SettingActivity : BaseActivity() {
    override val pageScene = PageScene.SETTING_PAGE

    @Composable
    override fun Content() {
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
                            )
                        )
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
                        viewModel.updateToastData(
                            ToastData(
                                text = "成功关机",
                                showMask = false,
                                duration = 800
                            )
                        )
                    }
                }
                if (couldJump) {
                    this.startActivity(intent)
                }
            }
        )
    }
}