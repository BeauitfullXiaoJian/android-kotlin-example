package com.example.androidx_example.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.androidx_example.until.tool.debugInfo
import kotlin.math.max

/**
 * 环形进度条
 */
class CircleProgressView : View {

    var maxValue = MIN_VALIDA_MAX_PROGRESS_VALUE
    var currentValue = MIN_VALIDA_PROGRESS_VALUE
        set(value) {
            field = value
            invalidate()
        }
    var borderWidth = OVERFLOW_FIX_VALUE
        set(value) {
            field = max(value, OVERFLOW_FIX_VALUE)
            progressBackgroundPaint = newPaint(progressBackgroundPaint.color, value)
            progressColorPaint = newPaint(progressColorPaint.color, value)
        }

    var progressBackgroundColor: Int = Color.GRAY
        set(value) {
            field = value
            progressBackgroundPaint = newPaint(value, borderWidth)
        }
    var progressColor: Int = Color.RED
        set(value) {
            field = value
            progressColorPaint = newPaint(value, borderWidth)
        }

    private var progressBackgroundPaint = newPaint(progressBackgroundColor, borderWidth)
    private var progressColorPaint = newPaint(progressColor, borderWidth)

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {}

    override fun onDraw(canvas: Canvas) {
        val d = width.toFloat() - borderWidth * 2
        // val r = d / 2
        // canvas.drawCircle(r, r, r, progressPaint)

        // 背景圆环
        canvas.drawArc(
            borderWidth * 2,
            borderWidth * 2,
            d,
            d,
            0f,
            360f,
            false,
            progressBackgroundPaint
        )

        // 进度圆弧
        canvas.drawArc(
            borderWidth * 2,
            borderWidth * 2,
            d,
            d,
            -90f,
            getProgressAngle(currentValue, maxValue),
            false,
            progressColorPaint
        )
    }

    companion object {
        private const val MIN_VALIDA_MAX_PROGRESS_VALUE = 1
        private const val MIN_VALIDA_PROGRESS_VALUE = 0
        private const val OVERFLOW_FIX_VALUE = 1f
        private const val MAX_K_VALUE = 1f
        private const val FULL_ANGLE = 360

        fun newPaint(colorValue: Int, borderWidth: Float): Paint {
            return Paint().apply {
                style = Paint.Style.STROKE
                color = colorValue
                isAntiAlias = true
                strokeWidth = borderWidth
            }
        }

        fun getProgressAngle(currentValue: Int, maxValue: Int): Float {
            val cv = Math.max(currentValue, MIN_VALIDA_PROGRESS_VALUE)
            val cm = Math.max(maxValue, MIN_VALIDA_MAX_PROGRESS_VALUE)
            val k = Math.min(cv / cm.toFloat(), MAX_K_VALUE)
            debugInfo((k * FULL_ANGLE).toString())
            return k * FULL_ANGLE
        }
    }
}
