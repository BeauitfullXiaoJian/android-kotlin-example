package com.example.androidx_example.fragments.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.databinding.VideoPopupWindowBinding
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.AnimateUntil

class VideoPopupWindow(parentFragment: BaseFragment, rootView: ViewGroup, video: Video) :
    PopupWindow(parentFragment.context) {
    init {
        // 初始化弹出窗口视图
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        contentView = LayoutInflater.from(parentFragment.context)
            .inflate(R.layout.video_popup_window, rootView, false)

        // 设置弹出动画
        val menuContainer = contentView.findViewById<CardView>(R.id.menu_container)
        AnimateUntil.popup(menuContainer)

        // 设置点击关闭窗口
        contentView.findViewById<View>(R.id.dismiss_container).setOnClickListener {
            AnimateUntil.down(menuContainer, doOnEnd = { dismiss() })
        }
        contentView.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            AnimateUntil.down(menuContainer, doOnEnd = { dismiss() })
            parentFragment.showToast("反馈成功～")
        }

        // 视图数据绑定
        val viewDataBinding = DataBindingUtil.bind<VideoPopupWindowBinding>(contentView)
        viewDataBinding?.video = video

        // 初始化标签列表
        val feedbackRecyclerView = contentView.findViewById<RecyclerView>(R.id.feedback_recycler).apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = FeedbackLabelAdapter()
        }

        parentFragment.createViewModel(HomeViewModel::class.java).apply {
            feedbackLabels.observe(parentFragment, Observer { labels ->
                (feedbackRecyclerView.adapter as FeedbackLabelAdapter).updateList(labels)
            })
        }
    }

    companion object {
        fun createAndShow(parentFragment: BaseFragment, rootView: ViewGroup, video: Video) {
            VideoPopupWindow(parentFragment, rootView, video).showAtLocation(rootView, Gravity.CENTER, 0, 0)
        }
    }

    class FeedbackLabelAdapter : RecyclerView.Adapter<FeedbackLabelAdapter.ViewHolder>() {

        private var labels = listOf<String>()

        fun updateList(labels: List<String>) {
            this.labels = labels
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(labels[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.create(parent)
        }

        override fun getItemCount(): Int {
            return labels.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            init {
                itemView.setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }

            fun bind(label: String) {
                (itemView as Button).text = label
            }

            companion object {
                fun create(host: ViewGroup): ViewHolder {
                    val view = LayoutInflater.from(host.context).inflate(R.layout.video_feedback_label, host, false)
                    return ViewHolder(view)
                }
            }
        }
    }
}