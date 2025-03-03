package com.wldmedical.capnoeasy.pages

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.kits.BluetoothType
import com.wldmedical.capnoeasy.kits.PDFSetting
import com.wldmedical.capnoeasy.kits.Patient
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import com.wldmedical.hotmeltprint.PrintSetting
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/***
 * 主页
 */
class MainActivity : BaseActivity() {
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
        val context = this;
        try {
            val deviceAddress = blueToothKit.getSavedBLEDeviceAddress(this)
            if (deviceAddress != null) {
                val device = blueToothKit.bluetoothAdapter?.getRemoteDevice(deviceAddress)
                // 尝试自动连接已经配对设备
                blueToothKit.connectDevice(device)
            }

            // 从用户偏好里读取默认打印设置
            val printSetting: PrintSetting = localStorageKit.loadPrintSettingFromPreferences(this)
            printSetting.printAddress?.let { viewModel.updatePrintAddress(it) }
            printSetting.printPhone?.let { viewModel.updatePrintPhone(it) }
            printSetting.printUrl?.let { viewModel.updatePrintUrl(it) }
            printSetting.printUrlQRCode?.let { viewModel.updatePrintUrlQRCode(it) }
            printSetting.printLogo?.let {
                viewModel.updatePrintLogo(it)
            }

            // 读取默认pdf设置
            val pdfSetting: PDFSetting = localStorageKit.loadPDFSettingFromPreferences(this)
            pdfSetting.pdfHospitalName?.let { viewModel.updatePdfHospitalName(it) }
            pdfSetting.pdfDepart?.let { viewModel.updatePdfDepart(it) }
            pdfSetting.pdfBedNumber?.let { viewModel.updatePdfBedNumber(it) }
            pdfSetting.pdfIDNumber?.let { viewModel.updatePdfIDNumber(it) }

            // 默认扫描，连接周围打印机
            blueToothKit.searchDevices(BluetoothType.CLASSIC)
        } catch (e: Exception) {
            println("wswTest 捕获到自动链接BLE配对设备异常: ${e.message}")
        }
    }

    private fun showAlert() {
        if(!viewModel.isRecording.value) {
            viewModel.updateConfirmData(
                ConfirmData(
                    title = getString(R.string.main_record_save_success),
                    text = getString(R.string.main_record_save_check),
                    confirm_btn_text = getString(R.string.main_confirm),
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
                text = if (isRecording) getString(R.string.main_stop_record) else getString(R.string.main_start_record),
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