package com.wldmedical.capnoeasy

import DatabaseBackupHelper
import DatabaseBackupHelperManager.dbBackupHelperKit
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.wldmedical.capnoeasy.kits.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CapnoEasyApplication : Application() {
    lateinit var database: AppDatabase

    // 备份数据库
    lateinit var dbBackupHelperKit: DatabaseBackupHelper

    companion object {
        lateinit var context: Context
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        database = AppDatabase.getDatabase(this)

        println("wswTest 这里？？")
        // 启动后开始备份数据库或者恢复数据
        DatabaseBackupHelperManager.initialize(this)
        dbBackupHelperKit = DatabaseBackupHelperManager.dbBackupHelperKit
        dbBackupHelperKit.startWork(applicationContext, database)
    }
}