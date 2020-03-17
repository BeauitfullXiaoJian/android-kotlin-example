package com.example.androidx_example.fragments.dir_explorer

import android.view.View
import android.view.ViewGroup
import com.example.androidx_example.R
import com.example.androidx_example.fragments.DirExplorerViewModel
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter
import com.example.androidx_example.until.ui.ViewUntil

class DirExplorerViewHolder(
    view: View
) : BaseRecyclerAdapter.ViewHolderBinder<DirExplorerViewModel.FileItem>(view) {

    override fun bind(item: DirExplorerViewModel.FileItem) {
    }

    companion object {
        fun create(
            parentView: ViewGroup
        ): DirExplorerViewHolder {
            val view = ViewUntil.createViewHolderView(parentView, R.layout.photo_item)
            return DirExplorerViewHolder(view)
        }
    }
}