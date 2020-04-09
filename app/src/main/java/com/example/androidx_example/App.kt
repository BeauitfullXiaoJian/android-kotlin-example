package com.example.androidx_example

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.androidx_example.entity.AppErrorLog
import com.example.androidx_example.until.ChatMessageBus
import com.example.androidx_example.until.sql.RoomUntil
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class App : Application(), Configuration.Provider {


    init {
        instance = this
        initDefaultErrorSave()
    }

    /**
     * 初始化错误日志保存操作
     */
    private fun initDefaultErrorSave() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val pw = PrintWriter(StringWriter())
            throwable.printStackTrace(pw)
            RoomUntil.db.appErrorLogDao().insert(
                AppErrorLog(
                    currentTime = System.currentTimeMillis(),
                    throwableMsg = throwable.message ?: "EMPTY",
                    stackIno = pw.toString()
                )
            )
            defaultHandler?.uncaughtException(thread, throwable)
            exitProcess(0)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
    }


    companion object {
        lateinit var instance: Application
        lateinit var mainActivity: MainActivity
    }
}