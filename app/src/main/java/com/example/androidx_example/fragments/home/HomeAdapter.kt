package com.example.androidx_example.fragments.home

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.androidx_example.data.Video

class HomeAdapter(private val activity: Activity) : ListAdapter<Video, HomeViewHolder>(
    object : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem.id == newItem.id
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.create(parent, activity)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video)
    }
}