package com.wldmedical.capnoeasy.pages

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.Entry
import com.wldmedical.capnoeasy.CapnoEasyApplication
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.kits.BluetoothType
import com.wldmedical.capnoeasy.kits.CO2Data
import com.wldmedical.capnoeasy.kits.Patient
import com.wldmedical.capnoeasy.kits.Record
import com.wldmedical.capnoeasy.kits.compress
import com.wldmedical.capnoeasy.kits.maxXPoints
import com.wldmedical.capnoeasy.models.CO2WavePointData
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import com.wldmedical.hotmeltprint.PrintSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.UUID

/***
 * 主页
 */
class MainActivity : BaseActivity() {

    override var pageScene = PageScene.HOME_PAGE

    private  var startRecordTime: LocalDateTime? = null

    private  var endRecordTime: LocalDateTime? = null

    private  val REQUEST_CODE_MANAGE_ALL_FILES_ACCESS_PERMISSION = 1001

    override fun onTabClick(index: Int): Boolean {
        super.onTabClick(index)

       // 正在记录，且跳往非主页
       if (viewModel.isRecording.value && index != 1) {
           showAlert()
           return false
       }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        // TODO: 测试，单条记录下数据过长，写入和读取
        lifecycleScope.launch { // 使用 lifecycleScope 在后台线程执行
            try {
                val patient = Patient(
                    name = "病人A",
                    age = 90,
                    gender = GENDER.MALE,
                )
                var startTime: LocalDateTime = LocalDateTime.now()
                var endTime: LocalDateTime = LocalDateTime.now()
                val records = mutableListOf<CO2WavePointData>()
                val record = Record(
                    patient = patient,
                    startTime = startTime,
                    endTime = endTime,
                    dateIndex = 1,
                    patientIndex = "1",
                    pdfFilePath = "pdfFilePath",
                )
                withContext(Dispatchers.IO) {
                    // TODO:  测试，插入记录
                    localStorageKit.database.recordDao().insertRecord(record)
                    println("wswTest 成功插入记录，id为====》 ${record.id}")
                    val recordId = record.id
//                    // TODO: 测试，为单条记录插入数据100个chunk,合计2G内存左右
//                    for (index in 1..100) {
//                        val chunk = List(600000) { index: Int ->
                    // TODO: 测试，为单条记录插入数据100个chunk
                    for (index in 1..100) {
                        val chunk = List(6000) { index: Int ->
                            CO2WavePointData(
                                co2 = index.toFloat(),
                                RR = 0,
                                ETCO2 = 0f,
                                FiCO2 = 0f,
                                index = index
                            )
                        }
                        localStorageKit.database.co2DataDao().insertCO2Data(
                            CO2Data(
                                recordId = recordId,
                                chunkIndex = index,
                                data = chunk.compress()
                            )
                        )
                        println("wswTest 开始插入数据块，chunkIndex为====》 ${index}")
                    }
                    println("wswTest 所有测试数据均已写入完成")
                }
            } catch (e: Exception) {
                println("wswTest 报错了 ${e.message}")
            }
        }


        try {
            val deviceAddress = blueToothKit.getSavedBLEDeviceAddress(this)
            if (deviceAddress != null) {
                val device = blueToothKit.bluetoothAdapter?.getRemoteDevice(deviceAddress)
                // 尝试自动连接已经配对设备
                blueToothKit.connectDevice(device)
            }
        } catch (e: Exception) {
            println("wswTest 捕获到自动链接BLE配对设备异常: ${e.message}")
        }

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

        // 请求文件管理权限，以便对数据库文件进行备份
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    startActivityForResult(intent, REQUEST_CODE_MANAGE_ALL_FILES_ACCESS_PERMISSION )
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivityForResult(intent, REQUEST_CODE_MANAGE_ALL_FILES_ACCESS_PERMISSION )
                }
            }
        }

        try {
            // 从用户偏好里读取默认打印设置
            val printSetting: PrintSetting = localStorageKit.loadPrintSettingFromPreferences(this)

            printSetting.hospitalName?.let { viewModel.updatePdfHospitalName(it) }
            printSetting.name?.let { viewModel.updatePatientName(it) }
            printSetting.gender?.let { viewModel.updatePatientGender(
                if(it == getStringAcitivity(R.string.etco2table_male)) GENDER.MALE else GENDER.FORMALE)
            }
            printSetting.age?.let { viewModel.updatePatientAge(it) }
            printSetting.reportName?.let { viewModel.updatePdfReportName(it) }
            printSetting.isPDF.let { viewModel.updateIsPDF(it) }
            printSetting.pdfDepart?.let { viewModel.updatePatientDepartment(it) }
            printSetting.pdfBedNumber?.let { viewModel.updatePatientBedNumber(it) }
            printSetting.pdfIDNumber?.let { viewModel.updatePatientID(it) }
            printSetting.showTrendingChart?.let { viewModel.updateShowTrendingChart(it) }
        } catch (e: Exception) {
            println("wswTest 从用户偏好里读取默认打印设置异常 : ${e.message}")
            e.printStackTrace()
        }

        try {
            // 默认扫描，连接周围打印机
            blueToothKit.searchDevices(BluetoothType.CLASSIC)
        } catch (e: Exception) {
            println("wswTest 默认扫描，连接周围打印机异常 : ${e.message}")
        }
    }

    private fun showAlert() {
        if(!viewModel.isRecording.value) {
            viewModel.updateConfirmData(
                ConfirmData(
                    title = getStringAcitivity(R.string.main_record_save_success),
                    text = getStringAcitivity(R.string.main_record_save_check),
                    confirm_btn_text = getStringAcitivity(R.string.main_confirm),
                    onClick = {
                        viewModel.updateConfirmData(null)
                        viewModel.updateTotalCO2WavedData()
                    }
                )
            )
        }
    }

    override fun onNavBarRightClick() {
        val isRecording = viewModel.isRecording.value
        val context = this

        // 如果基础信息没有填写完毕，不允许录播数据
         if (
             viewModel.patientName.value == null ||
             viewModel.patientGender.value == null ||
             viewModel.patientAge.value == null ||
             viewModel.patientID.value == null ||
             viewModel.patientDepartment.value == null ||
             viewModel.patientBedNumber.value == null
         ) {
             viewModel.updateToastData(
                 ToastData(
                     text = getStringAcitivity(R.string.main_cant_record_msg),
                     duration = 2000,
                     showMask = false,
                     type = ToastType.FAIL,
                     callback = {
                         viewModel.updateToastData(null)
                     }
                 )
             )
             return
         }

        localStorageKit.saveUserPrintSettingToPreferences(
            context = context,
            name = viewModel.patientName.value,
            gender = viewModel.patientGender.value?.title,
            age = viewModel.patientAge.value,
            idNumber = viewModel.patientID.value,
            depart = viewModel.patientDepartment.value,
            bedNumber = viewModel.patientBedNumber.value,
        )

        // 正在记录中，点击为保存动作
        if (isRecording) {
            lifecycleScope.launch {
                localStorageKit.stopRecord()
            }
        } else {
            // 点击后创建记录
            startRecordTime = LocalDateTime.now()
            lifecycleScope.launch {
                val patient = Patient(
                    name = viewModel.patientName.value ?: "",
                    gender = viewModel.patientGender.value ?: GENDER.MALE,
                    age = viewModel.patientAge.value ?: 0
                )
                localStorageKit.savePatient(patient)
                // 这里只创建记录，数据在ETCO2LineChart组件里，通过监听蓝牙传回的数据流，来动态存储到数据库
                localStorageKit.saveRecord(
                    context = context,
                    patient = patient,
                    recordName = "${viewModel.pdfHospitalName.value}_${viewModel.pdfReportName.value}",
                    lineChart = viewModel.lineChart,
                    startTime = startRecordTime ?: LocalDateTime.now(),
                    endTime = endRecordTime ?: LocalDateTime.now(),
                    maxETCO2 = viewModel.CO2Scale.value.value,
                    showTrendingChart = viewModel.showTrendingChart.value,
                    currentETCO2 = blueToothKit.currentETCO2.value,
                    currentRR = blueToothKit.currentRespiratoryRate.value,
                )
            }
        }

        viewModel.updateToastData(
            ToastData(
                text = if (isRecording) getStringAcitivity(R.string.main_stop_record) else getStringAcitivity(R.string.main_start_record),
                duration = 600,
                showMask = false,
                type = ToastType.SUCCESS,
                callback = {
                    viewModel.updateIsRecording(!isRecording)
                    showAlert()
                },
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MANAGE_ALL_FILES_ACCESS_PERMISSION ) {
            if (Environment.isExternalStorageManager()) {
                // 权限已授予，执行数据库恢复操作
                val application = application as CapnoEasyApplication

                // 访问 dbBackupHelperKit 并调用 restoreDatabase
                application.dbBackupHelperKit.startWork(applicationContext, application.database, true)
            } else {
                // 权限未授予，提示用户或采取其他措施
                Toast.makeText(this, "未授予存储权限，无法恢复数据库。", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Composable
    override fun Content() {
        CapnoEasyTheme {
            EtCo2Table(
                viewModel = viewModel,
                blueToothKit = blueToothKit
            )

            EtCo2LineChart(
                blueToothKit = blueToothKit,
                localStorageKit = localStorageKit,
                viewModel = viewModel
            )
        }
    }
}