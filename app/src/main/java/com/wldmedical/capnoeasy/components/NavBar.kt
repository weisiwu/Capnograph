package com.wldmedical.capnoeasy.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString
import com.wldmedical.capnoeasy.models.AppState
import com.wldmedical.capnoeasy.models.AppStateModel

data class NavBarComponentState(
    val currentPage: PageScene = PageScene.HOME_PAGE
)

/**
 * App顶部导航条
 * 所有一级页和二级页使用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    viewModel: AppStateModel,
    onRightClick: (() -> Unit)? = null
) {
    val context: Context = LocalContext.current
    val currentPage = viewModel.currentPage
    val rightImage: MutableState<Int?> = remember { mutableStateOf(null) }
    val rightDesc: MutableState<String> = remember { mutableStateOf("") }

    when(currentPage.value.currentPage) {
        PageScene.HISTORY_DETAIL_PAGE -> {
            rightImage.value = R.drawable.pull_up
            if (viewModel.isPDF.value) {
                rightDesc.value = getString(R.string.actionmodal_export_pdf, context)
            } else {
                rightDesc.value = getString(R.string.actionmodal_print_ticket, context)
            }
        }
        PageScene.HOME_PAGE ->
            if (viewModel.isRecording.value) {
                rightImage.value = R.drawable.nav_print_stop_btn
                rightDesc.value = getString(R.string.navbar_recording, context)
            } else {
                rightImage.value = R.drawable.nav_print_btn
                rightDesc.value = getString(R.string.navbar_start_record, context)
            }
        else -> {
            rightImage.value = null
            rightDesc.value = ""
            println("No Use")
        }
    }

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(rightImage.value != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = rightImage.value!!),
                            contentDescription = rightDesc.value,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    onRightClick?.invoke()
                                }
                        )
                        Text(
                            text = rightDesc.value,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .clickable {
                                    onRightClick?.invoke()
                                }
                        )
                    }
                }
            }
        },
        modifier = Modifier.padding(bottom = 60.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    CapnoEasyTheme {
        Column {
            NavBar(
                viewModel = AppStateModel(
                    AppState()
                )
            )
        }
    }
}