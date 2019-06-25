package com.example.androidx_example.until

import android.view.View
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Matrix;
import android.graphics.Point
import android.graphics.PointF
import android.util.Size
import androidx.core.animation.doOnEnd

// 动画默认执行时间300ms
const val ANIMATION_TIME: Long = 300

/**
 * 给视图加上动画的工具类
 */
class AnimateUntil {
    companion object {

        /**
         * 获取矩阵中的位置坐标（x，y)
         */
        fun getMatrixPoint(matrix: Matrix): PointF {
            val mtrValues = FloatArray(9)
            matrix.getValues(mtrValues);
            return PointF(mtrValues[Matrix.MTRANS_X], mtrValues[Matrix.MTRANS_Y])
        }

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

        /**
         * 缩放到指定值
         */
        fun scaleTo(
            target: View,
            targetScaleX: Float,
            targetScaleY: Float,
            duration: Long = ANIMATION_TIME,
            doOnEnd: () -> Unit = fun() {}
        ) {
            val animationX = ObjectAnimator.ofFloat(target, "scaleX", targetScaleX)
            val animationY = ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
            animationX.duration = duration
            animationY.duration = duration
            animationY.doOnEnd { doOnEnd() }
            animationX.start()
            animationY.start()

        }

        /**
         * 缩放尺寸到指定的值
         */
        fun setSizeTo(
            target: View,
            targetSize: Size,
            duration: Long = ANIMATION_TIME,
            doOnEnd: () -> Unit = fun() {}
        ) {
            target.post {
                val width = target.width
                val height = target.height
                val animationW = ValueAnimator.ofFloat(width.toFloat(), targetSize.width.toFloat())
                val animationH = ValueAnimator.ofFloat(height.toFloat(), targetSize.height.toFloat())
                animationW.duration = duration
                animationH.duration = duration
                animationW.addUpdateListener {
                    val lp = target.layoutParams
                    lp.width = (it.animatedValue as Float).toInt()
                    lp.height = (animationH.animatedValue as Float).toInt()
                    target.layoutParams = lp
                }
                animationW.doOnEnd { doOnEnd() }
                animationW.start()
                animationH.start()
            }
        }

        fun scaleSizeTo(
            target: View,
            targetSize: Size,
            duration: Long = ANIMATION_TIME,
            doOnEnd: () -> Unit = fun() {}
        ) {
            scaleTo(
                target = target,
                targetScaleX = 1f * targetSize.width / target.width,
                targetScaleY = 1f * targetSize.height / target.height,
                duration = duration,
                doOnEnd = doOnEnd
            )
        }

        /**
         * 移动到指定位置
         */
        fun moveTo(
            target: View,
            targetPoint: PointF,
            duration: Long = ANIMATION_TIME,
            doOnEnd: () -> Unit = fun() {}
        ) {
            target.post {
                val animationX = ObjectAnimator.ofFloat(
                    target,
                    "translationX",
                    target.translationX,
                    targetPoint.x
                )
                val animationY =
                    ObjectAnimator.ofFloat(
                        target,
                        "translationY",
                        target.translationY,
                        targetPoint.y
                    )
                animationX.duration = duration
                animationY.duration = duration
                animationY.doOnEnd { doOnEnd() }
                animationX.start()
                animationY.start()
            }
        }

        fun setSizeAndToTopStart(
            target: View,
            targetSize: Size,
            offset: Point = Point(0, 0),
            duration: Long = ANIMATION_TIME,
            doOnEnd: () -> Unit = fun() {}
        ) {
            setSizeTo(target, targetSize, duration) {
                moveTo(
                    target,
                    PointF(-target.x + offset.x, -target.y + offset.y),
                    duration,
                    doOnEnd
                )
            }
        }
    }
}