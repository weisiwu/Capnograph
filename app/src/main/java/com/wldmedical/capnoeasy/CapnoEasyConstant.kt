package com.wldmedical.capnoeasy

enum class CO2_UNIT(value: String) {
    MMHG(value = "mmHg"),
    KPA(value = "kPa"),
    PERCENT(value = "%"),
}

enum class CO2_SCALE(value: Float) {
    SMALL(value = 6.7f),
    MIDDLE(value = 8f),
    LARGE(value = 10f),
}

enum class WF_SPEED(value: Float) {
    SMALL(value = 1f),
    MIDDLE(value = 2f),
    LARGE(value = 4f),
}

val maxETCO2Range = 25f..50f
val minETCO2Range = 0f..25f

val maxRRRange = 0..60
val minRRRange = 0..30

val asphyxiationTimeRange = 10..60
val o2Compensation = 0..100