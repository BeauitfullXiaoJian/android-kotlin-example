package com.example.androidx_example.fragments.player

import android.content.Context
import android.graphics.PointF
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Size
import android.view.Surface
import android.view.TextureView
import com.example.androidx_example.until.debugInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer

const val DEFAULT_PLAY_SPEED = 1f

class PlayerView : TextureView {

    private var mPlayer: IjkMediaPlayer? = null
    private var videoWidth = 1920
    private var videoHeight = 1080
    private var aspectRatio: Float = AspectRatio.AR_16_9.value
    private var sizeMode: SizeMode = SizeMode.AUTO_FIT
    private var fitWidth = 0
    private var fitHeight = 0

    private var playUrl = ""
    private var currentPlayState = PlayState.DEFAULT

    var maxWidth = 0
    var maxHeight = 0

    val isPlaying
        get() = mPlayer?.isPlaying ?: false

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
        debugInfo("创建播放器")
        if (mPlayer == null) {
            mPlayer = IjkMediaPlayer().apply {
                // IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                setSpeed(DEFAULT_PLAY_SPEED)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
                // 视频准备就绪
                setOnPreparedListener {
                    start()
                }
                // 视频尺寸变化
                setOnVideoSizeChangedListener { _, w, h, _, _ ->
                    this@PlayerView.videoWidth = w
                    this@PlayerView.videoHeight = h
                    setPlayViewSize()
                }
                // 视频跳转
                setOnSeekCompleteListener { }
                // 缓冲更新
                setOnBufferingUpdateListener { _, _ -> }
                // 播放器消息
                setOnInfoListener { _, what, _ ->
                    when (what) {
                        IjkMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            // 正在缓冲
                        }
                        IjkMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                            // 缓冲结束
                        }
                    }
                    return@setOnInfoListener false
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
    }

    /**
     * 播放指定视频
     */
    fun startPlay(urlStr: String? = null) {
        createPlayer()
        mPlayer?.apply {
            if (urlStr != null) {
                try {
                    dataSource = urlStr
                    prepareAsync()
                    this@PlayerView.playUrl = urlStr
                    debugInfo("准备播放器")
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            } else {
                start()
            }
        }
    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        mPlayer?.apply {
            if (isPlaying) pause()
        }
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
        layoutParams.width = fitWidth
        layoutParams.height = fitHeight
        layoutParams = layoutParams
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
        // debugInfo("计算前的尺寸：$maxWidth,$maxHeight")
        // debugInfo("计算后的尺寸：${size.width},${size.height}")
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
            // 我们把视频尺寸缩放为容器宽度,高度按视频比例缩放
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
            // 我们把视频尺寸缩放为容器宽度,高度按比指定例缩放
            val expW = containerSize.width
            val expH = (expW / aspectRatio).toInt()
            return if (expH < containerSize.height)
                Size(expW, expH)
            else return Size((containerSize.height * (1f * expW / expH)).toInt(), containerSize.height)
        }
    }

    enum class PlayState {
        DEFAULT,
        PREPARE,
        LOADING_DATA,
        PLAY_PAUSE,
        PLAY_STOP,
        PLAYING
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