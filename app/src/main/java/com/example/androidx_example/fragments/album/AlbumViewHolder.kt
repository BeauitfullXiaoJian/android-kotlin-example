package com.example.androidx_example.fragments.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind() {

    }

    companion object {
        fun create(parentView: ViewGroup): AlbumViewHolder {
            val view = LayoutInflater.from(parentView.context).inflate(R.layout.photo_item, parentView, false)
            return AlbumViewHolder(view)
        }
    }
}