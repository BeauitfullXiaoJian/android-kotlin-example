package com.example.androidx_example.fragments.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val msgContent = view.findViewById<TextView>(R.id.chat_content)

    fun bind(msg: ChatMessage) {
        msgContent.text = msg.content
    }

    companion object {
        fun create(parentView: ViewGroup): ChatViewHolder {
            val view = LayoutInflater.from(parentView.context)
                .inflate(R.layout.chat_item_self, parentView, false)
            return ChatViewHolder(view)
        }
    }
}