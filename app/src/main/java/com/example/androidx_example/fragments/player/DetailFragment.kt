package com.example.androidx_example.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.databinding.FragmentPlayerTabDetailBinding
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.GlideApp
import kotlinx.android.synthetic.main.fragment_player_tab_detail.*

class DetailFragment : BaseFragment() {

    private val viewModel by lazy {
        fragmentViewModel(PlayerViewModel::class.java, parentFragment!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_tab_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewDataBinding = FragmentPlayerTabDetailBinding.bind(view)
        viewModel.videoDetail.observe(this, Observer {
            viewDataBinding?.video = it.video
            viewDataBinding?.up = it.up
            GlideApp.with(view)
                .load(it.up.avatarImageUrl)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(flv_up_avatar)
        })
        viewModel.videoRecommend.observe(this, Observer {
            detail_recycler_view.adapter = DetailAdapter(it)
        })
        viewModel.videoDataIsLoading.observe(this, Observer {
            detail_swipe.isRefreshing = it
        })
        detail_swipe.setColorSchemeResources(R.color.colorPrimary)
        detail_swipe.setOnRefreshListener { viewModel.reloadDetailFragmentData() }
        detail_recycler_view.layoutManager = LinearLayoutManager(context)
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