package com.example.androidx_example.fragments.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.data.ChatMessage

class ChatAdapter(private val chatRows: ArrayList<ChatMessage>, private val uid: String) :
    RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == 0) ChatViewHolder.createSelfItem(parent)
        else ChatViewHolder.createFriendItem(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatRows[position].fromUid == uid) ChatMessage.MESSAGE_FROM_SELF
        else ChatMessage.MESSAGE_FROM_FRIEND
    }

    override fun getItemCount(): Int {
        return chatRows.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatRows[position])
    }
}