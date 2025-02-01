package com.wldmedical.capnoeasy.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.wldmedical.capnoeasy.PageScene
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
    /***
     * 相关渲染函数
     */
    // 通用加载中
    @Composable
    open fun ShowLoading() {
        val loadingData = viewModel.loadingData.value
        if (loadingData != null) {
            Loading(
                data = loadingData,
                onClick = {
                    // TODO: 这里可以细化，比如链接中的，点击也不应该消失
                    viewModel.updateLoadingData(null)
                },
                onTimeout = {
                    viewModel.updateLoadingData(null)
                }
            )
        }
    }

    // 通用alert提示
    @Composable
    open fun ShowAlert() {
        val alertData = viewModel.alertData.value
        if (alertData != null) {
            AlertModal(
                data = alertData
            )
        }
    }

    // 通用confirm提示
    @Composable
    open fun ShowConfirm() {
        val confirmData = viewModel.confirmData.value
        if (confirmData != null) {
            ConfirmModal(
                data = confirmData
            )
        }
    }

    // 通用Toast提示
    @Composable
    open fun ShowToast() {
        val toastData = viewModel.toastData.value
        if (toastData != null) {
            Toast(
                data = toastData,
                onClick = {
                    viewModel.updateToastData(null)
                },
                onTimeout = {
                    viewModel.updateToastData(null)
                }
            )
        }
    }

    @Composable
    open fun Content() {}

    /***
     * 逻辑相关函数
     */
    lateinit var viewModel: AppStateModel

    open val pageScene = PageScene.HOME_PAGE

    open fun updatePageScene() {
        this.viewModel.updateCurrentPage(this.pageScene)
    }

    open fun onNavBarRightClick() {}

    /***
     * 生命周期函数
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AppStateModel::class.java]
        enableEdgeToEdge()
        setContent {
            updatePageScene()
            BaseLayout(
                context = this,
                float = {
                    ShowToast()

                    ShowAlert()

                    ShowConfirm()

                    ShowLoading()
                },
                onNavBarRightClick = { onNavBarRightClick() }
            ) {
                Content()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            updatePageScene()
        }
    }
}