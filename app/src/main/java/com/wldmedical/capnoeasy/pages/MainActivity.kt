package com.wldmedical.capnoeasy.pages

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
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
import com.wldmedical.capnoeasy.kits.Patient
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import com.wldmedical.hotmeltprint.PrintSetting
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/***
 * 主页
 */
class MainActivity : BaseActivity() {
    private val STORAGE_PERMISSION_CODE = 101

    override var pageScene = PageScene.HOME_PAGE

    private  var startRecordTime: LocalDateTime? = null

    private  var endRecordTime: LocalDateTime? = null

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
                val patient = Patient(
                    name = viewModel.patientName.value ?: "",
                    gender = viewModel.patientGender.value ?: GENDER.MALE,
                    age = viewModel.patientAge.value ?: 0
                )
                localStorageKit.savePatient(patient)
                localStorageKit.saveRecord(
                    context = context,
                    patient = patient,
                    recordName = "${viewModel.pdfHospitalName.value}_${viewModel.pdfReportName.value}",
                    lineChart = viewModel.lineChart,
                    data = viewModel.totalCO2WavedData.toList(),
                    startTime = startRecordTime ?: LocalDateTime.now(),
                    endTime = endRecordTime ?: LocalDateTime.now(),
                    maxETCO2 = viewModel.CO2Scale.value.value
                )
            }
        } else {
            startRecordTime = LocalDateTime.now()
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("wswTest 权限辩护到底算了算了 ====================== $requestCode")
        if (requestCode == 101) {
            if (grantResults.isNotEmpty()) {
                println("wswTest 授权的最终结果是>>>>>>>>>>>>>> ${grantResults[0]}")
            }

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val application = application as CapnoEasyApplication

                // 访问 dbBackupHelperKit 并调用 restoreDatabase
                println("wswTest 授权接受后开始处理东西")
                application.dbBackupHelperKit.startWork(applicationContext, application.database)
            } else {
                println("wswTEst 为什么会失败》》》》》》")
                Log.e("DatabaseBackupHelper", "Storage Permission Denied")
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
                viewModel = viewModel
            )
        }
    }
}