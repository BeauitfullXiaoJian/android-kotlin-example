package com.example.androidx_example.fragments.dir_explorer

import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter

class DirExplorerAdapter(private val navCtrl: NavController, private val maxWidth: Int) :
    BaseRecyclerAdapter<DirExplorerViewHolder, DirExplorerViewModel.FileItem>() {

    init {
        items = arrayOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirExplorerViewHolder {
        return DirExplorerViewHolder.create(parent, navCtrl, maxWidth)
    }
}