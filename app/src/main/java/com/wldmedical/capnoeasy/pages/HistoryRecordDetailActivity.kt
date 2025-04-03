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

    @SuppressLint("RememberReturnType")
    @Composable
    override fun Content() {
        val context = this
        val recordId = UUID.fromString(intent.getStringExtra(recordIdParams))
        val totalLen = 100f
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
                withContext(Dispatchers.IO) {
                    localStorageKit.database.recordDao().queryRecordById(recordId)
                }?.let { record ->
                    // 为导出做准备
                    if (record.pdfFilePath != null) {
                        if (record.pdfFilePath!!.isNotEmpty()) {
                            context.sourceFilePath = record.pdfFilePath!!
                            context.saveFileName = "${record.patientIndex}_${record.dateIndex}"
                            context.currentRecord = record
                        }
                    }

                    // 根据当前进度条所在的百分比位置，决定当前读取第几chunk的数据
                    // 读取指定记录下的co2Data数据
                    // 初始化的时候读取第一、第二chunk的数据。如果不是第一chunk，则需要读取前后加一起一共三chunk的数据
                    withContext(Dispatchers.IO) {
                        // 计算record下有多少个chunk
                        chunkNumber.value = localStorageKit.database.co2DataDao().getCO2DataCountByRecordId(recordId)
                        println("wswTest 当前记录下一共多少数据帧 ${chunkNumber.value}")
                        // 计算record下有多少个数据点
                        totalPointsNumber.value = chunkNumber.value - 1 * maxRecordDataChunkSize
                        println("wswTest 当前记录一共多少数据点P1 ${totalPointsNumber.value}")
                        localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkNumber.value - 1)?.data?.let {
                            // 加上尾chunk数据点数量
                            totalPointsNumber.value += it.size
                            println("wswTest 当前记录一共多少数据点P2 ${totalPointsNumber.value} __ ${it.size}")
                        }
                        // 先行清空当前数据，然后添加，防止数据过多，爆内存
//                        co2Data.clear()
//                        if (chunkIndex.value >= 1) {
//                            localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value - 1)?.let {
//                                println("wswTest 添加第一chunk")
//                                co2Data.addAll(listOf(it))
//                            }
//                        }
//                        localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value)?.let {
//                            println("wswTest 添加第二chunk")
//                            co2Data.addAll(listOf(it))
//                        }
//                        localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value + 1)?.let {
//                            println("wswTest 添加第三chunk")
//                            co2Data.addAll(listOf(it))
//                        }
                    }
                }
            }
        }

        // 滑动修改进度值时触发，根据新进度，计算展示用的数据
        LaunchedEffect(startValue.value) {
            println("wswTest 记录id ${recordId} 进度发生变化===> ${startValue.value}")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    localStorageKit.database.recordDao().queryRecordById(recordId)
                }?.let { record ->
                    val percent = chunkNumber.value * startValue.value
                    val minus = percent % 1
                    chunkIndex.value = ceil(percent).toInt()
                    println("wswTest 当前的数据帧序号是 ${chunkIndex.value}")
                    val safeStartIndex = ((if(chunkIndex.value == 0) 0 else maxRecordDataChunkSize) + minus * maxRecordDataChunkSize).toInt().coerceAtLeast(0)
                    println("wswTest 当前进度的起始点是 $safeStartIndex")
                    val safeEndIndex = (safeStartIndex + maxXPoints).coerceAtMost(co2Data.size)
                    println("wswTest 当前进度的结束点是 $safeEndIndex")

                    // TODO: 待验证，是否会触发改动
                    // 动态调整chunkIndex和数据
                    // 先行清空当前数据，然后添加，防止数据过多，爆内存
                    co2Data.clear()
                    if (chunkIndex.value >= 1) {
                        localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value - 1)?.let {
                            println("wswTest 添加第一chunk")
                            co2Data.addAll(it.data.decompressToCO2WavePointData())
                        }
                    }
                    localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value)?.let {
                        println("wswTest 添加第二chunk")
                        co2Data.addAll(it.data.decompressToCO2WavePointData())
                    }
                    localStorageKit.database.co2DataDao().getCO2DataByRecordIdAndChunkIndex(recordId, chunkIndex.value + 1)?.let {
                        println("wswTest 添加第三chunk")
                        co2Data.addAll(it.data.decompressToCO2WavePointData())
                    }

                    // 生成波形数据图
                    val newEntries = mutableListOf<Entry>()
                    var sequentialIndex = 0
                    val dataToUse = if (safeStartIndex < safeEndIndex) {
                        co2Data.slice(safeStartIndex until safeEndIndex)
                    } else {
                        emptyList()
                    }
                    dataToUse.forEach {
                        newEntries.add(Entry(sequentialIndex.toFloat(), it.co2))
                        sequentialIndex++
                    }

                    // 生成趋势数据，所有数据在存储前，就生成好趋势快照
                    // 根据chunk数，来决定如何凑齐不少于100个点
                    val newTrendEntries = mutableListOf<Entry>()
                    val everyChunkEtoc2Number = 100 / chunkNumber.value
                    println("wswTest 每个chunk需要提供多少个ETCO2点来绘制趋势图----> $everyChunkEtoc2Number")
                    var sequentialTrendIndex = 0
                    // 将所有的chunk依次读出来，按照一定间隔获取数据点组装为趋势数据展示
                    localStorageKit.database.co2DataDao().getTrendDataByRecordId(recordId)
                        .let { trendDataStrList ->
                            val trendDataList = trendDataStrList.joinToString("_").split("_").map { it.toFloat() }
                            val interval = floor(trendDataList.size / 100f).toInt()
                            for (i in 0 until trendDataList.size step interval) {
                                newTrendEntries.add(
                                    Entry(sequentialTrendIndex.toFloat(), trendDataList[i])
                                )
                                println("wswTest 添加趋势图点数据: ${trendDataList[i]}")
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