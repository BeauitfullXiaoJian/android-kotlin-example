package com.example.androidx_example.data

import com.google.gson.Gson
import kotlin.Exception

data class ChatMessage(
    val fromUid: String,
    val toUid: String,
    val type: MessageType,
    val content: String
) {

    data class MessageData(
        val fromUid: String,
        val toUid: String,
        val type: String,
        val content: String
    )

    companion object {
        const val MESSAGE_FROM_SELF = 0
        const val MESSAGE_FROM_FRIEND = 0
        fun createFromString(msgStr: String): ChatMessage? {
            return try {
                val data = Gson().fromJson(msgStr, MessageData::class.java)
                ChatMessage(
                    data.fromUid,
                    data.toUid,
                    MessageType.valueOf(data.type),
                    data.content
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    enum class MessageType {
        IMAGE, // 图片
        AUDIO, // 音频，语音
        VIDEO, // 视频
        TEXT   // 文字内容
    }
}