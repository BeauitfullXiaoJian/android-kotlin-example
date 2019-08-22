package com.example.androidx_example.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.data.Pagination
import com.example.androidx_example.data.Video
import com.example.androidx_example.fragments.home.HomeViewModel
import com.example.androidx_example.fragments.home.VideoPagedAdapter
import com.example.androidx_example.until.getWithSaveSuccess
import kotlinx.android.synthetic.main.activity_global_search.*

class GlobalSearchActivity : AppCompatActivity() {

    private var listAdapter: VideoPagedAdapter? = null
    private var viewModel: HomeViewModel? = null
    private var recyclerLayoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_search)
    }

    /**
     * 初始化视图模型
     */
    private fun initViewModel() {
//        viewModel = activityViewModel(HomeViewModel::class.java).also {
//            it.videoRows.observe(this, Observer { videos ->
//                listAdapter?.submitList(videos)
//            })
//        }
    }

    /**
     * 初始化相关视图
     */
    private fun initView() {
//        listAdapter = listAdapter ?: VideoPagedAdapter(this)
//
//        recyclerLayoutManager = GridLayoutManager(this, 2)
//        search_recycler_view.apply {
//            isActivated = true
//            adapter = listAdapter
//            layoutManager = recyclerLayoutManager
//        }
    }

    private fun initSearchView() {
        val searchView = search_toolbar.menu.findItem(R.id.search_item).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun loadLikeData() {
        val page = Pagination()
        getWithSaveSuccess(
            apiName = "videos",
            params = page.pageParams,
            successDo = { res ->
                val pageData = res.getPageData(Video::class.java)
                page.updateTotal(pageData.total)
            })
    }
}
