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
import androidx.navigation.Navigation
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.getPxFromDpIntegerId

class HomeViewHolder(view: View, private val parent: ViewGroup, private val parentFragment: BaseFragment) :
    RecyclerView.ViewHolder(view) {

    private var id: Int = 0
    private val title: TextView = view.findViewById(R.id.video_title)
    private val thumb: ImageView = view.findViewById(R.id.video_thumb)
    private val label: TextView = view.findViewById(R.id.video_label)
    private val opt: ImageButton = view.findViewById(R.id.btn_opt)

    init {
        view.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToPlayer(id)
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun bind(videoData: Video, position: Int) {
        val resources = itemView.resources
        val context = itemView.context
        val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
        val paddingValue = getPxFromDpIntegerId(resources, R.integer.space_sm_value)
        layoutParams.topMargin = paddingValue
        layoutParams.bottomMargin = paddingValue
        layoutParams.leftMargin = paddingValue
        layoutParams.rightMargin = paddingValue

        // 顶部元素
        if (position in 0..1) {
            layoutParams.topMargin *= 2
        }

        // 左侧元素
        if (position % 2 > 0) {
            layoutParams.rightMargin *= 2
        } else {
            layoutParams.leftMargin *= 2
        }


        itemView.layoutParams = layoutParams
        // 保存视频编号
        id = videoData.id
        // 设置标题
        title.text = videoData.videoTitle
        // 设置标签
        label.text = videoData.videoLabel
        // 设置封面图
        val animationDrawable = ContextCompat.getDrawable(context, R.drawable.bg_loading) as AnimationDrawable
        animationDrawable.start()
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
        fun create(parent: ViewGroup, context: BaseFragment): HomeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_item, parent, false)
            return HomeViewHolder(view, parent, context)
        }
    }
}