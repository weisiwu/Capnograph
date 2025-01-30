package com.wldmedical.capnoeasy.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.models.AppStateModel
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme

/***
 * 主页
 */
class MainActivity : BaseActivity() {

    @Composable
    override fun Content(viewModel: AppStateModel) {
        viewModel.updateCurrentPage(PageScene.HOME_PAGE)
        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                lineSeries {
                    series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11)
                }
            }
        }

        CapnoEasyTheme {
            EtCo2LineChart(modelProducer)
        }

        EtCo2Table()
    }
}