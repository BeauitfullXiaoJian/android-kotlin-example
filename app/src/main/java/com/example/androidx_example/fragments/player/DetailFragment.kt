package com.example.androidx_example.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.androidx_example.R
import com.example.androidx_example.databinding.FragmentPlayerTabDetailBinding
import com.example.androidx_example.fragments.BaseFragment

class DetailFragment : BaseFragment() {

    private val viewModel by lazy {
        shareViewModel(PlayerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_tab_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewDataBinding = FragmentPlayerTabDetailBinding.bind(view)
        viewModel.video.observe(this, Observer {
            viewDataBinding?.video = it
        })
    }

//    companion object {
//        fun create(videoId: Int): DetailFragment {
//            return DetailFragment().apply {
//                val args = Bundle()
//                args.putSerializable(VIDEO_ID, videoId)
//                arguments = args
//            }
//        }
//    }
}