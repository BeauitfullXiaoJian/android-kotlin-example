package com.example.androidx_example.activity.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.ViewUntil
import com.example.androidx_example.databinding.SearchItemBinding

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleView = itemView.findViewById<TextView>(0)
    private val imgView = itemView.findViewById<ImageView>(0)

    init {

    }

    fun bind(itemData: SearchItemData, position: Int) {

        titleView.text = itemData.text
        GlideApp.with(itemView.context)
            .load(itemData.imgUrl)
            .placeholder(ViewUntil.getAnimationDrawable(itemView.context))
            .into(imgView)
    }


    companion object {
        fun create(parentView: ViewGroup, context: Context): SearchViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.search_item, parentView, false)
            val binding = DataBindingUtil.inflate<SearchItemBinding>(
                LayoutInflater.from(context),
                R.layout.search_item,
                parentView,
                false
            )
            return SearchViewHolder(binding.root)
        }
    }
}