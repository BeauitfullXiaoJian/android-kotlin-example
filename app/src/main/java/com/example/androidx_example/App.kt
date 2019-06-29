package com.example.androidx_example

import android.app.Application
import android.util.Log
import androidx.work.Configuration

class App : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}