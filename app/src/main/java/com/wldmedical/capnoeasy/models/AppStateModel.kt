package com.wldmedical.capnoeasy.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wldmedical.capnoeasy.components.NavBarComponentState
import com.wldmedical.capnoeasy.components.PageScene
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * app运行时状态
 */

@Singleton
class AppState @Inject constructor() {
    // 当前所在页面
    val _currentPage = mutableStateOf(NavBarComponentState(currentPage = PageScene.HOME_PAGE))

    // 当前激活tab index
    val _currentTab = mutableIntStateOf(1)
}

@HiltViewModel
class AppStateModel @Inject constructor(private val appState: AppState): ViewModel() {
    // 当前所在页面
    val currentPage: State<NavBarComponentState> = appState._currentPage
    fun updateCurrentPage(newVal: PageScene) {
        appState._currentPage.value = NavBarComponentState(newVal)
    }

    // 当前激活tab index
    val currentTab: State<Int> = appState._currentTab
    fun updateCurrentTab(newVal: Int) {
        appState._currentTab.intValue = newVal
    }
}
