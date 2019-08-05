package com.example.androidx_example.fragments.album

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.data.PhotoData

class AlbumAdapter(private val photos: Array<PhotoData>, private val containerWidth: Int) :
    RecyclerView.Adapter<AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        AlbumViewHolder.containerViewWidth = containerWidth
        return AlbumViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(photos[position])
    }
}