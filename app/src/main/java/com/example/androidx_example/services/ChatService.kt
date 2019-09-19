package com.example.androidx_example.services

import androidx.lifecycle.LifecycleService
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.until.ChatMessageBus
import com.example.androidx_example.until.api.HttpRequest
import com.example.androidx_example.until.tool.debugInfo
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
        HttpRequest.webSocket("", "cool1024") { type, content, ws ->
            if (type == HttpRequest.WebSocketContentType.MESSAGE) {
                ChatMessage.createFromString(content)?.also {
                    ChatMessageBus.postMessage(it)
                }
            }
            mWebSocket = ws
            debugInfo("收到消息", content, type.name)
        }
    }
}