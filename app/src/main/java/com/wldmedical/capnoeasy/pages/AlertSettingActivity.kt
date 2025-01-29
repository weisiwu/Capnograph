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
 * 设置二级页 - 报警
 */
class AlertSettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
                RangeSelector(
                    title = "ETCO2 范围",
                    type = RangeType.BOTH,
                    startValue = 2.5f,
                    endValue = 10f,
                    valueRange = 0.3f..30f,
                )

                RangeSelector(
                    title = "ETCO2 范围",
                    type = RangeType.BOTH,
                    startValue = 2.5f,
                    endValue = 10f,
                    valueRange = 0.3f..30f,
                )
            }
        }
    }
}