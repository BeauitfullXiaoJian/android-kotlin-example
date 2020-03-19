package com.example.androidx_example.until.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidx_example.until.GlideApp

object DefaultBindingAdapter {

    @BindingAdapter("glideUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, imageUrl: String?) {
        imageUrl?.run {
            GlideApp.with(imageView.context)
                .load(imageUrl)
                .into(imageView)
        }
    }

}