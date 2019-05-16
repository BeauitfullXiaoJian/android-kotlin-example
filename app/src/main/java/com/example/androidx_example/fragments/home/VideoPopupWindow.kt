package com.example.androidx_example.fragments.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.androidx_example.R

class VideoPopupWindow(private val context: Context, rootView: ViewGroup) : PopupWindow(context) {
    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView = LayoutInflater.from(context).inflate(R.layout.video_popup_window, rootView, false);
        contentView.findViewById<View>(R.id.dismiss_container).setOnClickListener { dismiss() }
        isFocusable = true
    }
}