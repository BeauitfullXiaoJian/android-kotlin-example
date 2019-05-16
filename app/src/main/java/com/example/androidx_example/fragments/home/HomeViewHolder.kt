package com.example.androidx_example.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.Video

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.video_title)

    init {
        view.setOnClickListener {

        }
    }

    fun bind(videoData: Video) {
        val resources = itemView.resources
        val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
        val density = resources.displayMetrics.density
        layoutParams.topMargin = resources.getInteger(R.integer.)
//        params.bottomMargin = params.topMargin
//        params.leftMargin = getResources().getInteger(R.integer.space_sm_num) * density
//        params.rightMargin = params.leftMargin
//        holder.cardView.setLayoutParams(params)
        title.text = videoData.videoTitle
    }

    companion object {
        fun create(parent: ViewGroup): HomeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_item, parent, false)
            return HomeViewHolder(view)
        }
    }
}