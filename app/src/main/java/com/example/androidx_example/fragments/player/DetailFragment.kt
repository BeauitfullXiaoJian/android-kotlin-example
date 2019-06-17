package com.example.androidx_example.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.databinding.FragmentPlayerTabDetailBinding
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.fragments.home.HomeViewModel

const val VIDEO_DATA = "VIDEO_DATA"

class DetailFragment : BaseFragment() {

    private val viewModel by lazy {
        createViewModel(PlayerViewModel::class.java).apply {
            videoId = args.id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_tab_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewDataBinding = FragmentPlayerTabDetailBinding.bind(view)
        createViewModel(PlayerViewModel::class.java).video.observe(this, Observer {
            viewDataBinding?.video = it
            debugLog("=======432423424==========")
        })
    }

    companion object {
        fun create(video: Video): DetailFragment {
            return DetailFragment().apply {
                val args = Bundle()
                args.putSerializable(VIDEO_DATA, video)
                arguments = args
            }
        }
    }
}