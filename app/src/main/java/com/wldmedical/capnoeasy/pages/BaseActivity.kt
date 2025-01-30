package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.components.AlertModal
import com.wldmedical.capnoeasy.components.BaseLayout
import com.wldmedical.capnoeasy.components.ConfirmModal
import com.wldmedical.capnoeasy.components.Loading
import com.wldmedical.capnoeasy.components.Toast
import com.wldmedical.capnoeasy.models.AppStateModel
import dagger.hilt.android.AndroidEntryPoint

/***
 * 所有页面基类
 */
@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {
    // 通用加载中
    @Composable
    open fun ShowLoading(viewModel: AppStateModel) {
        val loadingData = viewModel.loadingData.value
        if (loadingData != null) {
            Loading(text = loadingData.text)
        }
    }

    // 通用alert提示
    @Composable
    open fun ShowAlert(viewModel: AppStateModel) {
        val alertData = viewModel.alertData.value
        if (alertData != null) {
            AlertModal(
                text = alertData.text,
                ok_btn_text = alertData.ok_btn_text,
                cancel_btn_text = alertData.cancel_btn_text
            )
        }
    }

    // 通用confirm提示
    @Composable
    open fun ShowConfirm(viewModel: AppStateModel) {
        val confirmData = viewModel.confirmData.value
        if (confirmData != null) {
            ConfirmModal(
                text = confirmData.text,
                title = confirmData.title,
                confirm_btn_text = confirmData.confirm_btn_text
            )
        }
    }

    // 通用Toast提示
    @Composable
    open fun ShowToast(viewModel: AppStateModel) {
        val toastData = viewModel.toastData.value
        if (toastData != null) {
            Toast(
                text = toastData.text,
                type = toastData.type,
                showMask = toastData.showMask
            )
        }
    }

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

                ShowToast(viewModel)

                ShowAlert(viewModel)

                ShowConfirm(viewModel)

                ShowLoading(viewModel)
            }
        }
    }
}