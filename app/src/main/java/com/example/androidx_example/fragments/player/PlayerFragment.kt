package com.example.androidx_example.fragments.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * 视频播放
 *
 */
class PlayerFragment : BaseFragment() {

    private val args by navArgs<PlayerFragmentArgs>()
    private var recoverySeekValue = 0
    private var resumePlayState = PlayerView.PlayState.PLAYING
    private val viewModel by lazy {
        fragmentViewModel(PlayerViewModel::class.java).apply { videoId = args.id }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recoverySeekValue = savedInstanceState?.getInt("progress", 0) ?: 0
        app_toolbar?.also { setNavToolBar(it) }
        initTabs()
        initPlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("progress", play_seek_bar.progress)
    }

    override fun onPause() {
        super.onPause()
        resumePlayState = player_ctr_view.playerView?.currentPlayState ?: PlayerView.PlayState.PLAY_PAUSE
        player_ctr_view?.pausePlay()
    }

    override fun onResume() {
        super.onResume()
        if (resumePlayState == PlayerView.PlayState.PLAYING) {
            player_ctr_view.resumePlay()
        }
    }

    override fun onBackPressed(): Boolean {
        return if (PlayerCtrlView.checkLandScreen(resources)) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            false
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
            true
        }
    }

    private fun initTabs() {
        val pagerAdapter = TabPagerAdapter(childFragmentManager, args.id)
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun initPlayer() {
        player_ctr_view.playerView = player_view
        player_ctr_view.playerImageView = player_thumb_view
        player_ctr_view.progressBar = play_progress_bar
        player_ctr_view.seekBar = play_seek_bar
        player_ctr_view.playTimeText = play_time
        player_ctr_view.playBtn = btn_play
        player_ctr_view.ctrlView = play_control
        player_ctr_view.appBarLayoutView = app_bar_layout
        btn_fullscreen?.setOnClickListener {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        viewModel.video.observe(this, Observer { video ->
            player_ctr_view.prepareAndStart(video, recoverySeekValue)
        })
    }
}
