package com.example.androidx_example.services

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.androidx_example.R
import com.example.androidx_example.components.CircleProgressView
import com.example.androidx_example.until.dpToPx
import android.net.wifi.WifiManager
import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.example.androidx_example.until.debugInfo
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class MusicService : LifecycleService() {

    private lateinit var mWifiLock: WifiManager.WifiLock
    private lateinit var mMediaPlayer: MediaPlayer
    private var mStateLiveData = MutableLiveData<PlayState>()
    private var mLoadedLiveData = MutableLiveData<Int>()
    private var mPositionLiveData = MutableLiveData<Int>()
    private var expStartPlay = false
    private var mPlayStatus = PlayState.DEFAULT
        set(value) {
            field = value
            mStateLiveData.postValue(mPlayStatus)
        }
    private var mLoadedData = 0
        set(value) {
            field = value
            mLoadedLiveData.postValue(value)
        }
    private var mPosition = 0
        set(value) {
            field = value
            mPositionLiveData.postValue(value)
        }

    private var canPlay = false
    private val intervalObs by lazy { Observable.interval(1, TimeUnit.SECONDS) }
    private var intervalDisposable: Disposable? = null

    private var mAudioFocusRequest: AudioFocusRequest? = null
    private val mFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        debugInfo("音频焦点变更")
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // 当其他应用申请焦点之后又释放焦点会触发此回调
                resumeMusic()
            }
            AudioManager.AUDIOFOCUS_LOSS ->
                // 长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                // 会触发此回调事件，例如播放QQ音乐，网易云音乐等
                // 通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                pauseMusic()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                pauseMusic()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // 短暂性丢失焦点并作降音处理
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        initAudioFocus()
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
        mWifiLock = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
            .createWifiLock(WifiManager.WIFI_MODE_FULL, "WIFI_MODE_FULL")
        mWifiLock.acquire()
        mMediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mMediaPlayer.setOnBufferingUpdateListener { _, percent ->
            mLoadedData = percent
        }
        mMediaPlayer.setOnCompletionListener {
            mPlayStatus = PlayState.END
        }
        mMediaPlayer.setOnPreparedListener {
            canPlay = true
            if (expStartPlay) {
                mMediaPlayer.start()
                mPlayStatus = PlayState.PLAYING
            }
        }
        intervalDisposable = intervalObs.subscribe {
            debugInfo("我触发了咯")
            if (mMediaPlayer.isPlaying) {
                mPosition = mMediaPlayer.currentPosition
                debugInfo("这个进度", mMediaPlayer.currentPosition.toString())
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return MusicBinder(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer.release()
        mWifiLock.release()
        intervalDisposable?.dispose()
        disposeAudioFocus()
    }

    override fun onLowMemory() {
    }

    /**
     * 设置音频焦点
     */
    private fun initAudioFocus() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setOnAudioFocusChangeListener(mFocusChangeListener).build()
            mAudioFocusRequest?.acceptsDelayedFocusGain()
        } else {
            audioManager.requestAudioFocus(
                mFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    private fun disposeAudioFocus() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioFocusRequest?.also { audioManager.abandonAudioFocusRequest(it) }
        } else {
            audioManager.abandonAudioFocus(mFocusChangeListener)
        }
    }

    private fun playMusic(musicUrl: String) {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
        mMediaPlayer.setDataSource(musicUrl)
        mMediaPlayer.prepareAsync()
        mPlayStatus = PlayState.LOADING
        expStartPlay = true
    }

    private fun pauseMusic() {
        mMediaPlayer.pause()
        mPlayStatus = PlayState.PAUSE
    }

    private fun resumeMusic() {
        mMediaPlayer.start()
        mPlayStatus = PlayState.PLAYING
        initAudioFocus()
    }

    class MusicBinder(private val service: MusicService) : Binder() {

        private var mPlayerView: View? = null

        /**
         * 播放指定链接的音乐
         */
        fun playMusic(musicUrl: String) {
            service.playMusic(musicUrl)
        }

        /**
         * 暂停播放器
         */
        fun pauseMusic() {
            service.pauseMusic()
        }

        /**
         * 恢复播放
         */
        fun resumeMusic() {
            service.resumeMusic()
        }

        /**
         * 获取播放器状态的LiveData
         */
        fun getStateLiveData(): MutableLiveData<PlayState> {
            return service.mStateLiveData
        }

        /**
         * 获取播放器缓存进度LiveData
         */
        fun getLoadedLiveData(): MutableLiveData<Int> {
            return service.mLoadedLiveData
        }

        /**
         * 显示播放器控件
         */
        fun showPlayerView(activity: Activity, dpOffset: Int) {
            val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
            mPlayerView = LayoutInflater.from(activity)
                .inflate(R.layout.music_player, contentView, false).also {
                    contentView.addView(it)
                    val lp = it.layoutParams as FrameLayout.LayoutParams
                    lp.gravity = Gravity.BOTTOM or Gravity.END
                    lp.bottomMargin = dpToPx(dpOffset)
                    it.layoutParams = lp
                    val progressView = it.findViewById<CircleProgressView>(R.id.progress)
                    val playIcon = it.findViewById<ImageView>(R.id.icon)
                    progressView.borderWidth = 5f
                    progressView.progressColor = ContextCompat.getColor(service, R.color.colorPrimary)
                    progressView.progressBackgroundColor = ContextCompat.getColor(service, R.color.colorLightPrimary)
                    progressView.setOnClickListener {
                        if (playIcon.isActivated) pauseMusic() else resumeMusic()
                    }
                    service.mStateLiveData.observe(service, Observer { state ->
                        if (state == PlayState.PLAYING) playIcon.isActivated = true
                        if (state == PlayState.PAUSE) playIcon.isActivated = false
                    })
                    // service.mLoadedLiveData.observe(service, Observer { loaded ->
                    // progressView.currentValue = loaded
                    // progressView.maxValue = 100
                    // })
                    service.mPositionLiveData.observe(service, Observer { position ->
                        progressView.currentValue = position
                        progressView.maxValue = service.mMediaPlayer.duration
                    })
                }
        }

        fun removePlayerView() {
            mPlayerView?.parent.also {
                (it as ViewGroup).removeView(mPlayerView)
            }
            mPlayerView
        }
    }

    enum class PlayState {
        DEFAULT, // 默认状态，创建时就为默认
        LOADING, // 加载音频中（不可播放，还么到可播放的载入量）
        PLAYING, // 播放
        PAUSE,   // 暂停
        END      // 播放结束
    }
}