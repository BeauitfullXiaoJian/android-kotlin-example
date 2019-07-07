package com.example.androidx_example.fragments.player

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.VideoDetailInfo
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.tenThousandNumFormat

class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val videoItem = view.findViewById<LinearLayout>(R.id.video_item)
    private val videoThumb = view.findViewById<ImageView>(R.id.flv_item_thumb)
    private val videoTitle = view.findViewById<TextView>(R.id.flv_item_title)
    private val videoUpName = view.findViewById<TextView>(R.id.flv_item_up)
    private val videoBrowserNum = view.findViewById<TextView>(R.id.flv_item_view)
    private val videoCommentNum = view.findViewById<TextView>(R.id.flv_item_comment)

    fun bind(videoData: VideoDetailInfo) {
        val context = itemView.context
        val animationDrawable = ContextCompat.getDrawable(context, R.drawable.bg_loading) as AnimationDrawable
        animationDrawable.start()
        GlideApp.with(context)
            .load("https:${videoData.video.videoThumbUrl}")
            .placeholder(animationDrawable)
            .into(videoThumb)
        videoTitle.text = videoData.video.videoTitle
        videoUpName.text = ""
        videoBrowserNum.text = tenThousandNumFormat(videoData.video.viewNum)
        videoCommentNum.text = tenThousandNumFormat(videoData.video.commentNum)
        videoUpName.text = videoData.up.nickName
        videoItem.setOnClickListener {
            val direction = PlayerFragmentDirections.actionPlayerFragmentSelf(videoData.video.id)
            it.findNavController().navigate(direction)
        }
    }

    companion object {
        fun create(parent: ViewGroup): DetailViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_detail_item, parent, false)
            return DetailViewHolder(view)
        }
    }
}