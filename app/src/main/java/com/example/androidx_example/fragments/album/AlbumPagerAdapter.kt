package com.example.androidx_example.fragments.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.androidx_example.data.AlbumData

class AlbumPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var albums: Array<AlbumData> = arrayOf()

    fun setAlbums(albums: Array<AlbumData>) {
        this@AlbumPagerAdapter.albums = albums
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        val album = albums[position]
        return PhotoPreviewFragment.create(album.path)
    }

    override fun getCount(): Int {
        return albums.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return albums[position].title
    }

}
