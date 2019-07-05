package com.example.androidx_example.fragments.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.data.ChatMessage

class ChatAdapter(private val chatRows: ArrayList<ChatMessage>) : RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return chatRows.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatRows[position])
    }
}