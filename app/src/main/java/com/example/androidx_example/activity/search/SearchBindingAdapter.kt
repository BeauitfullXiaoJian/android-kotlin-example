package com.example.androidx_example.activity.search

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidx_example.R
import com.example.androidx_example.until.GlideApp

object SearchBindingAdapter {

    @BindingAdapter("itemThumb")
    @JvmStatic
    fun loadImageItem(imgView: ImageView, imgUrl: String?) {
        imgUrl?.also {
            GlideApp.with(imgView.context)
                .load(it)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(imgView)
        }
    }
}