package com.example.androidx_example.fragments.album

import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.example.androidx_example.R
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.until.adapter.BaseRecyclerAdapter
import com.example.androidx_example.until.GlideApp
import com.example.androidx_example.until.ui.ViewUntil

class AlbumViewHolder(view: View, val containerSize: Size) :
    BaseRecyclerAdapter.ViewHolderBinder<PhotoData>(view) {

    private val cardView = view.findViewById<CardView>(R.id.photo_card)
    private val imageView = view.findViewById<ImageView>(R.id.photo_view)


    override fun bind(item: PhotoData) {
        val lp = cardView.layoutParams
        val fitSize = ViewUntil.getFitSize(item.size.size, containerSize)
        lp.height = fitSize.height
        cardView.layoutParams = lp
        cardView.setOnClickListener {
            PhotoPopupWindow.createAndShow(item.src, it.context, it as ViewGroup)
        }
        GlideApp.with(itemView)
            .load(item.src)
            .into(imageView)
    }

    companion object {
        fun create(parentView: ViewGroup, containerSize: Size): AlbumViewHolder {
            val view = ViewUntil.createViewHolderView(parentView, R.layout.photo_item)
            return AlbumViewHolder(view, containerSize)
        }
    }
}