package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.co2ScalesObj
import com.wldmedical.capnoeasy.co2UnitsObj
import com.wldmedical.capnoeasy.components.BaseLayout
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType
import com.wldmedical.capnoeasy.components.WheelPicker
import com.wldmedical.capnoeasy.wfSpeedsObj

/***
 * 所有页面基类
 */
open class BaseActivity : ComponentActivity() {
    // 通用加载中
    open fun showLoading(isLoading: Boolean) {
    }

    // 通用alert提示
    open fun showAlert(message: String?) {
    }

    // 通用Toast提示
    open fun showToast() {}

    @Composable
    open fun Content() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayout(
                context = this,
            ) {
                Content()
            }
        }
    }
}