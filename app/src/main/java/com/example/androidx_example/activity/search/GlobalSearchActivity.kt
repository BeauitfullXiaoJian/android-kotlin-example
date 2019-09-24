package com.example.androidx_example.activity.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.until.tool.getWithSaveSuccess
import com.example.httprequest.Pagination
import kotlinx.android.synthetic.main.activity_global_search.*

class GlobalSearchActivity : AppCompatActivity() {

    private lateinit var listAdapter: SearchAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_search)
        initSearchView()
        initSearchResultView()
    }

    /**
     * 初始化相关视图
     */
    private fun initSearchResultView() {
        listAdapter = SearchAdapter(this)
        layoutManager = GridLayoutManager(this, 1)
        search_recycler_view.apply {
            isActivated = true
            adapter = listAdapter
            layoutManager = this@GlobalSearchActivity.layoutManager
        }
    }

    private fun initSearchView() {
        (search_toolbar.menu.findItem(R.id.search_item).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    loadLikeData()
                    return true
                }
            })
        }
    }

    private fun loadLikeData() {
        val page = Pagination()
        getWithSaveSuccess(
            apiName = "videos",
            params = page.pageParams,
            successDo = { res ->
                val pageData = res.getPageData(Video::class.java)
                page.updateTotal(pageData.total)
            }
        )
    }
}
