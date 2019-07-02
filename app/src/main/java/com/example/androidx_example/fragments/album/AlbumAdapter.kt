package com.example.androidx_example.fragments.album

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter : RecyclerView.Adapter<AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind()
    }
}