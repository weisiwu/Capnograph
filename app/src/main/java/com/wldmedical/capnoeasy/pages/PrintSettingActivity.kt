package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.PrintDeviceConfig

/***
 * 设置二级页 - 打印
 */
class PrintSettingActivity : BaseActivity() {
    override val pageScene = PageScene.PRINT_CONFIG_PAGE

    @Composable
    override fun Content() {
        PrintDeviceConfig()
    }
}