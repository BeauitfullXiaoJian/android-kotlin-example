package com.example.androidx_example.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.Pagination
import com.example.androidx_example.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_player_tab_comment.*

class CommentFragment : BaseFragment() {

    private val viewModel by lazy {
        fragmentViewModel(PlayerViewModel::class.java, parentFragment!!)
    }
    private val page = Pagination.create(20)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_tab_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CommentAdapter(context!!)
        comment_recycler_view.layoutManager = LinearLayoutManager(context)
        comment_recycler_view.adapter = adapter
        comment_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val count = layoutManager.itemCount
                val nowShowIndex = layoutManager.findLastVisibleItemPosition()
                val offsetNum = 1
                if (count <= (nowShowIndex + offsetNum) && page.canLoadNext) {
                    page.nextPage()
                    viewModel.loadVideoComment(page)
                }
            }
        })
        comment_swipe.setColorSchemeResources(R.color.colorPrimary)
        comment_swipe.setOnRefreshListener { }
        viewModel.videoComment.observe(this, Observer {
            adapter.commentList = it
            adapter.notifyDataSetChanged()
            debugLog("评论加载数${it.size}")
        })
        viewModel.videoCommentIsLoading.observe(this, Observer {
            comment_swipe.isRefreshing = it
        })
        viewModel.loadVideoComment(page)
    }
}