package com.mo.screenlock

import android.app.Application
import android.util.Log

class App : Application() {

    companion object {
        var instance: App? = null

        fun getApp(): App {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Log.d("App", "onCreate...")
    }

}