package com.wldmedical.capnoeasy

import android.app.Application
import com.wldmedical.capnoeasy.kits.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CapnoEasyApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getDatabase(this)
    }
}