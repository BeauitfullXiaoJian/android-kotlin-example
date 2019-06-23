package com.example.androidx_example.fragments.player

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.androidx_example.R
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.tenThousandNumFormat

object DetailBindingAdapter {

    @BindingAdapter("glideUrl")
    @JvmStatic
    fun loadAvatarImage(imageView: ImageView, imageUrl: String?) {
        imageUrl?.run {
            GlideApp.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(imageView)
        }
    }

    @BindingAdapter("numToText", "numLabel")
    @JvmStatic
    fun loadNumToText(textView: TextView, num: Int?, label: String?) {
        val textStr = num?.let {
            val numStr = tenThousandNumFormat(it)
            return@let "$numStr${label ?: ""}"
        } ?: ""
        textView.text = textStr
    }

    @BindingAdapter("numToText")
    @JvmStatic
    fun loadNumToText(textView: TextView, num: Int?) {
        val textStr = num?.let {
            tenThousandNumFormat(it)
        } ?: ""
        textView.text = textStr
    }
}