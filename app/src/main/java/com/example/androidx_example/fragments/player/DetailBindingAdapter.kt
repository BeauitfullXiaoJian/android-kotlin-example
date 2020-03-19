package com.example.androidx_example.fragments.player

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.androidx_example.R
import com.example.androidx_example.data.VideoComment
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.tool.tenThousandNumFormat

object DetailBindingAdapter {

    @BindingAdapter("avatarUrl")
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
    fun loadReplies(linearLayout: LinearLayout, replies: List<VideoComment>) {
        for (i in 0 until  linearLayout.childCount - 1) {
            linearLayout.removeView(linearLayout.getChildAt(i))
        }
        for (i in 0 until replies.size) {
            val comment = replies[i]
            val textView = TextView(linearLayout.context)
            textView.isClickable = true
            textView.text = createCommentText(comment, linearLayout.context)
            textView.movementMethod = LinkMovementMethod.getInstance()
            linearLayout.addView(textView, linearLayout.childCount - 1)
        }
    }

    @JvmStatic
    fun createCommentText(comment: VideoComment, context: Context): SpannableString {
        val clickableSpan = object : ClickableSpan() {

            override fun onClick(widget: View) {
                val action = PlayerFragmentDirections.actionPlayerFragmentToUserDetailFragment(comment.user)
                widget.findNavController().navigate(action)
            }

            override fun updateDrawState(ds: TextPaint) {
                val color = ContextCompat.getColor(context, R.color.colorPrimary)
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        val spbString = SpannableString("${comment.user.nickName} : ${comment.content}")
        spbString.setSpan(clickableSpan, 0, comment.user.nickName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spbString
    }
}