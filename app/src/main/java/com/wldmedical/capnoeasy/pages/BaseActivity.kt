package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.BaseLayout
import com.wldmedical.capnoeasy.models.AppStateModel
import dagger.hilt.android.AndroidEntryPoint

/***
 * 所有页面基类
 */
@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {
    // 通用加载中
    @Composable
    open fun showLoading() {
    }

    // 通用alert提示
    @Composable
    open fun showAlert() {
    }

    // 通用Toast提示
    @Composable
    open fun showToast() {}

    @Composable
    open fun Content(viewModel: AppStateModel) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayout(
                context = this,
            ) { viewModel ->
                Content(viewModel)

//                showToast()
//
//                showAlert()
//
//                showToast()
//
//                showLoading()
            }
        }
    }
}