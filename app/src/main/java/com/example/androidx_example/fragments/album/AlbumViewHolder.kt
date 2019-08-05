package com.example.androidx_example.fragments.album

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidx_example.R
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.data.PhotoSize
import com.example.androidx_example.until.GlideApp

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imageView = view.findViewById<ImageView>(R.id.photo_view)

    fun bind(photo: PhotoData) {
        // 预先计算出ImageView的大小
        val lp = imageView.layoutParams
        val fitSize = getFitSize(photo.size)
        lp.height = fitSize.height
        lp.width = fitSize.width
        imageView.layoutParams = lp
        GlideApp.with(itemView)
            .load(photo.src)
            .into(imageView)
    }

    companion object {

        var containerViewWidth = 0

        fun create(parentView: ViewGroup): AlbumViewHolder {
            val view = LayoutInflater.from(parentView.context).inflate(R.layout.photo_item, parentView, false)
            return AlbumViewHolder(view)
        }

        fun getFitSize(originSize: PhotoSize): Size {
            val containerSize = Size(containerViewWidth / 2, Int.MAX_VALUE)
            val cwOW = 1f * containerSize.width / originSize.width
            val expW = containerSize.width
            val expH = (originSize.height * cwOW).toInt()
            return if (expH < containerSize.height)
                Size(expW, expH)
            else return Size((containerSize.height * (1f * expW / expH)).toInt(), containerSize.height)
        }
    }
}