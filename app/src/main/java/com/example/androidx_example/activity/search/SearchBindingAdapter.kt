package com.example.androidx_example.activity.search

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.androidx_example.R
import com.example.androidx_example.until.GlideApp

object SearchBindingAdapter {

    @BindingAdapter("itemTitle")
    @JvmStatic
    fun loadTitleItem(textView: TextView, title: String?) {
        title?.also {
            textView.text = it
        }
    }
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