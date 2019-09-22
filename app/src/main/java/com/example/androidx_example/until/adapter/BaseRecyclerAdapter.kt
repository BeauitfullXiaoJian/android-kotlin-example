package com.example.androidx_example.until.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<VH : BaseRecyclerAdapter.ViewHolderBinder<ITEM>, ITEM> :
    RecyclerView.Adapter<VH>() {

    protected lateinit var items: Array<ITEM>

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(items: Array<ITEM>) {
        this@BaseRecyclerAdapter.items = items
        notifyDataSetChanged()
    }

    abstract class ViewHolderBinder<ITEM>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: ITEM)
    }
}