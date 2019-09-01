package com.example.androidx_example.fragments.album

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.androidx_example.R
import com.example.androidx_example.until.AnimateUntil
import com.example.androidx_example.until.GlideApp
import com.github.chrisbanes.photoview.PhotoView

class PhotoPopupWindow(photoUrl: String, context: Context, rootView: ViewGroup) : PopupWindow() {

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        contentView = LayoutInflater.from(context).inflate(
            R.layout.photo_popup_window,
            rootView,
            false
        )
        setBackgroundDrawable(
            ColorDrawable().apply {
                color = Color.BLACK
                alpha = 240
            })
        GlideApp.with(context).load(photoUrl).into(contentView as PhotoView)
        AnimateUntil.popup(contentView)
    }

    companion object {
        fun createAndShow(photoUrl: String, context: Context, rootView: ViewGroup) {
            PhotoPopupWindow(photoUrl, context, rootView)
                .showAtLocation(rootView, Gravity.CENTER, 0, 0)
        }
    }
}