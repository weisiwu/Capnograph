package com.wldmedical.capnoeasy

import DatabaseBackupHelper
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.tencent.bugly.crashreport.CrashReport
import com.wldmedical.capnoeasy.kits.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CapnoEasyApplication : Application() {
    // 记录当前打开了多少页面
    private var activityCount = 0

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

        // TODO: 临时注释掉
//        CrashReport.initCrashReport(getApplicationContext())

        context = applicationContext

        database = AppDatabase.getDatabase(this)

        // 启动后开始备份数据库或者恢复数据
        DatabaseBackupHelperManager.initialize(this)
        dbBackupHelperKit = DatabaseBackupHelperManager.dbBackupHelperKit
        dbBackupHelperKit.startWork(applicationContext, database)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityCount++ // 增加活动计数
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityCount-- // 减少活动计数
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })

    }
}