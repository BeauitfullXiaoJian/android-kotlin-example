package com.example.androidx_example.until

import android.view.View
import android.animation.ObjectAnimator
import androidx.core.animation.doOnEnd

// 动画默认执行时间300ms
const val ANIMATION_TIME: Long = 300

/**
 * 给视图加上动画的工具类
 */
class AnimateUntil {
    companion object {

        /**
         * 向上弹出动画
         * @param target View 动画对象
         * @param duration Long 动画时间，默认ANIMATION_TIME
         */
        fun popup(target: View, duration: Long = ANIMATION_TIME, doOnEnd: () -> Unit = fun() {}) {
            target.post {
                target.translationY = target.height.toFloat()
                val animation = ObjectAnimator.ofFloat(target, "translationY", 0f)
                animation.duration = duration
                animation.doOnEnd { doOnEnd() }
                animation.start()
            }
        }

        /**
         * 向下收起动画
         * @param target View 动画对象
         * @param duration Long 动画时间，默认ANIMATION_TIME
         */
        fun down(target: View, duration: Long = ANIMATION_TIME, doOnEnd: () -> Unit = fun() {}) {
            target.post {
                val animation = ObjectAnimator.ofFloat(target, "translationY", target.height.toFloat())
                animation.duration = duration
                animation.doOnEnd { doOnEnd() }
                animation.start()
            }
        }
    }
}