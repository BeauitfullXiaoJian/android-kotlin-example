package com.example.androidx_example.fragments.chat

import android.view.ViewGroup
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.entity.MessageSaveData
import com.example.androidx_example.until.adapter.BasePagedListAdapter
import com.example.androidx_example.until.tool.debugInfo

class ChatAdapter(private val uid: String) :
    BasePagedListAdapter<ChatViewHolder, MessageSaveData>() {

    private fun getMessageItem(position: Int): ChatMessage {
        debugInfo("怎么没有这个项", position.toString())
        val item = getItem(position)!!
        return ChatMessage.createFromString(item.msgData)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == 0) ChatViewHolder.createSelfItem(parent)
        else ChatViewHolder.createFriendItem(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getMessageItem(position).fromUid == uid) ChatMessage.MESSAGE_FROM_SELF
        else ChatMessage.MESSAGE_FROM_FRIEND
    }
}