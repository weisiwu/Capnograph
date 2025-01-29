package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.wldmedical.capnoeasy.components.BasePage
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme

/***
 * 主页
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasePage(
                context = this,
            ) {
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
    }
}