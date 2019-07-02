package com.example.androidx_example.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.androidx_example.until.getPxFromDpIntegerId
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var listAdapter: VideoPagedAdapter? = null
    private var viewModel: HomeViewModel? = null
    private var recyclerLayoutManager: GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerPosition()
    }

    /**
     * 初始化视图模型
     */
    private fun initViewModel() {
        viewModel = activityViewModel(HomeViewModel::class.java).also {
            it.videoRows.observe(this, Observer { videos ->
                listAdapter?.submitList(videos)
                videos.dataSource.addInvalidatedCallback {
                    home_swipe_refresh?.isRefreshing = false
                }
            })
            it.recyclerPosition.observe(this, Observer { recyclerPositionData ->
                recyclerLayoutManager?.scrollToPositionWithOffset(
                    recyclerPositionData.position,
                    recyclerPositionData.offset
                )
            })
        }
    }

    /**
     * 初始化相关视图
     */
    private fun initView() {
        listAdapter = listAdapter ?: VideoPagedAdapter(this)

        recyclerLayoutManager = GridLayoutManager(context, 2)
        home_recycler.apply {
            isActivated = true
            adapter = listAdapter
            layoutManager = recyclerLayoutManager
        }

        home_swipe_refresh.apply {
            setColorSchemeResources(R.color.colorPrimary)
            setOnRefreshListener {
                listAdapter?.currentList?.apply {
                    dataSource.invalidate()
                }
            }
        }
    }

    /**
     * 保存列表显示的位置数据，以便之后恢复
     */
    private fun saveRecyclerPosition() {
        recyclerLayoutManager?.run {
            val position = findFirstVisibleItemPosition()
            val top = findViewByPosition(position)?.top ?: 0
            val offset = getPxFromDpIntegerId(resources, R.integer.space_sm_value) * 2
            viewModel?.recyclerPosition?.value = RecyclerPositionData(position, top - offset)
        }
    }
}
