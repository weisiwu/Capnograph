package com.wldmedical.capnoeasy.kits

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import io.github.weisiwu.slstracker.SlsLogTracker.Level

internal enum class GattNotification(val failureStage: String) {
    HANDSHAKE("BluetoothKit.gatt_handshake_subscription_failed"),
    WAVEFORM("BluetoothKit.gatt_waveform_subscription_failed")
}

internal fun observeGattWriteStart(write: () -> Int): Int {
    val stage = "BluetoothKit.gatt_write_start_failed"
    val status = try {
        write()
    } catch (e: Exception) {
        ErrorReporter.report(e, stage)
        throw e
    }
    if (status != BluetoothGatt.GATT_SUCCESS) ErrorReporter.diagnostic(Level.ERROR, stage, status)
    return status
}

internal fun gattWriteCompleted(status: Int): Boolean {
    if (status == BluetoothGatt.GATT_SUCCESS) return true
    ErrorReporter.diagnostic(Level.ERROR, "BluetoothKit.gatt_write_callback_failed", status)
    return false
}

internal fun enableGattNotification(notification: GattNotification, subscribe: () -> Boolean): Boolean {
    val enabled = try {
        subscribe()
    } catch (e: Exception) {
        ErrorReporter.report(e, notification.failureStage)
        return false
    }
    if (!enabled) ErrorReporter.diagnostic(Level.ERROR, notification.failureStage)
    return enabled
}

internal fun checkGattDataCharacteristics(
    receive: BluetoothGattCharacteristic?,
    send: BluetoothGattCharacteristic?
): Boolean {
    if (receive == null) {
        ErrorReporter.diagnostic(Level.ERROR, "BluetoothKit.gatt_receive_characteristic_missing")
    }
    if (send == null) {
        ErrorReporter.diagnostic(Level.ERROR, "BluetoothKit.gatt_send_characteristic_missing")
    }
    return receive != null && send != null
}
