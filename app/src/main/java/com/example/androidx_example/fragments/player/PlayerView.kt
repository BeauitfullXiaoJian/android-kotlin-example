package com.example.androidx_example.fragments.player

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.RelativeLayout
import com.example.androidx_example.until.tool.debugInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.concurrent.TimeUnit

class PlayerView : TextureView {

    // 播放器对象
    var mPlayer: IjkMediaPlayer? = null
        private set

    /**
     * 尺寸计算相关变量
     */
    private var videoWidth = 0
    private var videoHeight = 0
    private var aspectRatio: Float = AspectRatio.AR_16_9.value
    private var sizeMode: SizeMode = SizeMode.AUTO_FIT
    private var fitWidth = 0
    private var fitHeight = 0
    var maxWidth = 0
    var maxHeight = 0
    var recoverySeekValue = 0

    /**
     * 播放器状态相关变量
     */
    val isPlaying get() = mPlayer?.isPlaying ?: false
    private var stateChangeFun: ((state: PlayState, isLoading: Boolean) -> Unit)? = null
    private var bufferChangeFun: ((cached: Long, total: Long) -> Unit)? = null
    private var positionChangeFunc: ((position: Long, total: Long) -> Unit)? = null
    private var playUrl = ""

    // 定时器，观察对象，订阅对象
    private val intervalObs by lazy { Observable.interval(1, TimeUnit.SECONDS) }
    private var intervalDisposable: Disposable? = null

