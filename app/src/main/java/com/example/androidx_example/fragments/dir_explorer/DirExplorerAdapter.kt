package com.example.androidx_example.fragments.dir_explorer

import android.view.ViewGroup
import com.example.androidx_example.fragments.DirExplorerViewModel
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter

class DirExplorerAdapter :
    BaseRecyclerAdapter<DirExplorerViewHolder, DirExplorerViewModel.FileItem>() {

    init {
        items = arrayOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirExplorerViewHolder {
        return DirExplorerViewHolder.create(parent)
    }
}