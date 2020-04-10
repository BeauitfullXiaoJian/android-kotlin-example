package com.example.message

interface MessageSender {
    fun send(msg: Message): Boolean
}