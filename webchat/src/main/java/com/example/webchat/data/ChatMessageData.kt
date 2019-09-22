package com.example.webchat.data

/**
 * 聊天消息内容类
 */
data class ChatMessageData(
    val content: String, // 消息实际内容
    val type: MessageType // 消息类型
) {
    enum class MessageType {
        TEXT, // 文本
        IMAGE, // 图片
        VIDEO, // 视频
        SOUND // 声音
    }
}