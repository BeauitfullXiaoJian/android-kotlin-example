package com.example.androidx_example.fragments.player

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.*
import com.example.androidx_example.data.Video
import com.example.androidx_example.until.tool.debugInfo
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PlayerCtrlView : RelativeLayout {

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var moveGestureDetector: GestureDetector? = null
    private var displayMode = DisplayMode.DEFAULT
    private var windowManager: WindowManager? = null
    private var isSeeking = false
    private var playUrl = ""
    private val ctrlHideObserver = Observable.timer(3, TimeUnit.SECONDS)
    private var ctrlHideDispose: Disposable? = null

    var appBarLayoutView: CollapsingToolbarLayout? = null
    var ctrlView: View? = null
    var playerView: PlayerView? = null
    var playerImageView: ImageView? = null
    var progressBar: ProgressBar? = null
    var seekBar: SeekBar? = null
    var playTimeText: TextView? = null
    var playBtn: ImageButton? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        val height = if (checkLandScreen(resources)) {
            getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        } else {
            (width / PlayerView.AspectRatio.AR_16_9.value).toInt()
        }

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )

        playerView?.apply {
            maxWidth = width
            maxHeight = height
            measure(width, height)
            // updateViewSize()
        }
        // for (i in 0 until childCount) {
        // getChildAt(i).measure(width, height)
        //  }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val actionPointMinNum = when (displayMode) {
            DisplayMode.DEFAULT -> 3
            DisplayMode.TINY_WINDOW, DisplayMode.IN_ACTIVITY -> 1
        }

        // 每次按下都重新订阅隐藏对象
        if (event.action == MotionEvent.ACTION_DOWN) {
            subHideObs()
        }

        // 横屏才允许拖动&三指以上操作
        if (actionPointMinNum == 3 && !checkLandScreen(resources)) {
            return super.onTouchEvent(event)
        }

        // 每次拖动结束后恢复位置
        if (event.action == MotionEvent.ACTION_UP) {
            fixPlayViewPosition()
        }

        if (event.pointerCount >= actionPointMinNum) {
            scaleGestureDetector?.onTouchEvent(event)
            moveGestureDetector?.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
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
                start: MotionEvent?,
                current: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                when (displayMode) {
                    DisplayMode.TINY_WINDOW -> {
                        val dx = current!!.rawX - start!!.rawX
                        val dy = current.rawY - start.rawY
                        val lp = layoutParams as WindowManager.LayoutParams
                        lp.x = (downPosition.x + dx).toInt()
                        lp.y = (downPosition.y + dy).toInt()
                        windowManager?.updateViewLayout(this@PlayerCtrlView, lp)
                    }
                    DisplayMode.IN_ACTIVITY -> {
                        val dx = current!!.rawX - start!!.rawX
                        val dy = current.rawY - start.rawY
                        x = downPosition.x + dx
                        y = downPosition.y + dy
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
        // 订阅设置播放控件隐藏
        subHideObs()
    }

    /**
     * 准备并播放视频
     */
    fun prepareAndStart(video: Video, recoverySeekValue: Int) {
        if (playerView?.currentPlayState != PlayerView.PlayState.DEFAULT) {
            return
        }
        preparePlayer(video)
        startPlay(recoverySeekValue)
    }

    /**
     * 启动播放器
     */
    fun startPlay(recoverySeekValue: Int) {
        playerView?.startPlay(playUrl, recoverySeekValue)
        // playerView?.startPlay("https://183-134-19-1.ksyungslb.com/upos-sz-mirrorks32u.acgvideo.com/upgcxcode/18/55/3665518/3665518-1-32.flv")
        // playerView?.startPlay("https://www.cool1024.com:8000/flv?video=1.flv", recoverySeekValue)
    }

    /**
     * 恢复播放器
     */
    fun resumePlay() {
        playerView?.resumePlay()
    }

    /**
     * 播放指定视频对象
     */
    fun preparePlayer(video: Video) {
        playUrl = video.videoSourceUrl
        playerView?.apply {
            setStateUpdateListener { state, isLoading ->
                when (state) {
                    PlayerView.PlayState.PLAYING -> setViewToPlaying()
                    PlayerView.PlayState.PLAY_PAUSE -> setViewToPause()
                    else ->{}
                }
                if (isLoading) {
                    setViewToLoading()
                } else {
                    setViewToComplete()
                }
            }
            setBufferUpdateListener { cached, total ->
                if (!isSeeking) {
                    updateCacheSeekBar(cached, total)
                }
            }
            setPlayPositionUpdateListener { position, duration ->
                updatePlaySeekBar(position, duration)
                updatePlayTimeText(position, duration)
            }
        }
        seekBar?.apply {
            progress = 0
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    updatePlayTimeText(progress.toLong(), seekBar!!.max.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isSeeking = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (isSeeking) {
                        playerView?.seekPlay(seekBar!!.progress.toLong())
                    }
                }
            })
        }
        playBtn?.apply {
            isClickable = true
            setOnClickListener {
                playerView?.also { pv ->
                    if (pv.isPlaying) {
                        pv.pausePlay()
                    } else if (pv.currentPlayState == PlayerView.PlayState.PLAY_PAUSE) {
                        pv.startPlay()
                    }
                }
            }
        }
    }

    /**
     * 暂停播放器
     */
    fun pausePlay() {
        playerView?.pausePlay()
    }

    /**
     * 全局小窗口模式
     */
    fun floatInWindow(activity: Activity) {
        windowManager = moveViewTopWindow(this, activity)
        displayMode = DisplayMode.TINY_WINDOW
        when (displayMode) {
            DisplayMode.DEFAULT -> moveViewTopActivity(this, activity)
            DisplayMode.TINY_WINDOW -> {
                removeFromWindow()
                moveViewTopActivity(this, activity)
            }
            DisplayMode.IN_ACTIVITY -> {
                debugInfo("无需切换模式")
            }
        }
    }

    /**
     * 从全局窗口移除
     */
    fun removeFromWindow(): View {
        if (displayMode == DisplayMode.TINY_WINDOW) {
            windowManager?.removeView(this)
        }
        return this
    }

    /**
     * 活动小窗口模式
     */
    fun floatInActivity(activity: Activity) {
        when (displayMode) {
            DisplayMode.DEFAULT -> moveViewTopActivity(this, activity)
            DisplayMode.TINY_WINDOW -> {
                removeFromWindow()
                moveViewTopActivity(this, activity)
            }
            DisplayMode.IN_ACTIVITY -> {
                debugInfo("无需切换模式")
            }
        }
        displayMode = DisplayMode.IN_ACTIVITY
    }

    /**
     * 从活动中移除视图
     */
    fun removeFromActivity(): View {
        if (displayMode == DisplayMode.IN_ACTIVITY) {
            (this.parent as ViewGroup).removeView(this)
        }
        return this
    }

    /**
     * 修复播放视图位置（如果有必要的话）
     */
    private fun fixPlayViewPosition() {
        // TODO 这个方法还没有实现，这里需要根据视图当前的大小位置计算出最佳的显示位置
        playerView?.apply {
            moveViewToPoint(this, getCenterPoint())
//            if (scaleX < 1 || scaleY < 1) {
//                moveViewToPoint(this, getCenterPoint())
//            }
        }
    }

    /**
     * 设置当前所有相关视图为播放状态
     */
    private fun setViewToPlaying() {
        isSeeking = false
        progressBar?.visibility = View.GONE
        playerImageView?.visibility = View.GONE
        playBtn?.isSelected = true
        appBarLayoutView?.also {
            val lp = it.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags = 0
            it.layoutParams = lp
        }
    }

    private fun setViewToPause() {
        debugInfo("视图暂停")
        playBtn?.isSelected = false
        appBarLayoutView?.also {
            val lp = it.layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            it.layoutParams = lp
        }
    }

    private fun setViewToLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun setViewToComplete() {
        progressBar?.visibility = View.GONE
    }

    /**
     * 更新播放器时间显示
     */
    private fun updatePlayTimeText(current: Long, total: Long) {
        playTimeText?.text = String.format(
            "%s:%s",
            formatDuration(current), formatDuration(total)
        )
    }

    /**
     * 更新播放器播放进度显示
     */
    private fun updatePlaySeekBar(current: Long, total: Long) {
        seekBar?.max = total.toInt()
        seekBar?.progress = current.toInt()
    }

    /**
     * 更新缓存进度显示
     */
    private fun updateCacheSeekBar(cached: Long, total: Long) {
        seekBar?.max = total.toInt()
        seekBar?.secondaryProgress = cached.toInt()
    }

    private fun subHideObs() {
        debugInfo("重设。。。。")
        ctrlView?.visibility = View.VISIBLE
        ctrlHideDispose?.dispose()
        ctrlHideDispose = ctrlHideObserver.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ctrlView?.visibility = View.INVISIBLE
            }
    }

    enum class DisplayMode {
        TINY_WINDOW,
        IN_ACTIVITY,
        DEFAULT
    }

    companion object {

        /**
         * 检查是否是横屏
         */
        fun checkLandScreen(resources: Resources): Boolean {
            val configuration = resources.configuration
            return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }

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
                    Point(view.x.toInt(), view.y.toInt())
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
            val layoutParams = FrameLayout.LayoutParams(
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
            val layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 2)
            layoutParams.x = 0
            layoutParams.y = 0
            layoutParams.width = 500
            layoutParams.height = 300
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            window.addView(view, layoutParams)
            return window
        }

        /**
         * 移动视图到指定位置（动画）
         */
        fun moveViewToPoint(view: View, targetPoint: PointF) {

            // X轴方向移动
            val animatorX = ValueAnimator.ofFloat(view.x, targetPoint.x)
            animatorX.duration = 500
            animatorX.addUpdateListener { animation ->
                view.x = animation.animatedValue as Float
            }
            animatorX.start()

            // Y轴方向移动
            val animatorY = ValueAnimator.ofFloat(view.y, targetPoint.y)
            animatorY.duration = 500
            animatorY.addUpdateListener { animation ->
                view.y = animation.animatedValue as Float
            }
            animatorY.start()
        }

        fun formatDuration(duration: Long): String {
            val durationSecondValue = duration / 1000
            val minute = durationSecondValue / 60
            val second = durationSecondValue % 60
            return String.format(
                "%s:%s",
                if (minute > 9) minute else "0$minute",
                if (second > 9) second else "0$second"
            )
        }
    }

}