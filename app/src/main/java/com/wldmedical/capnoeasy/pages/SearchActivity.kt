package com.wldmedical.capnoeasy.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.Device
import com.wldmedical.capnoeasy.components.DeviceList
import com.wldmedical.capnoeasy.components.DeviceTypes
import com.wldmedical.capnoeasy.components.TypeSwitch

/***
 * 搜素列表
 */
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
                val devices = listOf(
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                    Device(name = "SMI-M14", mac = "D4:F0:EA:C0:93:9B"),
                )

                TypeSwitch(
                    types = DeviceTypes
                )

                DeviceList(
                    devices = devices
                )
            }
        }
    }
}