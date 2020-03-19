package com.example.androidx_example.fragments.dir_explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.androidx_example.R
import com.example.androidx_example.databinding.DirExplorerItemViewBinding
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter
import com.example.androidx_example.until.ui.ViewUntil

class DirExplorerViewHolder(
    view: View,
    val binding: DirExplorerItemViewBinding
) : BaseRecyclerAdapter.ViewHolderBinder<DirExplorerViewModel.FileItem>(view) {

    override fun bind(item: DirExplorerViewModel.FileItem) {
        binding.file = item
    }

    companion object {
        fun create(
            parentView: ViewGroup
        ): DirExplorerViewHolder {
            val binding = DataBindingUtil.inflate<DirExplorerItemViewBinding>(
                LayoutInflater.from(parentView.context),
                R.layout.dir_explorer_item_view,
                parentView,
                false
            )
            return DirExplorerViewHolder(binding.root, binding)
        }
    }
}