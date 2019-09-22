package com.example.webchat.data


/**
 * 聊天消息类
 */
data class ChatMessage(
    val uuid: String, // 消息的唯一编号
    val fromUid: String, // 消息来源人
    val toUid: String // 消息接收人
)