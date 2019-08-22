package com.example.androidx_example.until

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.androidx_example.R


object ViewUntil {

    /**
     * 设置全屏模式
     */
    fun setFullMode(window: Window) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    /**
     * 获取图片默认加载动画-Drawable对象
     */
    fun getAnimationDrawable(context: Context): AnimationDrawable {
        val animationDrawable = ContextCompat.getDrawable(context, R.drawable.bg_loading) as AnimationDrawable
        animationDrawable.start()
        return animationDrawable
    }
}