package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wldmedical.capnoeasy.co2ScalesObj
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.wfSpeedsObj

/***
 * 设置二级页 - 展示
 */
class DisplaySettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
                WheelPicker(
                    config = co2UnitsObj
                )

                WheelPicker(
                    config = co2ScalesObj
                )

                WheelPicker(
                    config = wfSpeedsObj
                )
            }
        }
    }
}