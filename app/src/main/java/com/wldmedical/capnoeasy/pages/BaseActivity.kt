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
import com.wldmedical.capnoeasy.components.ToastData
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
            Loading(
                data = loadingData,
                onClick = {
                    // TODO: 这里可以细化，比如链接中的，点击也不应该消失
                    viewModel.updateLoadingData(null)
                }
            )
        }
    }

    // 通用alert提示
    @Composable
    open fun ShowAlert(viewModel: AppStateModel) {
        val alertData = viewModel.alertData.value
        if (alertData != null) {
            AlertModal(
                data = alertData,
                onOk = {
                    viewModel.updateLoadingData(null)
                },
                onCancel = {
                    viewModel.updateLoadingData(null)
                }
            )
        }
    }

    // 通用confirm提示
    @Composable
    open fun ShowConfirm(viewModel: AppStateModel) {
        val confirmData = viewModel.confirmData.value
        if (confirmData != null) {
            ConfirmModal(
                data = confirmData,
                onClick = {
                    viewModel.updateConfirmData(null)
                }
            )
        }
    }

    // 通用Toast提示
    @Composable
    open fun ShowToast(viewModel: AppStateModel) {
        val toastData = viewModel.toastData.value
        if (toastData != null) {
            Toast(
                data = toastData,
                onClick = {
                    viewModel.updateToastData(null)
                }
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
                float = { viewModel ->
                    ShowToast(viewModel)

                    ShowAlert(viewModel)

                    ShowConfirm(viewModel)

                    ShowLoading(viewModel)
                }
            ) { viewModel ->
                Content(viewModel)
            }
        }
    }
}