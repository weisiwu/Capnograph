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
import com.wldmedical.capnoeasy.kits.recordMaxXPoints
import com.wldmedical.capnoeasy.kits.saveChartToPdfInBackground
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.capnoeasy.recordIdParams
import com.wldmedical.hotmeltprint.PrintSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.time.Duration
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

    private var lineChart: LineChart? = null

    private val entriesCopy = mutableStateListOf<CO2WavePointData>()

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
        val recordId = intent.getStringExtra(recordIdParams)
        val context = this
        val printSetting: PrintSetting = localStorageKit.loadPrintSettingFromPreferences(this)
        lifecycleScope.launch {
            // 读取记录的数据并向指定位置生成pdf文件
            withContext(Dispatchers.IO) {
                val record = localStorageKit.database.recordDao().queryRecordById(id = UUID.fromString(recordId))

                record?.let { _record ->
                    val _recordData = _record.data
                    val _recordDataMidIndex = (_recordData.size / 2).toInt()
                    val _midRecord = _recordData.getOrNull(_recordDataMidIndex)
                    _record.pdfFilePath?.let { _pdfFilePath ->
                        lineChart?.let { _lineChart ->
                            saveChartToPdfInBackground(
                                lineChart = _lineChart,
                                data = entriesCopy,
                                filePath = _pdfFilePath,
                                record = record,
                                maxETCO2 = viewModel.CO2Scale.value.value,
                                currentETCO2 = _midRecord?.ETCO2 ?: 0f,
                                currentRR = _midRecord?.RR ?: 0,
                                printSetting = printSetting,
                                showTrendingChart = true,
                                context = context
                            )
                        }
                    }
                }
            }
        }

        if (this.sourceFilePath.isNotEmpty()) {
            createPdfDocument()
        }
    }

    override fun onPrintTicketClick() {
        val currentData = currentRecord?.data ?: return
        if (currentData.isEmpty()) { return }

        if (!blueToothKit.gpPrinterManager.getConnectState()) {
            viewModel.updateToastData(
                ToastData(
                    text = getStringAcitivity(R.string.recorddetail_print_fail),
                    showMask = false,
                    duration = 1000,
                )
            )
            return
        }

        // 对波形数据进行过滤
        val filteredData = filterData(currentData, viewModel.CO2Scale.value.value)
        val allPoints: List<Float> = filteredData.map {
            it.co2
        }

        blueToothKit.gpPrinterManager.print(
            this,
            allPoints,
            localStorageKit.loadPrintSettingFromPreferences(this),
            viewModel.CO2Scale.value.value
        )
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
            if (this.size < recordMaxXPoints) {
                repeat(recordMaxXPoints) { add(Entry(it.toFloat(), 0f)) }
            }
        }
        val trendEntries = remember { mutableStateListOf<Entry>() }
        val scrollState = rememberScrollState()

        LaunchedEffect(recordId, startValue.value) {
            lifecycleScope.launch {
                val record = withContext(Dispatchers.IO) {
                    localStorageKit.database.recordDao().queryRecordById(UUID.fromString(recordId))
                }
                if (record != null) {
                    // 为导出做准备
                    if (record.pdfFilePath != null) {
                        if (record.pdfFilePath!!.isNotEmpty()) {
                            context.sourceFilePath = record.pdfFilePath!!
                            context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
                            context.currentRecord = record
                        }
                    }

                    val startIndex = ((startValue.value / totalLen.value) * record.data.size).toInt()
                    val endIndex = startIndex + recordMaxXPoints
                    val endIndexPDF = startIndex + (recordMaxXPoints * 1.5).toInt()
                    val safeStartIndex = startIndex.coerceAtLeast(0).coerceAtMost((record.data.size - recordMaxXPoints).coerceAtLeast(0))
                    val safeEndIndex = endIndex.coerceAtMost(record.data.size)
                    val safeEndIndexPDF = endIndexPDF.coerceAtMost(record.data.size)
                    println("wswTest safeStartIndex $safeStartIndex $safeEndIndex ${safeEndIndex} ")

                    // 目前按照每秒100个点去算
                    val duration = Duration.between(record.startTime, record.endTime)
                    totalLen.value = max(0f, duration.seconds.toFloat()) // 总秒数

                    val newEntries = mutableListOf<Entry>()
                    var sequentialIndex = 0
                    val dataToUse = if (safeStartIndex < safeEndIndex) {
                        if (safeStartIndex < 0) {
                            emptyList()
                        } else {
                            record.data.slice(safeStartIndex until safeEndIndex)
                        }
                    } else {
                        emptyList()
                    }
                    println("wswTest dlldldld")
                    val dataToUsePDF = if (safeStartIndex < safeEndIndexPDF) {
                        if (safeStartIndex < 0) {
                            emptyList()
                        } else {
                            record.data.slice(safeStartIndex until safeEndIndexPDF)
                        }
                    } else {
                        emptyList()
                    }

                    entriesCopy.clear()
                    entriesCopy.addAll(dataToUsePDF.asReversed())
                    dataToUse.forEach {
                        it?.let { it1 ->
                            newEntries.add(Entry(sequentialIndex.toFloat(), it1.co2))
                            sequentialIndex++
                        }
                    }

                    // 生成趋势数据
                    val newTrendEntries = mutableListOf<Entry>()
                    var sequentialTrendIndex = 0
                    for (i in record.data.indices) {
                        record.data[i]?.let {
                            newTrendEntries.add(Entry(sequentialTrendIndex.toFloat(), it.ETCO2))
                            sequentialTrendIndex++
                        }
                    }

                    Snapshot.withMutableSnapshot {
                        entries.clear()
                        entries.addAll(newEntries)

                        // 趋势图只在读取记录后更新一次
                        if (trendEntries.size <= 0) {
                            trendEntries.clear()
                            trendEntries.addAll(newTrendEntries)
                        }
                    }
                }
            }
        }

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
                        lineChart = this
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

            if (totalLen.value > 5f) {
                RangeSelector(
                    unit = "",
                    format = { time ->
                        val partialSeconds = time.toLong() // 计算部分秒数

                        // 将部分秒数转换为小时、分钟、秒
                        val hours = partialSeconds / 3600
                        val remainingSeconds = partialSeconds % 3600
                        val minutes = remainingSeconds / 60
                        val seconds = remainingSeconds % 60

                        (if (hours > 1) "$hours:" else "") +
                        (if (minutes > 1) "$minutes:" else "") +
                        (if (seconds > 0) "$seconds" else "")
                    },
                    value = startValue.value,
                    type = RangeType.ONESIDE,
                    valueRange = 0f..totalLen.value,
                    onValueChange = { start, end ->
                        println("wswTest 最后的值是多少 start $start")
                        startValue.value = start
                    }
                )
            }
        }
    }
}