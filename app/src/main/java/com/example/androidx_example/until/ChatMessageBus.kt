package com.example.androidx_example.until

import com.example.androidx_example.data.ChatMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

object
ChatMessageBus {

    private val busSubject = PublishSubject.create<ChatMessage>()

    fun postMessage(msg: ChatMessage) {
        busSubject.onNext(msg)
    }

    fun obsOnMainThread(msgFun: (msg: ChatMessage) -> Unit): Disposable {
        return busSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            msgFun.invoke(it)
        }
    }
}