package com.wldmedical.capnoeasy.kits

import kotlin.concurrent.fixedRateTimer
import kotlin.math.sin

const val POINTS_PER_SECOND = 100
const val MAX_POINTS = 500
const val INTERVAL_MS = 1000 / POINTS_PER_SECOND

/***
 * 蓝牙demo数据
 * 1、每秒生成100个点，所有生成的点都是连续光滑曲线
 * 2、最多存储500个
 */
class RandomCurveGenerator(
    private val updateGraph: ((Float) -> Unit)? = null
) {
    private var index = 0f

    init {
        startGenerating()
    }

    private fun startGenerating() {
        fixedRateTimer(initialDelay = 0, period = INTERVAL_MS.toLong()) {
            generateAndStorePoint()
        }
    }

    private fun generateAndStorePoint() {
        val newPoint = generateSineWaveInRange(index)
        index += 0.01f
        updateGraph?.invoke(newPoint)
    }

    fun generateSineWaveInRange(x: Float = 0f): Float {
        val scaledValue = sin(x) * 25f
        val finalValue = scaledValue + 25f
        return finalValue
    }
}

fun main() {
    RandomCurveGenerator() // 启动曲线生成
    Thread.sleep(1000000) // 保持程序运行10秒
}
