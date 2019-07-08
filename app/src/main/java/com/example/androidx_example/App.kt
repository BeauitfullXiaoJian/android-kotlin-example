package com.example.androidx_example

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.androidx_example.entity.AppErrorLog
import com.example.androidx_example.until.RoomUntil
import java.io.PrintWriter
import java.io.StringWriter

class App : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
    }

    init {
        instance = this
        initDefaultErrorSave()
    }

    /**
     * 初始化错误日志保存操作
     */
    private fun initDefaultErrorSave() {
        // 未捕获异常统一处理
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
            // 使用默认的处理方式让APP停止运行
            defaultHandler.uncaughtException(thread, throwable)
        }
    }

    companion object {
        lateinit var instance: Application
    }
}