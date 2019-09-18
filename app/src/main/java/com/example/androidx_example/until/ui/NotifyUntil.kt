package com.example.androidx_example.until.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotifyUntil {

    private var notifyChanelId = 10000

    /**
     * 获取一个NotifyId
     * @return Int
     */
    private fun getNewNotifyId(): Int {
        return ++notifyChanelId
    }

    /**
     * 获取当前通知的Id
     */
    fun getCurrentNotifyId(): Int {
        return notifyChanelId
    }

    /**
     * 获取一个简单通知对象
     */
    fun getSimpleNotifyBuilder(
        appContext: Context,
        notifyName: String,
        title: String,
        content: String,
        iconId: Int? = null,
        descriptionText: String? = null,
        importance: Int? = null
    ): NotificationCompat.Builder {
        val notifyId = getNewNotifyId()
        val channelId = notifyId.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                notifyName,
                importance ?: NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
            descriptionText?.also { channel.description = it }
            val notificationManager: NotificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(appContext, channelId).apply {
            setContentTitle(title)
            setContentText(content)
            iconId?.also { setSmallIcon(it) }
        }
    }


    /**
     * 显示一个简单的通知消息
     */
    fun showSimpleNotify(
        appContext: Context,
        notifyName: String,
        title: String,
        content: String,
        iconId: Int? = null,
        descriptionText: String? = null,
        importance: Int? = null
    ) {
        val notify = getSimpleNotifyBuilder(
            appContext,
            notifyName,
            title,
            content,
            iconId,
            descriptionText,
            importance
        ).build()
        NotificationManagerCompat.from(appContext).notify(notifyChanelId, notify)
    }
}