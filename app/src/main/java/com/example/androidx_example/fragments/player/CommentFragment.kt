package com.example.androidx_example.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import com.example.httprequest.Pagination
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
        val commentAdapter = CommentAdapter(context!!)
        comment_recycler_view.apply {
            isActivated = false
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        }

        comment_swipe.setColorSchemeResources(R.color.colorPrimary)
        comment_swipe.setOnRefreshListener {
            page.resetPagination()
            viewModel.loadVideoComment(page)
        }
        viewModel.videoComment.observe(this, Observer {
            comment_recycler_view.isActivated = it.isNotEmpty()
            commentAdapter.commentList = it
            commentAdapter.notifyDataSetChanged()
        })
        viewModel.videoCommentIsLoading.observe(this, Observer {
            comment_swipe.isRefreshing = it
        })
        viewModel.loadVideoComment(page)
    }
}