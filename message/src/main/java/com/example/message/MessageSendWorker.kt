package com.example.message

import android.content.Context
import androidx.work.*
import kotlin.Exception

/**
 *  消息发送工作对象
 */
class MessageSendWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val sender: MessageSender,
    private val saver: MessageSaver
) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        return try {
            val msgData = inputData.getString(MESSAGE_ARG_NAME)
            val msg = Message.createFromString(msgData!!)
            // 保存消息记录
            saver.save(msg)
            // 发送消息
            val result = sender.send(msg)
            // 变更消息状态，并保存
            msg.updateStats(result)
            saver.update(msg)
            if (result) Result.success() else Result.failure()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {

        private const val MESSAGE_ARG_NAME = "MESSAGE"

        /**
         *  发送消息
         */
        fun send(context: Context, msg: Message) {
            val inputData = Data.Builder()
                .putString(MESSAGE_ARG_NAME, msg.toString())
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
        }
    }
}