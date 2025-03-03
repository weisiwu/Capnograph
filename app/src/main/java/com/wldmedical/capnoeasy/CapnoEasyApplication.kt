package com.wldmedical.capnoeasy

import android.app.Application
import android.content.Context
import com.wldmedical.capnoeasy.kits.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CapnoEasyApplication : Application() {
    lateinit var database: AppDatabase

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        database = AppDatabase.getDatabase(this)
    }
}