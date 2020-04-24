package com.example.message

interface MessageSaver {
    fun save(msg: Message)
    fun update(msg: Message)
}