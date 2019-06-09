package com.example.androidx_example.fragments.player


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
    private val viewModel by lazy {
        createViewModel(PlayerViewModel::class.java).apply {
            videoId = args.id
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs()
        initPlayer()
    }

    private fun initTabs() {
        val pagerAdapter = TabPagerAdapter(childFragmentManager)
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun initPlayer() {
        player_ctr_view.playerView = player_view
        player_ctr_view.playerImageView = player_thumb_view
        viewModel.video.observe(this, Observer { video ->
            debugLog(video.videoTitle)
            player_ctr_view.preparePlayer(video)
            player_ctr_view.startPlay()
        })
    }
}
