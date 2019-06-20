package com.example.androidx_example.fragments.home

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.androidx_example.data.Video
import com.example.androidx_example.fragments.BaseFragment

class VideoPagedAdapter(private val parentFragment: BaseFragment) :
    PagedListAdapter<Video, HomeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.create(parent, parentFragment)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video!!, position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem.id == newItem.id
        }
    }
}