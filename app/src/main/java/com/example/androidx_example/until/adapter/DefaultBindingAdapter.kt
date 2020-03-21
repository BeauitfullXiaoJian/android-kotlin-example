package com.example.androidx_example.until.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.ui.ViewUntil

object DefaultBindingAdapter {

    @BindingAdapter("glideUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, imageUrl: String?) {
        imageUrl?.run {
            GlideApp.with(imageView.context)
                .load(imageUrl)
                .placeholder(ViewUntil.getAnimationDrawable(imageView.context))
                .into(imageView)
        }
    }

}