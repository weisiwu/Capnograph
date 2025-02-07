package com.wldmedical.capnoeasy.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.patientParams

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    override val pageScene = PageScene.HISTORY_DETAIL_PAGE

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Composable
    override fun Content() {
        val patient = intent.getSerializableExtra(patientParams) as Record
        println("wswTest 接收到的病人是 ${patient.patient.name}")
    }
}