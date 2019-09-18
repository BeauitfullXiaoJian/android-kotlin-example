package com.example.androidx_example.until

import android.content.Context
import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.until.ui.NotifyUntil
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

    fun registerNotify(appContext: Context) {
        obsOnMainThread {
            NotifyUntil.showSimpleNotify(
                appContext = appContext,
                notifyName = "聊天消息",
                title = "新消息",
                content = it.content,
                descriptionText = "聊天消息",
                iconId = R.mipmap.ic_launcher
            )
        }
    }
}