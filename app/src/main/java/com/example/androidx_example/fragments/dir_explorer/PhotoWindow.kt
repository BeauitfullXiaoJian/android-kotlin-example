package com.example.androidx_example.fragments.dir_explorer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.androidx_example.R
import com.example.androidx_example.until.ui.AnimateUntil
import com.example.androidx_example.until.GlideApp
import com.github.chrisbanes.photoview.PhotoView

class PhotoWindow(
    imgUrl: String,
    rootView: ViewGroup
) :
    PopupWindow() {

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        contentView = LayoutInflater.from(rootView.context).inflate(
            R.layout.dir_explorer_photo_window,
            rootView,
            false
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        AnimateUntil.popup(contentView)
        GlideApp.with(rootView).load(imgUrl).into(contentView as PhotoView)
    }

    companion object {
        fun createAndShow(
            imgUrl: String,
            rootView: ViewGroup
        ) {
            PhotoWindow(imgUrl, rootView)
                .showAtLocation(rootView, Gravity.CENTER, rootView.x.toInt(), rootView.y.toInt())
        }
    }
}