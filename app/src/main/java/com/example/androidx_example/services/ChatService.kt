package com.example.androidx_example.services

import androidx.lifecycle.LifecycleService
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.entity.MessageSaveData
import com.example.androidx_example.until.ChatMessageBus
import com.example.androidx_example.until.sql.RoomUntil
import com.example.androidx_example.until.tool.debugInfo
import com.example.androidx_example.works.MessageSendWorker
import com.example.httprequest.HttpRequest
import com.example.httprequest.Request
import com.google.gson.Gson
import okhttp3.WebSocket

class ChatService : LifecycleService() {

    private lateinit var mWebSocket: WebSocket

    override fun onCreate() {
        super.onCreate()
        createWebSocketClient()
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebSocket.cancel()
    }

    private fun createWebSocketClient() {
        Request.webSocket("", "cool1024") { type, content, ws ->
            if (type == HttpRequest.WebSocketContentType.MESSAGE) {
                ChatMessage.createFromString(content)?.also {
                    ChatMessageBus.postMessage(it)
                    this.trySaveMsgLog(it.toUid, Gson().toJson(it))
                }
            }
            mWebSocket = ws
            debugInfo("收到消息", content, type.name)
        }
    }

    private fun trySaveMsgLog(
        receiverUid: String,
        msgData: String
    ) {
        Thread {
            if (receiverUid == "cool1024") {
                val msg = MessageSaveData(
                    requestId = "",
                    sendState = MessageSendWorker.MessageState.SENDING.value,
                    msgData = msgData,
                    receiverUid = receiverUid,
                    sendTime = System.currentTimeMillis()
                )
                RoomUntil.db.msgSaveDataDao().insert(msg)
                debugInfo("保存消息")
            }
        }.start()
    }
}