package com.example.androidx_example.fragments.player


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.androidx_example.R
import kotlinx.android.synthetic.main.fragment_player.*

/**
 * 视频播放
 *
 */
class PlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_play.setOnClickListener {
            // 播放视频
            player_view.startPlay("https://www.cool1024.com:8000/flv?video=1.flv")
        }
    }
}
