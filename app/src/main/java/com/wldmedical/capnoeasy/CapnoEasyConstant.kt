package com.wldmedical.capnoeasy

interface BaseEnmu<T> {
    val value: T
}

data class wheelPickerConfig<out E: BaseEnmu<V>, V>(
    val title: String,
    val items: List<E>,
    val defaultValue: E
)

enum class CO2_UNIT(val rawValue: String):BaseEnmu<String> {
    MMHG("mmHg"),
    KPA("kPa"),
    PERCENT("%");

    override val value: String = rawValue
}

val co2Units = listOf(CO2_UNIT.KPA, CO2_UNIT.MMHG, CO2_UNIT.PERCENT)
val co2UnitsObj = wheelPickerConfig(items = co2Units, title = "CO2 单位", defaultValue = CO2_UNIT.MMHG)

enum class CO2_SCALE(val rawValue: Float):BaseEnmu<Float> {
    SMALL(6.7f),
    MIDDLE(8f),
    LARGE(10f);

    override val value: Float = rawValue
}

val co2Scales = listOf(CO2_SCALE.SMALL, CO2_SCALE.MIDDLE, CO2_SCALE.LARGE)
val co2ScalesObj = wheelPickerConfig(items = co2Scales, title = "CO2 Scale", defaultValue = CO2_SCALE.MIDDLE)

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

val maxRRRange = 0..60
val minRRRange = 0..30

val asphyxiationTimeRange = 10..60
val o2Compensation = 0..100