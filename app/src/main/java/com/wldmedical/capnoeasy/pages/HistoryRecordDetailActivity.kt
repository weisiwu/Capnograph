package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
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
import com.wldmedical.capnoeasy.kits.CO2Data
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.kits.decompressToCO2WavePointData
import com.wldmedical.capnoeasy.kits.filterData
import com.wldmedical.capnoeasy.kits.maxRecordDataChunkSize
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.kits.saveChartToPdfInBackground
import com.wldmedical.capnoeasy.maxMaskZIndex
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.capnoeasy.recordIdParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.floor
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

    // 用于打印的CO2波形数据
    private var printableCo2Data: List<CO2WavePointData> = emptyList()
    private var currentLineChart: LineChart? = null
    private var currentRecordId: UUID? = null
    private var exportPdfFilePath: String = ""

    private val createDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                savePdfToUri(exportPdfFilePath, uri)
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
        val record = currentRecord
        if (record == null) {
            viewModel.updateToastData(
                ToastData(
                    text = getStringAcitivity(R.string.recorddetail_pdf_fail),
                    showMask = false,
                    duration = 1000,
                )
            )
            return
        }
        val lineChart = currentLineChart ?: LineChart(this).apply {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = viewModel.CO2Scale.value.value
        }
        val outputPath = File(cacheDir, "${saveFileName.ifEmpty { record.patientIndex }}.pdf").absolutePath
        exportPdfFilePath = outputPath
        lifecycleScope.launch {
            val allCo2Data = loadAllCo2Data(record.id)
            if (allCo2Data.isEmpty()) {
                viewModel.updateToastData(
                    ToastData(
                        text = getStringAcitivity(R.string.recorddetail_pdf_fail),
                        showMask = false,
                        duration = 1000,
                    )
                )
                return@launch
            }
            saveChartToPdfInBackground(
                lineChart = lineChart,
                data = allCo2Data,
                filePath = outputPath,
                record = record,
                maxETCO2 = viewModel.CO2Scale.value.value,
                currentETCO2 = allCo2Data.lastOrNull()?.ETCO2 ?: 0f,
                currentRR = allCo2Data.lastOrNull()?.RR ?: 0,
                printSetting = localStorageKit.loadPrintSettingFromPreferences(this@HistoryRecordDetailActivity),
                showTrendingChart = viewModel.showTrendingChart.value,
                co2Unit = viewModel.CO2Unit.value.value,
                context = this@HistoryRecordDetailActivity,
                onComplete = { success ->
                    if (success) {
                        createPdfDocument()
                    } else {
                        viewModel.updateToastData(
                            ToastData(
                                text = getStringAcitivity(R.string.recorddetail_pdf_fail),
                                showMask = false,
                                duration = 1000,
                            )
                        )
                    }
                }
            )
        }
    }

    override fun onPrintTicketClick() {
        val recordId = currentRecordId
        if (recordId == null) {
            viewModel.updateToastData(
                ToastData(
                    text = getStringAcitivity(R.string.recorddetail_record_fail),
                    showMask = false,
                    duration = 1000,
                )
            )
            return
        }
        lifecycleScope.launch {
            val allCo2Data = loadAllCo2Data(recordId)
            if (allCo2Data.isEmpty()) {
                viewModel.updateToastData(
                    ToastData(
                        text = getStringAcitivity(R.string.recorddetail_record_fail),
                        showMask = false,
                        duration = 1000,
                    )
                )
                return@launch
            }

            if (!blueToothKit.gpPrinterManager.getConnectState()) {
                viewModel.updateToastData(
                    ToastData(
                        text = getStringAcitivity(R.string.recorddetail_print_fail),
                        showMask = false,
                        duration = 1000,
                    )
                )
                return@launch
            }

            val filteredData = filterData(allCo2Data, viewModel.CO2Scale.value.value)
            val allPoints: List<Float> = filteredData.map {
                it.co2
            }

            blueToothKit.gpPrinterManager.print(
                this@HistoryRecordDetailActivity,
                allPoints,
                localStorageKit.loadPrintSettingFromPreferences(this@HistoryRecordDetailActivity),
                viewModel.CO2Scale.value.value
            )
        }
    }

    private suspend fun loadAllCo2Data(recordId: UUID): List<CO2WavePointData> {
        return withContext(Dispatchers.IO) {
            localStorageKit.database.co2DataDao()
                .getCO2DataByRecordId(recordId)
                .flatMap { it.data.decompressToCO2WavePointData() }
        }
    }

    @SuppressLint("RememberReturnType")
    @Composable
    override fun Content() {
        val context = this
        val recordId = UUID.fromString(intent.getStringExtra(recordIdParams))
        currentRecordId = recordId
        val totalLen = 100f
        val startValue = remember { mutableStateOf(1f) } // 初始值设为1（100%），显示记录的最后部分
        val trendChart: MutableState<LineChart?> = remember { mutableStateOf(null) }
        val chart: MutableState<LineChart?> = remember { mutableStateOf(null) }
        val entries = remember { mutableStateListOf<Entry>() }
        val trendEntries = remember { mutableStateListOf<Entry>() }
        val scrollState = rememberScrollState()
        // 记录关联的co2数据
        val co2Data: MutableList<CO2WavePointData> = remember { mutableListOf() }
        // 当前展示的是第几个chunk
        val chunkIndex = remember { mutableStateOf(0) }
        // 当前记录关联的chunk总数
        val chunkNumber = remember { mutableStateOf(0) }
        // 当前record下总共有多少数据点
        val totalPointsNumber = remember { mutableStateOf(0) }
        val trendData = remember { mutableListOf(0f) }

        // 初始化时触发(recordId变化，视为打开新的记录页)
        LaunchedEffect(recordId) {
            println("wswTest 记录id 开始初始化 ${recordId}")
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(recordId)
                    if (record == null) return@withContext null

                    // 计算record下有多少个chunk
                    val chunkNumberValue = localStorageKit.database.co2DataDao().getCO2DataCountByRecordId(recordId)
                    println("wswTest 当前记录下一共多少数据帧 ${chunkNumberValue}")

                    // 计算record下有多少个数据点
                    val lastChunkData = localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkNumberValue - 1)
                    val totalPointsNumberValue = if (lastChunkData != null) {
                        (chunkNumberValue - 1) * maxRecordDataChunkSize + lastChunkData.data.size
                    } else {
                        0
                    }
                    println("wswTest 当前记录一共多少数据点 ${totalPointsNumberValue}")

                    Triple(record, chunkNumberValue, totalPointsNumberValue)
                }

                // 在主线程更新UI状态
                result?.let { (record, chunkNumberValue, totalPointsNumberValue) ->
                    // 为导出做准备
                    context.currentRecord = record
                    context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
                    context.exportPdfFilePath = if (!record.pdfFilePath.isNullOrEmpty()) {
                        record.pdfFilePath!!
                    } else {
                        File(
                            context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS),
                            "${context.saveFileName}.pdf"
                        ).absolutePath
                    }

                    chunkNumber.value = chunkNumberValue
                    totalPointsNumber.value = totalPointsNumberValue
                }
            }
        }

        // 滑动修改进度值时触发，根据新进度，计算展示用的数据
        LaunchedEffect(startValue.value) {
            println("wswTest 记录id ${recordId} 进度发生变化===> ${startValue.value}")
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) {
                    val record = localStorageKit.database.recordDao().queryRecordById(recordId)
                    if (record == null) return@withContext null

                    val percent = chunkNumber.value * startValue.value
                    val minus = percent % 1
                    val currentChunkIndex = ceil(percent).toInt()
                    println("wswTest 当前的数据帧序号是 $currentChunkIndex")

                    // 从数据库读取CO2数据
                    val loadedCo2Data = mutableListOf<CO2WavePointData>()
                    if (currentChunkIndex >= 1) {
                        localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, currentChunkIndex - 1)?.let {
                            println("wswTest 添加第一chunk")
                            loadedCo2Data.addAll(it.data.decompressToCO2WavePointData())
                        }
                    }
                    localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, currentChunkIndex)?.let {
                        println("wswTest 添加第二chunk")
                        loadedCo2Data.addAll(it.data.decompressToCO2WavePointData())
                    }
                    localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, currentChunkIndex + 1)?.let {
                        println("wswTest 添加第三chunk")
                        loadedCo2Data.addAll(it.data.decompressToCO2WavePointData())
                    }

                    // 与主页保持一致：使用 maxXPoints 作为一屏的数据量
                    println("wswTest 加载的总数据量: ${loadedCo2Data.size} 个点")

                    val filteredData = if (loadedCo2Data.size > maxXPoints) {
                        println("wswTest 数据超过${maxXPoints}个点，从 ${loadedCo2Data.size} 个点中只取最后${maxXPoints}个")
                        loadedCo2Data.takeLast(maxXPoints)
                    } else {
                        println("wswTest 数据不足${maxXPoints}个点，显示全部 ${loadedCo2Data.size} 个点")
                        loadedCo2Data
                    }

                    // 生成波形数据图
                    val newEntries = mutableListOf<Entry>()
                    
                    // 将X轴缩小到4分之1，使数据更加紧凑
                    filteredData.forEach {
                        newEntries.add(Entry(it.index.toFloat(), it.co2))
                    }

                    // 生成趋势数据
                    val newTrendEntries = mutableListOf<Entry>()
                    val everyChunkEtoc2Number = if (chunkNumber.value > 0) 100 / chunkNumber.value else 100
                    println("wswTest 每个chunk需要提供多少个ETCO2点来绘制趋势图----> $everyChunkEtoc2Number")
                    var sequentialTrendIndex = 0
                    localStorageKit.database.co2DataDao().getTrendDataByRecordId(recordId)
                        .let { trendDataStrList ->
                            val trendDataList = trendDataStrList.joinToString("_").split("_").mapNotNull { it.toFloatOrNull() }
                            val interval = floor(trendDataList.size / 100f).toInt().coerceAtLeast(1)
                            for (i in 0 until trendDataList.size step interval) {
                                newTrendEntries.add(
                                    Entry(sequentialTrendIndex.toFloat(), trendDataList[i])
                                )
                                println("wswTest 添加趋势图点数据: ${trendDataList[i]}")
                                sequentialTrendIndex++
                            }
                        }

                    Triple(currentChunkIndex, Pair(filteredData, newEntries), newTrendEntries)
                }
                result?.let { (currentChunkIndex, dataPair, newTrendEntries) ->
                    val (filteredCo2Data, newEntries) = dataPair
                    chunkIndex.value = currentChunkIndex

                    // 更新打印用的数据（使用过滤后的数据）
                    context.printableCo2Data = filteredCo2Data

                    Snapshot.withMutableSnapshot {
                        co2Data.clear()
                        co2Data.addAll(filteredCo2Data)

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

                            // 禁用横向滑动和缩放，让图表自动压缩宽度占满一屏
                            trendChart.value = this
                        }
                    },
                    update = {
                        val dataSet = LineDataSet(trendEntries, "ETCO2")
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
                        currentLineChart = this
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

            if (co2Data.size > maxMaskZIndex) {
                RangeSelector(
                    unit = "S",
                    value = startValue.value,
                    type = RangeType.ONESIDE,
                    valueRange = 0f..totalLen,
                    onValueChange = { start, end ->
                        startValue.value = start
                    }
                )
            }
        }
    }
}