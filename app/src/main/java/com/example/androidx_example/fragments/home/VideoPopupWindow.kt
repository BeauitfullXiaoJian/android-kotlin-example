package com.example.androidx_example.fragments.home

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.androidx_example.R

class VideoPopupWindow(private val activity: Activity, rootView: ViewGroup) : PopupWindow(activity) {
    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView = LayoutInflater.from(activity).inflate(R.layout.video_popup_window, rootView, false);
        contentView.findViewById<View>(R.id.dismiss_container).setOnClickListener { dismiss() }
        isFocusable = true
        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
            .create(HomeViewModel::class.java)
        val viewDataBinding = DataBindingUtil.bind<ViewDataBinding>(contentView)
    }
}