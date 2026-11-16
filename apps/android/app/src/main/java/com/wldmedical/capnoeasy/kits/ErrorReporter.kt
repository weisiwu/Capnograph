package com.wldmedical.capnoeasy.kits

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import com.tencent.bugly.crashreport.CrashReport
import io.github.weisiwu.slstracker.SlsLogTracker.Level
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

object ErrorReporter {
    private const val TAG = "ErrorReporter"
    private const val BUGLY_APP_ID_KEY = "BUGLY_APPID"
    private const val MAX_USER_DATA_KEY_LENGTH = 50
    private const val MAX_USER_DATA_VALUE_LENGTH = 200
    private const val DEFAULT_REPORT_THROTTLE_MS = 60_000L

    private val initialized = AtomicBoolean(false)
    private val lastReportAtByStage = ConcurrentHashMap<String, Long>()

    @Volatile
    private var appContext: Context? = null

    fun initialize(context: Context) {
        val applicationContext = context.applicationContext
        appContext = applicationContext

        if (initialized.get()) {
            return
        }

        runCatching {
            val debug = isDebuggable(applicationContext)
            val strategy = CrashReport.UserStrategy(applicationContext).apply {
                setAppVersion(getAppVersion(applicationContext))
                setAppPackageName(applicationContext.packageName)
                setAppChannel(if (debug) "debug" else "release")
                setUploadProcess(true)
                setBuglyLogUpload(true)
                setEnableNativeCrashMonitor(true)
                setEnableANRCrashMonitor(true)
            }

            val appId = readManifestString(applicationContext, BUGLY_APP_ID_KEY)
            if (appId.isNullOrBlank()) {
                CrashReport.initCrashReport(applicationContext, strategy)
            } else {
                CrashReport.initCrashReport(applicationContext, appId, debug, strategy)
            }

            initialized.set(true)
            setContext("app_version", getAppVersion(applicationContext))
            setContext("app_channel", if (debug) "debug" else "release")
            setContext("android_sdk", Build.VERSION.SDK_INT.toString())
        }.onFailure {
            report(it, "Application.crash_reporter_init_failed")
        }
    }

    fun startupCheckpoint(stage: String, startedAt: Long, level: Level = Level.INFO) {
        val elapsedMs = SystemClock.elapsedRealtime() - startedAt
        val message = "Startup [$stage] elapsedMs=$elapsedMs sdk=${Build.VERSION.SDK_INT}"
        if (level == Level.WARN) Log.w(TAG, message) else Log.i(TAG, message)
        CapnoCloudLog.publish(level, stage, elapsedMs = elapsedMs)
        setContext("startup_stage", stage)
    }

    fun diagnostic(level: Level, stage: String, statusCode: Int? = null, detailCode: Int? = null) {
        val priority = when (level) {
            Level.DEBUG -> Log.DEBUG
            Level.INFO -> Log.INFO
            Level.WARN -> Log.WARN
            Level.ERROR -> Log.ERROR
        }
        val status = statusCode?.let { " status=$it" }.orEmpty()
        val detail = detailCode?.let { " detail=$it" }.orEmpty()
        Log.println(priority, TAG, "[$stage]$status$detail")
        if (level >= Level.WARN && isThrottled(stage, DEFAULT_REPORT_THROTTLE_MS)) return
        CapnoCloudLog.publish(level, stage, statusCode = statusCode, detailCode = detailCode)
    }

    fun setContext(key: String, value: Any?) {
        val context = appContext ?: return
        if (!initialized.get()) {
            return
        }

        runCatching {
            CrashReport.putUserData(
                context,
                sanitizeKey(key),
                sanitizeValue(value)
            )
        }.onFailure {
            Log.w(TAG, "Failed to attach crash context: $key", it)
        }
    }

    fun setAppForeground(isForeground: Boolean) {
        val context = appContext ?: return
        if (!initialized.get()) {
            return
        }

        runCatching {
            CrashReport.setIsAppForeground(context, isForeground)
        }.onFailure {
            Log.w(TAG, "Failed to update app foreground state", it)
        }
    }

    fun report(
        throwable: Throwable,
        stage: String,
        metadata: Map<String, Any?> = emptyMap(),
        throttleMs: Long = DEFAULT_REPORT_THROTTLE_MS
    ) {
        Log.e(TAG, "[$stage] ${throwable.message.orEmpty()}", throwable)

        if (isThrottled(stage, throttleMs)) {
            return
        }

        CapnoCloudLog.publish(
            Level.ERROR,
            stage,
            exceptionType = throwable.javaClass.simpleName,
            pointCount = metadata["point_count"] as? Int,
            failureCount = metadata["failure_count"] as? Int
        )
        val context = appContext ?: return
        if (!initialized.get()) {
            return
        }

        setContext("last_error_stage", stage)
        setContext("last_error_type", throwable.javaClass.simpleName)
        metadata.forEach { (key, value) ->
            setContext("last_${key}", value)
        }

        runCatching {
            CrashReport.postCatchedException(throwable, Thread.currentThread(), false)
        }.onFailure {
            Log.w(TAG, "Failed to post caught exception to Bugly", it)
        }
    }

    fun coroutineExceptionHandler(
        stage: String,
        metadata: Map<String, Any?> = emptyMap()
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            report(throwable, stage, metadata)
        }
    }

    private fun isThrottled(stage: String, throttleMs: Long): Boolean {
        if (throttleMs <= 0L) {
            return false
        }

        val now = System.currentTimeMillis()
        val previous = lastReportAtByStage[stage]
        if (previous != null && now - previous < throttleMs) {
            return true
        }

        lastReportAtByStage[stage] = now
        return false
    }

    private fun sanitizeKey(rawKey: String): String {
        return rawKey
            .replace(Regex("[^A-Za-z0-9_.-]"), "_")
            .take(MAX_USER_DATA_KEY_LENGTH)
            .ifBlank { "unknown" }
    }

    private fun sanitizeValue(rawValue: Any?): String {
        return rawValue
            ?.toString()
            ?.replace(Regex("\\s+"), " ")
            ?.take(MAX_USER_DATA_VALUE_LENGTH)
            ?: "null"
    }

    private fun isDebuggable(context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    @Suppress("DEPRECATION")
    private fun getAppVersion(context: Context): String {
        return runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "unknown"
        }.getOrDefault("unknown")
    }

    @Suppress("DEPRECATION")
    private fun readManifestString(context: Context, key: String): String? {
        return runCatching {
            val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
            }
            applicationInfo.metaData?.getString(key)
        }.getOrNull()
    }
}
