package com.example.androidx_example.fragments.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.androidx_example.data.AlbumData
import com.example.androidx_example.until.adapter.BaseViewPagerAdapter

class AlbumPagerAdapter(fm: FragmentManager) : BaseViewPagerAdapter<AlbumData>(fm) {

    init {
        items = arrayOf()
    }

    override fun getItem(position: Int): Fragment {
        val album = items[position]
        return PhotoPreviewFragment.create(album.path)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return items[position].title
    }
}
