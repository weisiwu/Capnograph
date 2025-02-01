package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.SystemInoList
import com.wldmedical.capnoeasy.components.systeminfos

/***
 * 设置二级页 - 系统
 */
class SystemSettingActivity : BaseActivity() {
    override val pageScene = PageScene.SYSTEM_CONFIG_PAGE

    @Composable
    override fun Content() {
        SystemInoList(systeminfos = systeminfos)
    }
}