package com.example.androidx_example.fragments.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.until.GlideApp

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val msgContent = view.findViewById<TextView>(R.id.chat_content)
    private val msgAvatar = view.findViewById<ImageView>(R.id.chat_avatar)

    fun bind(msg: ChatMessage) {
        msgContent.text = msg.content
        GlideApp.with(itemView)
            .load(R.drawable.ic_avatar_0)
            .circleCrop()
            .into(msgAvatar)
    }

    companion object {
        fun createSelfItem(parentView: ViewGroup): ChatViewHolder {
            val view = LayoutInflater.from(parentView.context)
                .inflate(R.layout.chat_item_self, parentView, false)
            return ChatViewHolder(view)
        }

        fun createFriendItem(parentView: ViewGroup): ChatViewHolder {
            val view = LayoutInflater.from(parentView.context)
                .inflate(R.layout.chat_item_friend, parentView, false)
            return ChatViewHolder(view)
        }
    }
}