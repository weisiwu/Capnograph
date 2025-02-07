package com.wldmedical.capnoeasy.kits

import kotlin.math.abs
import kotlin.random.Random
import kotlin.concurrent.fixedRateTimer
import java.util.LinkedList

const val POINTS_PER_SECOND = 100
const val MAX_POINTS = 500
const val INTERVAL_MS = 1000 / POINTS_PER_SECOND

/***
 * 蓝牙demo数据
 * 1、每秒生成100个点，所有生成的点都是连续光滑曲线
 * 2、最多存储500个
 */
class RandomCurveGenerator(
    private val updateGraph: ((LinkedList<Float>) -> Unit)? = null
) {
    private val curvePoints: LinkedList<Float> = LinkedList() // 存储生成的点
    private var keyPoints: MutableList<Float> = mutableListOf()

    init {
        startGenerating()
    }

    private fun startGenerating() {
        fixedRateTimer(initialDelay = 0, period = INTERVAL_MS.toLong()) {
            generateAndStorePoint()
            updateGraph?.invoke(curvePoints)
        }
    }

    private fun generateAndStorePoint() {
        // 生成新点
        if (keyPoints.isEmpty() || Random.nextFloat() < 0.1) { // 10%几率生成新的关键点
            val keyPoint = Random.nextFloat() * 50 // 或者根据需要生成不同的范围
            keyPoints.add(keyPoint)
            keyPoints.sort()
        }

        if (keyPoints.size > 1) {
            val newPoint = generateSmoothPoint()
            if (curvePoints.size >= MAX_POINTS) {
                curvePoints.removeFirst() // 保持点数在最大值之内
            }
            curvePoints.add(newPoint)
        }
    }

    private fun generateSmoothPoint(): Float {
        // 线性插值逻辑
        val size = curvePoints.size
        val index = (size - 1).coerceAtLeast(0)
        val t = Random.nextFloat()

        return if (size == 0) {
            Random.nextFloat() * 50 // 直接生成第一个点
        } else {
            // 使用当前数组生成光滑的点
            val start = curvePoints[index]
            val end = keyPoints[(keyPoints.size - 1).coerceAtMost(index + 1)]
            start + t * (end - start)
        }
    }
}

fun main() {
    RandomCurveGenerator() // 启动曲线生成
    Thread.sleep(10000) // 保持程序运行10秒
}
