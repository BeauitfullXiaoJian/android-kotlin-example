package com.example.androidx_example.works

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.androidx_example.App
import com.example.androidx_example.entity.MessageSaveData
import com.example.androidx_example.until.RoomUntil
import com.example.androidx_example.until.api.HttpRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *  聊天消息发送Worker
 */
class MessageSendWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        val apiData = HttpRequest.getSync("message/send", hashMapOf())
        return if (apiData.isOk()) Result.success() else Result.retry()
    }

    companion object {

        private const val MESSAGE_RECEIVER = "MESSAGE_RECEIVER"
        private const val MESSAGE_DATA = "MESSAGE_DATA"

        /**
         *  执行发送消息Worker
         */
        fun send(context: Context, receiverUId: String, msgData: String) {
            val inputData = Data.Builder()
                .putString(MESSAGE_RECEIVER, receiverUId)
                .putString(MESSAGE_DATA, msgData)
                .build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<MessageSendWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(workRequest)
            GlobalScope.launch {
                RoomUntil.db.msgSaveDataDao().insert(
                    MessageSaveData(
                        requestId = workRequest.id.toString(),
                        sendState = 0,
                        msgData = msgData,
                        receiverUid = receiverUId,
                        sendTime = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}