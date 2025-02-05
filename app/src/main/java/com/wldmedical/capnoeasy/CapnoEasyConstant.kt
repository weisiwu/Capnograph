package com.wldmedical.capnoeasy

import java.text.DecimalFormat
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

interface BaseEnmu<T> {
    val value: T
}

data class wheelPickerConfig<out E: BaseEnmu<V>, V>(
    val title: String,
    val items: List<E>,
    val defaultValue: E
)

val RR_UNIT: String = "bmp"
val TIME_UNIT: String = "S"
val O2_UNIT: String = "%"

enum class CO2_UNIT(val rawValue: String):BaseEnmu<String> {
    MMHG("mmHg"),
    KPA("kPa"),
    PERCENT("%");

    override val value: String = rawValue
}

val co2Units = listOf(CO2_UNIT.KPA, CO2_UNIT.MMHG, CO2_UNIT.PERCENT)
val co2UnitsObj = wheelPickerConfig(items = co2Units, title = "CO2 单位", defaultValue = CO2_UNIT.MMHG)

enum class CO2_SCALE(val rawValue: Float):BaseEnmu<Float> {
    SMALL(50f),
    MIDDLE(60f),
    LARGE(75f),

    KPA_SMALL(6.7f),
    KPA_MIDDLE(8f),
    KPA_LARGE(10f),

    PERCENT_SMALL(6.6f),
    PERCENT_MIDDLE(7.9f),
    PERCENT_LARGE(9.9f);

    override val value: Float = rawValue
}

enum class WF_SPEED(val rawValue: Float):BaseEnmu<Float> {
    SMALL(1f),
    MIDDLE(2f),
    LARGE(4f);

    override val value: Float = rawValue
}

val wfSpeeds = listOf(WF_SPEED.SMALL, WF_SPEED.MIDDLE, WF_SPEED.LARGE)
val wfSpeedsObj = wheelPickerConfig(items = wfSpeeds, title = "WF Speed", defaultValue = WF_SPEED.MIDDLE)

val maxETCO2Range = 25f..50f
val minETCO2Range = 0f..25f
val ETCO2Range = 0f..50f

val maxRRRange = 0f..60f
val minRRRange = 0f..30f
val RRRange = 0f..60f

val asphyxiationTimeRange = 10f..60f
val o2CompensationRange = 0f..100f

val maxMaskZIndex = 9999f
val maskOpacity = 0.2f

val patientAgeRange = 0..200

enum class GENDER(val title: String) {
    MALE(title = "男"),
    FORMALE(title = "女")
}

// 通过名字获取对应的值
fun <T : Any> getValueByKey(obj: T, key: String): Any? {
    val kClass = obj::class as KClass<T>
    val property = kClass.memberProperties.find { it.name == key }

    return if (property is KProperty1<T, *>) {
        property.get(obj)
    } else {
        null
    }
}

enum class PageScene(val title: String) {
    HOME_PAGE("CapnoGraph"), // 主页
    SETTING_PAGE("CapnoGraph-设置"), // 设置页
    DEVICES_LIST_PAGE("CapnoGraph-附近设备"), // 设备列表页
    SYSTEM_CONFIG_PAGE("CapnoGraph-系统设置"), // 设置页 - 系统设置
    ALERT_CONFIG_PAGE("CapnoGraph-报警参数"), // 设置页 - 报警参数
    DISPLAY_CONFIG_PAGE("CapnoGraph-显示参数"), // 设置页 - 显示参数
    MODULE_CONFIG_PAGE("CapnoGraph-模块参数"), // 设置页 - 模块参数
    PRINT_CONFIG_PAGE("CapnoGraph-打印设置"), // 设置页 - 打印设置
    HISTORY_LIST_PAGE("CapnoGraph-历史记录"), // 设置页 - 历史记录列表
    HISTORY_DETAIL_PAGE("CapnoGraph-记录详情"), // 设置页 - 历史记录详情
}

val FloatToFixed = DecimalFormat("#")

enum class LanguageTypes(val cname: String) {
    CHINESE(cname = "中文"),
    ENGLISH(cname = "English")
}

data class SystemIno(
    val name: String,
    val value: String,
    val isRadio: Boolean = false,
    val radios: Array<LanguageTypes>? = null
)

val InfinityDuration: Long = 1000000000