package com.example.androidx_example.fragments.player

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.data.VideoDetailInfo

class DetailAdapter(private val videoList: Array<VideoDetailInfo>) : RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}