    var currentPlayState = PlayState.DEFAULT
        private set(value) {
            field = value
            sendPlayerStateChange()
        }
    private var isLoadingState = false
        set (value) {
            field = value
            sendPlayerStateChange()
        }
    private var savePlayState = PlayState.DEFAULT

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(fitWidth, fitHeight)
    }

    init {
        surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                debugInfo("修改了")
                releasePlayer()
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                createPlayer()
                mPlayer?.setSurface(Surface(surface))
            }
        }
    }

    /**
     * 创建播放器
     */
    private fun createPlayer() {
        if (mPlayer == null) {
            mPlayer = IjkMediaPlayer().apply {
                // IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                setSpeed(PlaySpeed.DEFAULT_PLAY_SPEED.value)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
                // 视频准备就绪
                setOnPreparedListener {
                    debugInfo("视频状态-准备就绪")
                    seekTo(recoverySeekValue.toLong())
                    start()
                    this@PlayerView.currentPlayState = PlayState.PLAYING
                    this@PlayerView.savePlayState = this@PlayerView.currentPlayState
                    this@PlayerView.isLoadingState = false
                }
                // 视频尺寸变化
                setOnVideoSizeChangedListener { _, w, h, _, _ ->
                    this@PlayerView.videoWidth = w
                    this@PlayerView.videoHeight = h
                    setPlayViewSize()
                }
                // 视频跳转
                setOnSeekCompleteListener {
                    currentPlayState = savePlayState
                    isLoadingState = false
                }
                // 缓冲更新
                setOnBufferingUpdateListener { player, cached ->
                    bufferChangeFun?.invoke(
                        player.currentPosition + cached,
                        player.duration
                    )
                }
                // 播放器消息
                setOnInfoListener { _, what, _ ->
                    debugInfo("视频状态$what")
                    when (what) {
                        IjkMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            // 正在缓冲
                            currentPlayState = PlayState.LOADING_DATA
                            isLoadingState = true
                        }
                        IjkMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                            // 缓冲结束
                            currentPlayState = savePlayState
                            isLoadingState = false
                        }
                    }
                    return@setOnInfoListener false
                }
            }
            intervalDisposable = intervalObs
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (isPlaying) {
                        positionChangeFunc?.invoke(mPlayer!!.currentPosition, mPlayer!!.duration)
                    }
                }
        }
    }

    /**
     * 视图处于区域中心
     */
    private fun fitVideoCenter() {
        val p = getCenterPoint()
        x = p.x
        y = p.y
    }

    /**
     * 释放播放器
     */
    private fun releasePlayer() {
        mPlayer?.apply {
            reset()
            release()
        }
        mPlayer = null
        intervalDisposable?.dispose()
        debugInfo("销毁播放器")
    }

    /**
     * 更新播放器状态
     */
    private fun sendPlayerStateChange() {
        stateChangeFun?.invoke(currentPlayState, isLoadingState)
    }

    /**
     * 播放指定视频
     */
    fun startPlay(urlStr: String? = null, seekValue: Int = 0) {
        createPlayer()
        mPlayer?.apply {
            if (urlStr != null) {
                try {
                    dataSource = urlStr
                    recoverySeekValue = seekValue
                    prepareAsync()
                    this@PlayerView.currentPlayState = PlayState.PREPARE
                    this@PlayerView.savePlayState = PlayState.PLAYING
                    this@PlayerView.playUrl = urlStr
                    debugInfo("准备播放器$seekValue")
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            } else {
                start()
                this@PlayerView.currentPlayState = PlayState.PLAYING
                this@PlayerView.savePlayState = PlayState.PLAYING
            }
        }
    }

    /**
     * 恢复播放
     */
    fun resumePlay(){
        mPlayer?.apply {
            start()
            currentPlayState = PlayState.PLAYING
            savePlayState = PlayState.PLAYING
        }
    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        mPlayer?.apply {
            pause()
            currentPlayState = PlayState.PLAY_PAUSE
            savePlayState = PlayState.PLAY_PAUSE
        }
    }

    /**
     * 跳转播放
     */
    fun seekPlay(position: Long) {
        mPlayer?.also {
            it.seekTo(position)
            if (currentPlayState in listOf(PlayState.PLAY_PAUSE, PlayState.PLAYING)) {
                savePlayState = currentPlayState
            }
            currentPlayState = PlayState.LOADING_DATA
            isLoadingState = true
        }
    }


    /**
     * 设置状态变更监听方法
     */
    fun setStateUpdateListener(listener: (state: PlayState, isLoading: Boolean) -> Unit) {
        stateChangeFun = listener
    }

    /**
     * 设置缓冲变更监听方法
     */
    fun setBufferUpdateListener(listener: (cached: Long, total: Long) -> Unit) {
        bufferChangeFun = listener
    }

    /**
     * 设置播放进度变更监听方法
     */
    fun setPlayPositionUpdateListener(listener: (position: Long, duration: Long) -> Unit) {
        positionChangeFunc = listener
    }

    /**
     * 更新视图尺寸模式
     */
    fun updatePlayViewSizeMode(mode: SizeMode, aspectRatio: Float = AspectRatio.AR_16_9.value) {
        sizeMode = mode
        this.aspectRatio = aspectRatio
        updateViewSize()
    }

    /**
     * 更新视频尺寸
     */
    fun updateViewSize() {
        scaleX = 1f
        scaleY = 1f
        setPlayViewSize()
    }

    /**
     * 获取视频视图中心坐标
     */
    fun getCenterPoint(): PointF {
        return PointF((maxWidth - fitWidth) / 2f, (maxHeight - fitHeight) / 2f)
    }

    fun getPoint(): PointF {
        val mtrValues = FloatArray(9)
        matrix.getValues(mtrValues)
        val x = mtrValues[Matrix.MTRANS_X]
        val y = mtrValues[Matrix.MTRANS_Y]
        return PointF(x, y)
    }

    /**
     * 设置当前视频区域尺寸
     */
    private fun setPlayViewSize() {
        if (maxWidth <= 0 || maxHeight <= 0) {
            return
        }
        when (sizeMode) {
            SizeMode.AUTO_FIT -> fitVideoSizeCenter(Size(videoWidth, videoHeight))
            SizeMode.FIT_ASPECT_RATIO -> fitAspectRatioCenter(aspectRatio)
        }
    }

    /**
     * 视图适应视频尺寸
     */
    private fun fitVideoSize(videoSize: Size) {
        if (videoSize.width <= 0 || videoSize.height <= 0) {
            return
        }
        val size = getScaleFitSize(Size(maxWidth, maxHeight), videoSize)
        fitWidth = size.width
        fitHeight = size.height
        val lp = layoutParams as RelativeLayout.LayoutParams
        lp.width = fitWidth
        lp.height = fitHeight
        layoutParams = lp
        debugInfo("尺寸计算===============")
        debugInfo("计算前的尺寸：$maxWidth,$maxHeight")
        debugInfo("合适的视频尺寸：$fitWidth,$fitHeight")
        debugInfo("比例：${1.0 * fitWidth / fitHeight}")
        debugInfo("视图真实尺寸：$width,$height")
    }

    /**
     * 视图适应视频尺寸
     */
    private fun fitVideoSizeCenter(videoSize: Size) {
        fitVideoSize(videoSize)
        fitVideoCenter()
    }

    /**
     * 视图强制比例显示，并让视图处于区域中心
     */
    private fun fitAspectRatio(aspectRatio: Float) {
        val size = getScaleFitAspectRatio(Size(maxWidth, maxHeight), aspectRatio)
        fitWidth = size.width
        fitHeight = size.height
        layoutParams.width = fitWidth
        layoutParams.height = fitHeight
        layoutParams = layoutParams
    }

    /**
     * 视图强制比例显示，并让视图处于区域中心
     */
    private fun fitAspectRatioCenter(aspectRatio: Float) {
        fitAspectRatio(aspectRatio)
        fitVideoCenter()
    }

    companion object {

        /**
         * 计算一个适合面板的缩放尺寸
         */
        fun getScaleFitSize(containerSize: Size, originSize: Size): Size {
            // 我们把视频尺寸缩放为适合容器的尺寸,高度按视频比例缩放
            val cwOW = 1f * containerSize.width / originSize.width
            val expW = containerSize.width
            val expH = (originSize.height * cwOW).toInt()
            return if (expH < containerSize.height)
                Size(expW, expH)
            else return Size((containerSize.height * (1f * expW / expH)).toInt(), containerSize.height)
        }

        /**
         * 按固定比例计算缩放尺寸
         */
        fun getScaleFitAspectRatio(containerSize: Size, aspectRatio: Float): Size {
            // 我们把视频尺寸缩放为容器的尺寸,高度按指定比例缩放
            val expW = containerSize.width
            val expH = (expW / aspectRatio).toInt()
            return if (expH < containerSize.height)
                Size(expW, expH)
            else return Size((containerSize.height * (1f * expW / expH)).toInt(), containerSize.height)
        }
    }

    enum class PlaySpeed(val value: Float) {
        DEFAULT_PLAY_SPEED(1f),
        X_0_5_PLAY_SPEED(0.5f),
        X_1_5_PLAY_SPEED(1.5f),
        X_2_PLAY_SPEED(2f)
    }

    enum class PlayState {
        DEFAULT, // 默认状态
        PREPARE, // 创建播放器
        LOADING_DATA, // 加载视频数据
        PLAY_PAUSE, // 暂停播放
        PLAY_STOP, // 停止播放
        PLAYING // 正在播放
    }

    enum class SizeMode {
        AUTO_FIT,
        FIT_ASPECT_RATIO
    }

    enum class AspectRatio(var value: Float) {
        AR_1_1(1f),
        AR_16_9(16.0f / 9.0f),
        AR_4_3(4.0f / 3.0f)
    }
}