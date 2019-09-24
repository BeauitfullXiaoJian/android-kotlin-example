package com.example.androidx_example.works

import android.content.Context
import androidx.work.*
import com.example.androidx_example.entity.MessageSaveData
import com.example.androidx_example.until.sql.RoomUntil
import com.example.httprequest.Request
import kotlin.Exception

/**
 *  聊天消息发送Worker
 */
class MessageSendWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        val msgReceiverUid = inputData.getString(MESSAGE_RECEIVER)
        val msgData = inputData.getString(MESSAGE_DATA)
        return try {
            val log = trySaveMsgLog(id.toString(), msgReceiverUid!!, msgData!!)
            log?.let {

                val apiData = Request.getSync(
                    "message/send", hashMapOf(
                        "to" to it.receiverUid,
                        "content" to it.msgData
                    )
                )
                if (apiData.isOk()) {
                    updateMsgToSuccess(it)
                    Result.success()
                } else {
                    updateMsgToError(it)
                    Result.failure()
                }
            } ?: Result.failure() // SQL执行失败，没有保存到聊天记录
        } catch (e: Exception) {
            e.printStackTrace()
            // 异常错退出，重新尝试
            Result.retry()
        }
    }

    enum class MessageState(val value: Int) {
        SENDING(0),
        SEND_ERROR(-1),
        SEND_SUCCESS(1)
    }

    companion object {

        private const val MESSAGE_RECEIVER = "MESSAGE_RECEIVER"
        private const val MESSAGE_DATA = "MESSAGE_DATA"

        /**
         *  执行发送消息Worker
         */
        fun send(context: Context, receiverUid: String, msgData: String) {
            val inputData = Data.Builder()
                .putString(MESSAGE_RECEIVER, receiverUid)
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
        }

        private fun trySaveMsgLog(
            requestId: String,
            receiverUid: String,
            msgData: String
        ): MessageSaveData? {
            return RoomUntil.db.msgSaveDataDao().findMsgByRequestId(requestId) ?: insertMsg(
                requestId,
                msgData,
                receiverUid
            )
        }

        private fun insertMsg(
            requestId: String,
            msgData: String,
            receiverUid: String
        ): MessageSaveData? {
            val msg = MessageSaveData(
                requestId = requestId,
                sendState = MessageState.SENDING.value,
                msgData = msgData,
                receiverUid = receiverUid,
                sendTime = System.currentTimeMillis()
            )
            val insertId = RoomUntil.db.msgSaveDataDao().insert(msg)
            return if (insertId > 0) msg.copy(id = insertId) else null
        }

        private fun updateMsgToSuccess(data: MessageSaveData) {
            RoomUntil.db.msgSaveDataDao()
                .update(data.copy(sendState = MessageState.SEND_SUCCESS.value))
        }

        private fun updateMsgToError(data: MessageSaveData) {
            RoomUntil.db.msgSaveDataDao()
                .update(data.copy(sendState = MessageState.SEND_ERROR.value))
        }
    }
}