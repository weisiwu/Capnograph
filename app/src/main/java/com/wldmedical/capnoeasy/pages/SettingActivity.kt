package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.SettingList
import com.wldmedical.capnoeasy.components.settings

/***
 * 设置一级页
 */
class SettingActivity : BaseActivity() {
    override val pageScene = PageScene.SETTING_PAGE

    @Composable
    override fun Content() {
        SettingList(
            context = this,
            settings = settings
        )
    }
}