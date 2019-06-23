package com.example.androidx_example.fragments.player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.VideoComment
import com.example.androidx_example.databinding.VideoCommentItemBinding

class CommentViewHolder(view: View, val binding: VideoCommentItemBinding) : RecyclerView.ViewHolder(view) {

    fun bind(comment: VideoComment, position: Int) {
        binding.comment = comment
        binding.position = position
    }

    companion object {
        fun create(parent: ViewGroup, context: Context): CommentViewHolder {
            val binding = DataBindingUtil.inflate<VideoCommentItemBinding>(
                LayoutInflater.from(context),
                R.layout.video_comment_item,
                parent,
                false
            )
            return CommentViewHolder(binding.root, binding)
        }
    }
}