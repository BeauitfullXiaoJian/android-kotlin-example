package com.example.androidx_example.fragments.album

import android.util.Size
import android.view.ViewGroup
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter

class AlbumAdapter(private val containerWidth: Int) :
    BaseRecyclerAdapter<AlbumViewHolder, PhotoData>() {

    init {
        items = arrayOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val containerSize = Size(containerWidth / 2, Int.MAX_VALUE)
        return AlbumViewHolder.create(parent, containerSize, items)
    }
}