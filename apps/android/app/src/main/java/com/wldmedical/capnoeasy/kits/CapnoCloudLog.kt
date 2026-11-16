package com.wldmedical.capnoeasy.kits

import android.os.Build
import android.util.Log
import com.wldmedical.capnoeasy.BuildConfig
import io.github.weisiwu.slstracker.SlsLogTracker
import io.github.weisiwu.slstracker.SlsLogTracker.Level
import java.io.IOException
import java.net.URL
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Future
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal object CapnoCloudLog {
    private const val TAG = "CapnoSls"
    private const val CHANNEL = "capnoeasy-android"
    private val stagePattern = Regex("[A-Za-z0-9_.-]{1,96}")

    private val tracker: SlsLogTracker? by lazy {
        val relay = BuildConfig.CAPNO_SLS_RELAY_URL.trim()
        val url = when {
            relay.isNotEmpty() -> relay
            BuildConfig.DEBUG && BuildConfig.CAPNO_SLS_WEB_TRACKING -> SlsLogTracker.DEFAULT_WEBTRACKING_URL
            else -> return@lazy null
        }
        runCatching {
            SlsLogTracker(
                URL(url),
                CHANNEL,
                if (BuildConfig.DEBUG) "debug" else "release",
                BuildConfig.VERSION_NAME
            )
        }.onFailure {
            Log.w(TAG, "Invalid SLS endpoint")
        }.getOrNull()
    }

    private val executor = ThreadPoolExecutor(
        0, 1, 30L, TimeUnit.SECONDS, ArrayBlockingQueue(64),
        { task -> Thread(task, "capno-sls") }
    )

    fun publish(
        level: Level,
        stage: String,
        elapsedMs: Long? = null,
        exceptionType: String? = null,
        statusCode: Int? = null
    ): Future<String?>? {
        if ((!BuildConfig.DEBUG && level == Level.DEBUG) || !stagePattern.matches(stage)) return null
        val client = tracker ?: return null
        val time = System.currentTimeMillis()
        val fields = buildMap<String, Any> {
            put("android_sdk", Build.VERSION.SDK_INT)
            elapsedMs?.let { put("elapsed_ms", it) }
            exceptionType?.let { put("exception_type", it) }
            statusCode?.let { put("status_code", it) }
        }

        return try {
            executor.submit<String?> {
                try {
                    client.send(level, stage, time, fields).also { requestId ->
                        if (BuildConfig.DEBUG) Log.d(TAG, "Accepted $level $stage request_id=$requestId")
                    }
                } catch (error: IOException) {
                    Log.w(TAG, "SLS write failed for $level $stage: ${error.javaClass.simpleName}")
                    null
                } catch (error: RuntimeException) {
                    Log.w(TAG, "SLS write failed for $level $stage: ${error.javaClass.simpleName}")
                    null
                }
            }
        } catch (_: RejectedExecutionException) {
            Log.w(TAG, "SLS queue full. Dropped $level $stage")
            null
        }
    }
}
