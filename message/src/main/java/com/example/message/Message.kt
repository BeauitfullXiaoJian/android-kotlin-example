package com.example.message

import com.google.gson.Gson

class Message(
    val msgData: MessageData
) {
    var sendTime: Long = 0L
    var receiverTime: Long = 0L
    var sendState: MessageState = MessageState.SENDING

    fun success() {
        sendState = MessageState.SEND_SUCCESS
    }

    fun error() {
        sendState = MessageState.SEND_ERROR
    }

    fun sending() {
        sendState = MessageState.SENDING
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

        fun text(text: String, fromUid: String, toUid: String): Message {
            val data = MessageData(fromUid, toUid, MessageType.TEXT, text)
            return Message(data).apply {
                sendTime = System.currentTimeMillis()
            }
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