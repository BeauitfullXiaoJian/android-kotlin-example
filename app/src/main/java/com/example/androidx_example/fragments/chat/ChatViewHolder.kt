package com.example.androidx_example.fragments.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.androidx_example.R
import com.example.androidx_example.data.ChatMessage
import com.example.androidx_example.fragments.album.PhotoPopupWindow
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.tool.debugInfo

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val msgContent = view.findViewById<TextView>(R.id.chat_content)
    private val msgAvatar = view.findViewById<ImageView>(R.id.chat_avatar)
    private val msgImg = view.findViewById<ImageView>(R.id.chat_image)

    fun bind(msg: ChatMessage) {
        when (msg.type) {
            ChatMessage.MessageType.TEXT -> {
                msgContent.visibility = View.VISIBLE
                msgContent.text = msg.content
            }
            ChatMessage.MessageType.IMAGE -> {
                msgImg.visibility = View.VISIBLE
                msgImg.setOnClickListener {
                    PhotoPopupWindow.createAndShow(
                        msg.content,
                        itemView.context,
                        itemView as ViewGroup
                    )
                }
                GlideApp.with(itemView).load(msg.content)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(1000)
                    .into(msgImg)
            }
            else -> debugInfo("不支持的消息类型")
        }
        GlideApp.with(itemView).load(R.drawable.ic_avatar_0).circleCrop().into(msgAvatar)
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