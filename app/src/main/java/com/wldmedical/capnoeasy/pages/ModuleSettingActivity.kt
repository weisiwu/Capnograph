package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType

/***
 * 设置二级页 - 模块
 */
class ModuleSettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
                RangeSelector(
                    title = "大气压(mmHg)",
                    value = 12.3f,
                    type = RangeType.ONESIDE,
                    valueRange = 0.3f..30f,
                )

                RangeSelector(
                    title = "大气压(mmHg)",
                    value = 12.3f,
                    type = RangeType.ONESIDE,
                    valueRange = 0.3f..30f,
                )
            }
        }
    }
}