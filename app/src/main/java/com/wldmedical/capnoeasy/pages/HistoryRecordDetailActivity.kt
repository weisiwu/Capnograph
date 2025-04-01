package com.wldmedical.capnoeasy.pages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.RangeSelector
import com.wldmedical.capnoeasy.components.RangeType
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.kits.filterData
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.recordIdParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.math.max

/***
 * 历史记录详情页
 */
class HistoryRecordDetailActivity : BaseActivity() {
    override var pageScene = PageScene.HISTORY_DETAIL_PAGE

    override fun onNavBarRightClick() {
        if (viewModel.isPDF.value) {
            this.onSavePDFClick()
        } else {
            this.onPrintTicketClick()
        }
    }

    private var saveFileName: String = ""

    private var currentRecord: Record? = null

    private val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                savePdfToUri(sourceFilePath, uri)
            }
        }
    }

    private fun createPdfDocument() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "${saveFileName}.pdf") // 设置默认文件名
        }
        createDocumentLauncher.launch(intent)
    }

    private fun savePdfToUri(sourceFilePath: String, uri: Uri) {
        var outputStream: OutputStream? = null
        var inputStream: FileInputStream? = null
        try {
            outputStream = contentResolver.openOutputStream(uri)
            inputStream = FileInputStream(File(sourceFilePath))
            inputStream.copyTo(outputStream!!)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
            inputStream?.close()
        }
    }

    // 保存PDF文件
    override fun onSavePDFClick() {
        if (this.sourceFilePath.isNotEmpty()) {
            createPdfDocument()
        }
    }

    override fun onPrintTicketClick() {
        return
        // TODO: 临时注释，等数据问题解决掉 
//        val currentData = currentRecord?.data ?: return
//        if (currentData.isEmpty()) { return }
//
//        if (!blueToothKit.gpPrinterManager.getConnectState()) {
//            viewModel.updateToastData(
//                ToastData(
//                    text = getStringAcitivity(R.string.recorddetail_print_fail),
//                    showMask = false,
//                    duration = 1000,
//                )
//            )
//            return
//        }
//
//        // 对波形数据进行过滤
//        val filteredData = filterData(currentData, viewModel.CO2Scale.value.value)
//        val allPoints: List<Float> = filteredData.map {
//            it.co2
//        }
//
//        blueToothKit.gpPrinterManager.print(
//            this,
//            allPoints,
//            localStorageKit.loadPrintSettingFromPreferences(this),
//            viewModel.CO2Scale.value.value
//        )
    }

    @Composable
    override fun Content() {
        val context = this;
        val recordId = intent.getStringExtra(recordIdParams)
        val totalLen = remember { mutableStateOf(30f) }
        val startValue = remember { mutableStateOf(0f) }
        val trendChart: MutableState<LineChart?> = remember { mutableStateOf(null) }
        val chart: MutableState<LineChart?> = remember { mutableStateOf(null) }
        val entries = remember { mutableStateListOf<Entry>() }.apply {
            if (this.size < maxXPoints) {
                repeat(maxXPoints) { add(Entry(it.toFloat(), 0f)) }
            }
        }
        val trendEntries = remember { mutableStateListOf<Entry>() }
        val scrollState = rememberScrollState()

        // TODO: 展示部分全部注释掉，后续再加上
//        LaunchedEffect(recordId, startValue.value) {
//            lifecycleScope.launch {
//                val record = withContext(Dispatchers.IO) {
//                    localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
//                }
//                if (record != null) {
//                    // 为导出做准备
//                    if (record.pdfFilePath != null) {
//                        if (record.pdfFilePath!!.isNotEmpty()) {
//                            context.sourceFilePath = record.pdfFilePath!!
//                            context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
//                            context.currentRecord = record
//                        }
//                    }
//
//                    val startIndex = (startValue.value * 100).toInt()
//                    val endIndex = startIndex + maxXPoints
//                    val safeStartIndex = startIndex.coerceAtLeast(0)
//                    val safeEndIndex = endIndex.coerceAtMost(record.data.size)
//
//                    // 目前按照每秒100个点去算
//                    totalLen.value = (max(0, record.data.size - maxXPoints) / 100).toFloat()
//                    val newEntries = mutableListOf<Entry>()
//                    var sequentialIndex = 0
//                    val dataToUse = if (safeStartIndex < safeEndIndex) {
//                        record.data.slice(safeStartIndex until safeEndIndex)
//                    } else {
//                        emptyList()
//                    }
//                    dataToUse.forEach {
//                        newEntries.add(Entry(sequentialIndex.toFloat(), it.co2))
//                        sequentialIndex++
//                    }
//
//                    // 生成趋势数据
//                    val newTrendEntries = mutableListOf<Entry>()
//                    var sequentialTrendIndex = 0
//                    for (i in record.data.indices step 50) {
//                        newTrendEntries.add(Entry(sequentialTrendIndex.toFloat(), record.data[i].ETCO2))
//                        sequentialTrendIndex++
//                    }
//
//                    Snapshot.withMutableSnapshot {
//                        entries.clear()
//                        entries.addAll(newEntries)
//
//                        // 趋势图只在读取记录后更新一次
//                        if (trendEntries.size <= 0) {
//                            trendEntries.clear()
//                            trendEntries.addAll(newTrendEntries)
//                        }
//                    }
//                }
//            }
//        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Make the Column vertically scrollable
        ) {
            if (viewModel.showTrendingChart.value) {
                // 趋势图: 按照间隔获取硬件设备传回的ETCO2。并将其渲染
                // 如果整体波形数据少于30秒，不展示
                // 如果数据极多，会扩大间隔，确保趋势图的点不超过100个
                AndroidView(
                    factory = {
                        LineChart(it).apply {
                            // 设置图表大小
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                300.dp.value.toInt()
                            )

                            // 设置图表背景颜色
                            setBackgroundColor(Color.Transparent.value.toInt())

                            // 设置 X 轴位置
                            xAxis.position = XAxis.XAxisPosition.BOTTOM

                            // 禁用右侧 Y 轴
                            axisRight.isEnabled = false

                            // 隐藏 DescriptionLabel
                            description.isEnabled = false

                            // 设置 Y 轴值范围
                            axisLeft.axisMinimum = 0f
                            axisLeft.axisMaximum = viewModel.CO2Scale.value.value

                            // 设置 X 轴格式化器
                            xAxis.valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String { return "" }
                            }

                            trendChart.value = this
                        }
                    },
                    update = {
                        // 设置数据
                        val dataSet = LineDataSet(trendEntries, getStringAcitivity(R.string.chart_trending))
                        dataSet.lineWidth = 2f
                        dataSet.setDrawCircles(false) // 不绘制圆点
                        dataSet.setDrawValues(false) // 不绘制具体的值
                        val lineData = LineData(dataSet)
                        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                        trendChart.value?.data = lineData
                        it.invalidate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            // 渲染波形图
            AndroidView(
                factory = {
                    LineChart(it).apply {
                        // 设置图表大小
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            300.dp.value.toInt()
                        )

                        // 设置图表背景颜色
                        setBackgroundColor(Color.Transparent.value.toInt())

                        // 设置 X 轴位置
                        xAxis.position = XAxis.XAxisPosition.BOTTOM

                        // 禁用右侧 Y 轴
                        axisRight.isEnabled = false

                        // 隐藏 DescriptionLabel
                        description.isEnabled = false

                        // 设置 Y 轴值范围
                        axisLeft.axisMinimum = 0f
                        axisLeft.axisMaximum = viewModel.CO2Scale.value.value

                        // 设置 X 轴格式化器
                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String { return "" }
                        }

                        chart.value = this
                    }
                },
                update = {
                    // 设置数据
                    val dataSet = LineDataSet(entries, "ETCO2")
                    dataSet.lineWidth = 2f
                    dataSet.setDrawCircles(false) // 不绘制圆点
                    dataSet.setDrawValues(false) // 不绘制具体的值
                    val lineData = LineData(dataSet)
                    dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                    chart.value?.data = lineData
                    it.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            if (totalLen.value > 0f) {
                RangeSelector(
                    unit = "S",
                    value = startValue.value,
                    type = RangeType.ONESIDE,
                    valueRange = 0f..totalLen.value,
                    onValueChange = { start, end ->
                        startValue.value = start
                    }
                )
            }
        }
    }
}