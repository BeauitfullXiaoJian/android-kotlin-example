package com.example.androidx_example.activity.search

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(val context: Context) : RecyclerView.Adapter<SearchViewHolder>() {

    var items = ArrayList<SearchItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}