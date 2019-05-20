package com.example.androidx_example.fragments.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.databinding.VideoPopupWindowBinding
import com.example.androidx_example.until.AnimateUntil


class VideoPopupWindow(parentFragment: Fragment, rootView: ViewGroup, video: Video) :
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
        contentView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            AnimateUntil.down(menuContainer, doOnEnd = { dismiss() })
        }

        // 添加反馈标签
        val gridLayout = contentView.findViewById<GridLayout>(R.id.feedback_label_grid)
//        gridLayout.addView(createLabelBtn("违规", parentFragment.context!!, gridLayout))
//        gridLayout.addView(createLabelBtn("违规", parentFragment.context!!, gridLayout))
//        gridLayout.addView(createLabelBtn("违规", parentFragment.context!!, gridLayout))
        gridLayout.addView(createLabelBtn("违规", parentFragment.context!!, gridLayout))
        // 视图数据绑定
        val viewDataBinding = DataBindingUtil.bind<VideoPopupWindowBinding>(contentView)
        viewDataBinding?.video = video
    }

    companion object {
        fun createAndShow(parentFragment: Fragment, rootView: ViewGroup, video: Video) {
            VideoPopupWindow(parentFragment, rootView, video).showAtLocation(rootView, Gravity.CENTER, 0, 0)
        }

        private fun createLabelBtn(title: String, context: Context, parentView: ViewGroup): Button {
            val labelBtn = LayoutInflater.from(context)
                .inflate(R.layout.video_feedback_label, null) as Button
            return labelBtn.apply {
                text = title
                background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_outline_white_click
                )
            }
        }
    }
}