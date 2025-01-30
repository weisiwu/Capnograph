package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.PrintDeviceConfig

/***
 * 设置二级页 - 打印
 */
class PrintSettingActivity : BaseActivity() {
    @Composable
    override fun Content() {
        PrintDeviceConfig()
    }
}