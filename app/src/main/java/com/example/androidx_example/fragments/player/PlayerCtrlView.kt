package com.example.androidx_example.fragments.player

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.FrameLayout
import com.example.androidx_example.until.debugInfo

class PlayerCtrlView : FrameLayout {

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var moveGestureDetector: GestureDetector? = null
    private var displayMode = DisplayMode.DEFAULT
    private var windowManager: WindowManager? = null
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
        val actionPointMinNum = when (displayMode) {
            DisplayMode.DEFAULT -> 3
            DisplayMode.TINY_WINDOW, DisplayMode.IN_ACTIVITY -> 1
        }
        if (event.pointerCount >= actionPointMinNum) {
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

            private var downPosition: Point = Point()

            override fun onDown(e: MotionEvent?): Boolean {
                downPosition = getWindowPoint(displayMode, this@PlayerCtrlView)
                return super.onDown(e)
            }

            override fun onScroll(
                start: MotionEvent,
                current: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                when (displayMode) {
                    DisplayMode.TINY_WINDOW -> {
                        val dx = current.rawX - start.rawX
                        val dy = current.rawY - start.rawY
                        val lp = layoutParams as WindowManager.LayoutParams
                        lp.x = (downPosition.x + dx).toInt()
                        lp.y = (downPosition.y + dy).toInt()
                        windowManager?.updateViewLayout(this@PlayerCtrlView, lp)
                    }
                    DisplayMode.IN_ACTIVITY -> {
                        val dx = current.rawX - start.rawX
                        val dy = current.rawY - start.rawY
                        val lp = layoutParams as LayoutParams
                        lp.leftMargin = (downPosition.x + dx).toInt()
                        lp.topMargin = (downPosition.y + dy).toInt()
                        layoutParams = lp
                    }
                    DisplayMode.DEFAULT -> {
                        playerView?.apply {
                            x -= distanceX
                            y -= distanceY
                        }
                    }
                }
                return true
            }
        })
    }

    /**
     * 全局小窗口模式
     */
    fun floatInWindow(activity: Activity) {
        windowManager = moveViewTopWindow(this, activity)
        displayMode = DisplayMode.TINY_WINDOW
    }

    /**
     * 活动小窗口模式
     */
    fun floatInActivity(activity: Activity) {
        moveViewTopActivity(this, activity)
        displayMode = DisplayMode.IN_ACTIVITY
    }

    enum class DisplayMode {
        TINY_WINDOW,
        IN_ACTIVITY,
        DEFAULT,
    }

    companion object {

        /**
         * 根据窗口模式获取坐标
         */
        fun getWindowPoint(mode: DisplayMode, view: View): Point {
            return when (mode) {
                DisplayMode.TINY_WINDOW -> {
                    val lp = view.layoutParams as WindowManager.LayoutParams
                    Point(lp.x, lp.y)
                }
                DisplayMode.IN_ACTIVITY -> {
                    val lp = view.layoutParams as LayoutParams
                    Point(lp.leftMargin, lp.topMargin)
                }
                else -> Point()
            }
        }

        /**
         * 应用内小窗口
         */
        fun moveViewTopActivity(view: View, activity: Activity) {
            (view.parent as ViewGroup).removeView(view)
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            val layoutParams = LayoutParams(
                500,
                300,
                Gravity.BOTTOM or Gravity.END
            )
            layoutParams.bottomMargin = 20
            layoutParams.rightMargin = 20
            contentView.addView(view, layoutParams)
        }

        /**
         * 全局小窗口
         */
        fun moveViewTopWindow(view: View, activity: Activity): WindowManager {
            (view.parent as ViewGroup).removeView(view)
            val window = activity.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_PHONE)
            layoutParams.x = 0
            layoutParams.y = 0
            layoutParams.width = 500
            layoutParams.height = 300
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            window.addView(view, layoutParams)
            return window
        }
    }
}