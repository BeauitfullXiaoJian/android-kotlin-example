package com.example.androidx_example.until.adapter

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil


abstract class BasePagedListAdapter<VH : BaseRecyclerAdapter.ViewHolderBinder<ITEM>, ITEM : BasePagedListAdapter.DiffItem> :
    PagedListAdapter<ITEM, VH>(
        object : DiffUtil.ItemCallback<ITEM>() {
            override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean =
                oldItem.id == newItem.id
        }
    ) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    interface DiffItem {
        val id: Long?
    }
}
