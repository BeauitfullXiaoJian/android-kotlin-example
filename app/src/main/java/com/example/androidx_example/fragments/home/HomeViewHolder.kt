package com.example.androidx_example.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.until.GlideApp
import androidx.core.content.ContextCompat
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment

class HomeViewHolder(view: View, private val parent: ViewGroup, private val parentFragment: Fragment) :
    RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.video_title)
    private val thumb: ImageView = view.findViewById(R.id.video_thumb)
    private val label: TextView = view.findViewById(R.id.video_label)
    private val opt: ImageButton = view.findViewById(R.id.btn_opt)

    init {
        view.setOnClickListener {

        }
    }

    fun bind(videoData: Video) {
        val resources = itemView.resources
        val context = itemView.context
        val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
        val density = resources.displayMetrics.density
        layoutParams.topMargin = (resources.getInteger(R.integer.space_sm_value) * density).toInt()
        layoutParams.bottomMargin = layoutParams.topMargin
        layoutParams.leftMargin = layoutParams.bottomMargin
        layoutParams.rightMargin = layoutParams.leftMargin
        itemView.layoutParams = layoutParams
        // 设置标题
        title.text = videoData.videoTitle
        // 设置标签
        label.text = videoData.videoLabel
        // 设置封面图
        val animationDrawable = ContextCompat.getDrawable(context, R.drawable.bg_loading) as AnimationDrawable?
        animationDrawable!!.start()
        GlideApp.with(context)
            .load(videoData.videoThumbUrl)
            .placeholder(animationDrawable)
            .into(thumb)
        // 设置弹出菜单
        opt.setOnClickListener {
            VideoPopupWindow.createAndShow(parentFragment, parent, videoData)
        }
    }

    companion object {
        fun create(parent: ViewGroup, context: Fragment): HomeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_item, parent, false)
            return HomeViewHolder(view, parent, context)
        }
    }
}