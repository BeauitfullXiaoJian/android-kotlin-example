package com.example.androidx_example.fragments.player

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.databinding.FragmentPlayerTabDetailBinding
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.tool.shareImage
import kotlinx.android.synthetic.main.fragment_player_tab_detail.*

class DetailFragment : BaseFragment() {

    private val viewModel by lazy {
        fragmentViewModel(PlayerViewModel::class.java, parentFragment!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_tab_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewBinding(view)
        initView()
    }

    private fun initViewBinding(view: View) {
        val viewDataBinding = FragmentPlayerTabDetailBinding.bind(view)
        viewModel.videoDetail.observe(this, Observer {
            viewDataBinding?.video = it.video
            viewDataBinding?.up = it.up
        })
        viewModel.videoRecommend.observe(this, Observer {
            detail_recycler_view.adapter = DetailAdapter(it)
        })
        viewModel.videoDataIsLoading.observe(this, Observer {
            detail_swipe.isRefreshing = it
        })
    }

    private fun initView() {
        // 设置下拉刷新
        detail_swipe.setColorSchemeResources(R.color.colorPrimary)
        detail_swipe.setOnRefreshListener { viewModel.reloadDetailFragmentData() }
        // 设置Recycler LayoutManger
        detail_recycler_view.layoutManager = LinearLayoutManager(context)
        // 设置分享
        btn_share.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
            shareImage(context!!, bitmap, "图片分享到")
        }
    }
}