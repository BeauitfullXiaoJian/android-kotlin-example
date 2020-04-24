package com.example.message

import com.google.gson.Gson

class Message(
    var id: Long,
    var workRequestId: String,
    var sendState: Int,
    var fromUid: String,
    var toUid: String,
    var sendTime: Long,
    var receiverTime: Long
) {

    fun success() {
        sendState = MessageState.SEND_SUCCESS.value
    }

    fun error() {
        sendState = MessageState.SEND_ERROR.value
    }

    fun sending() {
        sendState = MessageState.SENDING.value
    }

    fun updateStats(result: Boolean) {
        if (result) success() else error()
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun createFromString(msg: String): Message {
            return Gson().fromJson(msg, Message::class.java)
        }
    }

    /**
     * 消息数据对象
     */
    data class MessageData(
        val fromUid: String, // 发送者
        val toUid: String, // 接收者
        val type: MessageType, // 消息类型
        val content: String // 消息内容
    )

    /**
     * 消息类型
     */
    enum class MessageType {
        IMAGE, // 图片
        AUDIO, // 音频，语音
        VIDEO, // 视频
        TEXT   // 文字内容
    }

    /**
     * 消息状态
     */
    enum class MessageState(val value: Int) {
        SENDING(0),
        SEND_ERROR(-1),
        SEND_SUCCESS(1)
    }
}