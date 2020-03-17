package com.example.androidx_example.fragments.player

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.data.VideoComment

class CommentAdapter(val context: Context) : RecyclerView.Adapter<CommentViewHolder>() {

    var commentList = arrayOf<VideoComment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position], position)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}