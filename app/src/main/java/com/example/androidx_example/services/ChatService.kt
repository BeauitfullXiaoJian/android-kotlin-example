package com.example.androidx_example.services

import androidx.lifecycle.LifecycleService
import com.example.androidx_example.until.api.HttpRequest
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.showToast
import io.reactivex.Observable
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

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
        mWebSocket = HttpRequest.webSocket("", "cool1024") { type, content ->
            if (type == HttpRequest.WebSocketContentType.OPEN_MESSAGE) {
            }
        }
    }
}