package com.example.androidx_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_global_search.*

class GlobalSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_search)
    }

    private fun initSearchView(){
        val searchView = search_toolbar.menu.findItem(R.id.search_item).actionView as SearchView
    }
}
