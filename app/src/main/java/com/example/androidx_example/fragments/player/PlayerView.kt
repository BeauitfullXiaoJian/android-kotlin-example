package com.example.androidx_example.fragments.player

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import com.example.androidx_example.until.debugInfo
import tv.danmaku.ijk.media.player.IjkMediaPlayer

const val DEFAULT_PLAY_SPEED = 1f

class PlayerView : TextureView {

    private var mPlayer: IjkMediaPlayer? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = true
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                debugInfo("可用")
                createPlayer(surface!!)
            }
        }
    }

    private fun createPlayer(surface: SurfaceTexture) {

        mPlayer = IjkMediaPlayer().apply {
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
            setSpeed(DEFAULT_PLAY_SPEED)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
            setSurface(Surface(surface))
            // 视频准备就绪
            setOnPreparedListener {
                start()
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

    fun startPlay(urlStr: String) {
        mPlayer?.apply {
            dataSource = urlStr
            prepareAsync()
        }
    }
}