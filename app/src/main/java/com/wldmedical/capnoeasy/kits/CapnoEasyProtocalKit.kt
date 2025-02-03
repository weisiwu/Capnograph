package com.wldmedical.capnoeasy.kits

import java.util.UUID

/**
 * 指令集合
 */
enum class SensorCommand(val value: Int) {
    CO2Waveform(value = 0x80), // 接受数据
    Zero(value = 0x82), // 校零
    Settings(value = 0x84), // 更改读取设置
    Expand(value = 0xF2), // 获取系统扩展设置，如硬件、软件版本
    NACKError(value = 0xC8), // 非应答错误
    GetSoftwareRevision(value = 0xCA), // 获取软件版本
    StopContinuous(value = 0xC9), // 停止连读模式
    ResetNoBreaths(value = 0xCC), // 清除窒息状态
    Reset(value = 0xF8), // 复位指令
}

// 16 位 UUID 转换为 128 位 UUID
fun convert16BitUUIDto128Bit(uuid16: Int): UUID {
    val uuidStr = String.format("%04X", uuid16) // 将整数转换为4位十六进制字符串
    val uuid128 = "0000${uuidStr}-0000-1000-8000-00805f9b34fb"
    return UUID.fromString(uuid128)
}

/**
 * 蓝牙服务UUID
 */
enum class BLEServers(val value: Int) {
    BLESendDataSer(value = 0xFFE5), // 65509
    BLEReceiveDataSer(value = 0xFFE0), // 65504
    BLEModuleParamsSer(value = 0xFF90), // 65424
    BLEAntihijackSer(value = 0xFFC0), // 65472 反蓝牙劫持
}

/**
 * 蓝牙服务顺序排列 - 转换为16进制字符串
 */
enum class BLEServersUUID(val value: UUID) {
    BLEAntihijackSer(
        value = convert16BitUUIDto128Bit(BLEServers.BLEAntihijackSer.value)
    ),
    BLEReceiveDataSer(
        value = convert16BitUUIDto128Bit(BLEServers.BLEReceiveDataSer.value)
    ),
    BLESendDataSer(
        value = convert16BitUUIDto128Bit(BLEServers.BLESendDataSer.value)
    ),
}

val sortedBLEServersUUID = listOf(
    BLEServersUUID.BLEAntihijackSer.value,
    BLEServersUUID.BLEReceiveDataSer.value,
    BLEServersUUID.BLESendDataSer.value,
)

/**
 * 蓝牙特征UUID
 */
enum class BLECharacteristics(val value: Int) {
    BLESendDataCha(value = 0xFFE9),
    BLEReceiveDataCha(value = 0xFFE4),
    BLERenameCha(value = 0xFF91),
    BLEBaudCha(value = 0xFF93),
    BLEAntihijackChaNofi(value = 0xFFC2),
    BLEAntihijackCha(value = 0xFFC1),
};

/**
 * 蓝牙特征UUID - 转换为16进制字符串
 */
enum class BLECharacteristicUUID(val value: UUID) {
    BLEAntihijackCha(
        value = convert16BitUUIDto128Bit(BLECharacteristics.BLEAntihijackCha.value)
    ),
    BLESendDataCha(
        value = convert16BitUUIDto128Bit(BLECharacteristics.BLESendDataCha.value)
    ),
    BLEReceiveDataCha(
        value = convert16BitUUIDto128Bit(BLECharacteristics.BLEReceiveDataCha.value)
    ),
    BLEAntihijackChaNofi(
        value = convert16BitUUIDto128Bit(BLECharacteristics.BLEAntihijackChaNofi.value)
    ),
};

val sortedBLECharacteristicUUID = listOf(
    BLECharacteristicUUID.BLEAntihijackCha.value,
    BLECharacteristicUUID.BLEAntihijackChaNofi.value,
    BLECharacteristicUUID.BLEReceiveDataCha.value,
    BLECharacteristicUUID.BLESendDataCha.value,
)

/**
 * 蓝牙描述符UUID
 */
enum class BLEDescriptorUUID(val value: Int) {
    CCCDDescriptor(value = 0x2902),
}

/**
 * 校零状态 - 走80H结果获取
 * 0/1/2/3其实是将对应标志位换出来的值
 */
enum class ZSBState(val value: Int) {
    NOZeroning(value = 0), // 不处于校零过程
    Resetting(value = 4), // 正在校零
    NotReady( value = 8), // 需要校零
    NotReady2(value = 12), // 校零
}

/**
 * 接受数据信息DPI（等位替代ISB）
 */
enum class ISBState80H(val value: Int) {
    CO2WorkStatus(value = 0x01), // CO2工作状态
    ETCO2Value(value = 0x02), // 监测计算到的 ETCO2 数值
    RRValue(value = 0x03), // 表示监测计算到的呼吸率数值
    FiCO2Value(value = 0x04), // 表示监测计算到的 FiCO2 数值
    DetectBreath(value = 0x05), // 表示检测到一次呼吸，该帧只有在检查到呼吸之后才会设置一次
    DeviceError(value = 0x07), // 该帧在硬件确实有问题的时候，将每秒自动汇报一次
}

/**
 * 【ISB】读取/设置模块指令
 */
enum class ISBState84H(val value: Int) {
    NoUse(value = 0), // 无效的参数设置
    AirPressure(value = 1), // 大气压
    Temperature(value = 4), // 气体温度
    ETCO2Period(value = 5), // ETCO2 时间周期
    NoBreaths(value = 6), // 窒息时间
    SetCO2Unit(value = 7), // 设置CO2Unit
    Sleep(value = 8), // 休眠模式
    ZeroPointGasType(value = 9), // 零点气类型
    GasCompensation(value = 11), // 读取/设置 气体补偿
    GetSensorPartNumber(value = 18), // 获取sensor part number
    GetSerialNumber(value = 20), // 获取sensor serial number
    GetHardWareRevision(value = 21), // 获取硬件版本
    Stop(value = 27), // 停止采样气泵
}

/**
 * 【ISB】扩展指令
 */
enum class ISBStateF2H(val value: Int) {
    EnergyStatus(value = 0x29), // 读取电源状态
    NoUse(value = 0x2A), // 读取报警配置
    CO2Scale(value = 0x2C), // 波形显示范围
}

/**
 * 【ISB】获取软件信息指令 这是ISB只是用来区分场景，并非真实的设备ISB值
 */
enum class ISBStateCAH(val value: Int) {
    GetSoftWareRevision(value = 99), // 获取软件版本
    GetProductionDate(value = 98), // 生产日期
    GetModuleName(value = 97), // 模块名称
}

/***
 * CapnoEasy设备通讯协议
 * 1、设置相关指令
 * 2、接受发送过来的数据
 */
