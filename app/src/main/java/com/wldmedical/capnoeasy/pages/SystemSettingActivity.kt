package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.SystemInoList
import com.wldmedical.capnoeasy.components.systeminfos

/***
 * 设置二级页 - 系统
 */
class SystemSettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
                SystemInoList(systeminfos = systeminfos)
            }
        }
    }
}