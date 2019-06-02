package com.example.androidx_example.fragments.player

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.FrameLayout
import com.example.androidx_example.until.debugInfo

class PlayerCtrlView : FrameLayout {

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var moveGestureDetector: GestureDetector? = null
    var playerView: PlayerView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> (width / PlayerView.AspectRatio.AR_4_3.value).toInt()
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            else -> getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        }
        debugInfo("推荐尺寸${getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)}")
        debugInfo("面板尺寸$width,$height")
        playerView?.apply {
            maxWidth = width
            maxHeight = height
            updateViewSize()
        }
        for (i in 0 until childCount) {
            getChildAt(i).measure(width, height)
        }
        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (event.pointerCount >= 3) {
            scaleGestureDetector?.onTouchEvent(event)
            moveGestureDetector?.onTouchEvent(event)
        }
        return true
    }

    init {
        isFocusable = true
        isClickable = true
        // 播放器缩放
        scaleGestureDetector = ScaleGestureDetector(context, object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                playerView?.apply {
                    scaleX *= detector.scaleFactor
                    scaleY *= detector.scaleFactor
                }
                return true
            }
        })
        // 播放器拖动
        moveGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                playerView?.apply {
                    x -= distanceX
                    y -= distanceY
                }
                return true
            }

        })
        // 播放器全屏
    }

    /**
     * 小窗口模式
     */
    fun tinyWindow(activity: Activity) {
        moveViewTopWindow(this, activity)
    }

    companion object {
        fun moveViewTopActivity(view: View, activity: Activity) {
            (view.parent as ViewGroup).removeView(view)
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            val layoutParams = FrameLayout.LayoutParams(
                500,
                300,
                Gravity.BOTTOM or Gravity.END
            )
            layoutParams.bottomMargin = 20
            layoutParams.rightMargin = 20
            contentView.addView(view, layoutParams)
        }

        fun moveViewTopWindow(view: View, activity: Activity) {
            (view.parent as ViewGroup).removeView(view)
            val window = activity.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams = WindowManager.LayoutParams(
                500,
                300
            )
            layoutParams.x = 0
            layoutParams.y = 0
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            window.addView(view, layoutParams)
            // activity.requestPermissions(listOf(Manifest.permission.SYSTEM_ALERT_WINDOW).toTypedArray(), 0)
        }
    }
}