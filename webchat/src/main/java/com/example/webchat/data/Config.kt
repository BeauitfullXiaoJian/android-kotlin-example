package com.example.webchat.data

data class Config(
    val serverHost: String, // 聊天服务器地址
    val reconnectionTime: Long, // 服务器重连时间间隔（毫秒）
    val uid: String // 当前用户
)