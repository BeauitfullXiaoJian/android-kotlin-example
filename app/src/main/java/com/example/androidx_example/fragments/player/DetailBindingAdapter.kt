package com.example.androidx_example.fragments.player

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.androidx_example.R
import com.example.androidx_example.data.VideoComment
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

    @BindingAdapter("commentReplies")
    @JvmStatic
    fun loadReplies(linearLayout: LinearLayout, comments: List<VideoComment>) {
        for (i in 0 until comments.size step 4) {
            val comment = comments[i]
            val textView = TextView(linearLayout.context)
            textView.text = createCommentText(comment.user.nickName, comment.content, linearLayout.context)
            linearLayout.addView(textView, linearLayout.childCount - 1)
        }
    }

    @JvmStatic
    fun createCommentText(name: String, content: String, context: Context): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
            }

            override fun updateDrawState(ds: TextPaint) {
                val color = ContextCompat.getColor(context, R.color.colorPrimary)
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        val spbString = SpannableString("$name:$content")
        spbString.setSpan(clickableSpan, 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spbString
    }
}