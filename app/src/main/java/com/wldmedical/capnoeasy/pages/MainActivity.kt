package com.wldmedical.capnoeasy.pages

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.wldmedical.capnoeasy.GENDER
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.components.ConfirmData
import com.wldmedical.capnoeasy.components.EtCo2LineChart
import com.wldmedical.capnoeasy.components.EtCo2Table
import com.wldmedical.capnoeasy.components.ToastData
import com.wldmedical.capnoeasy.components.ToastType
import com.wldmedical.capnoeasy.kits.Patient
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/***
 * 主页
 */
class MainActivity : BaseActivity() {
    override val pageScene = PageScene.HOME_PAGE

    private  var startRecordTime: LocalDateTime? = null

    private  var endRecordTime: LocalDateTime? = null

    override fun onTabClick(index: Int): Boolean {
        super.onTabClick(index)

//        // 正在记录，且跳往非主页
//        if (viewModel.isRecording.value && index != 1) {
//            showAlert()
//            return false
//        }
        return true
    }

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
    }

    private fun showAlert() {
        if(!viewModel.isRecording.value) {
            viewModel.updateConfirmData(
                ConfirmData(
                    title = "记录保存成功",
                    text = "保存的记录可在设置>历史记录中查看",
                    confirm_btn_text = "确认",
                    onClick = {
                        viewModel.updateConfirmData(null)
                    }
                )
            )
        }
    }

    override fun onNavBarRightClick() {
        val isRecording = viewModel.isRecording.value
        startRecordTime = LocalDateTime.now()

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
                    patient = patient,
                    startTime = startRecordTime ?: LocalDateTime.now(),
                    endTime = endRecordTime ?: LocalDateTime.now()
                )
            }
        }

        viewModel.updateToastData(
            ToastData(
                text = if (isRecording) "停止记录" else "启动记录",
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
            EtCo2LineChart(
                blueToothKit = blueToothKit,
                viewModel = viewModel
            )

            EtCo2Table(
                viewModel = viewModel,
                blueToothKit = blueToothKit
            )
        }
    }
}