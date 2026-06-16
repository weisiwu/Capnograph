package com.wldmedical.capnoeasy

import DatabaseBackupHelper
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.wldmedical.capnoeasy.kits.AppDatabase
import com.wldmedical.capnoeasy.kits.ErrorReporter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CapnoEasyApplication : Application() {
    // 记录当前打开了多少页面
    private var activityCount = 0
    private var startedActivityCount = 0

    lateinit var database: AppDatabase

    // 备份数据库
    lateinit var dbBackupHelperKit: DatabaseBackupHelper

    companion object {
        lateinit var context: Context
    }

    // 获取当前活动数量的方法
    fun getActivityCount(): Int {
        return activityCount
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()

        ErrorReporter.initialize(this)

        context = applicationContext

        try {
            database = AppDatabase.getDatabase(this)
        } catch (e: Exception) {
            ErrorReporter.report(e, "Application.database_init")
            throw e
        }

        try {
            // TODO:(wsw) 这里需要等待时机，等待可以恢复的时候再行恢复
            // 启动后开始备份数据库或者恢复数据
            DatabaseBackupHelperManager.initialize(this)
            println("wswTest 这里开始备份数据库")
            dbBackupHelperKit = DatabaseBackupHelperManager.dbBackupHelperKit
            dbBackupHelperKit.startWork(applicationContext, database)
            println("wswTest 完成备份数据库")
        } catch (e: Exception) {
            ErrorReporter.report(e, "Application.database_backup_start")
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityCount++ // 增加活动计数
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityCount-- // 减少活动计数
            }

            override fun onActivityStarted(activity: Activity) {
                startedActivityCount++
                ErrorReporter.setAppForeground(true)
            }
            override fun onActivityResumed(activity: Activity) {
                ErrorReporter.setContext("foreground_activity", activity.javaClass.simpleName)
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                startedActivityCount = (startedActivityCount - 1).coerceAtLeast(0)
                if (startedActivityCount == 0) {
                    ErrorReporter.setAppForeground(false)
                }
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })

    }
}
