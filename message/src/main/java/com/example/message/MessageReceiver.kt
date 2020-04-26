package com.example.message

interface MessageReceiver {
    fun subscribe(message: Message)
}