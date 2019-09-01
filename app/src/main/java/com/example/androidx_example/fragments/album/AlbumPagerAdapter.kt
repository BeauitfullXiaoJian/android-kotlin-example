package com.example.androidx_example.fragments.album

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.androidx_example.data.AlbumData

class AlbumPagerAdapter(
    fm: FragmentManager,
    private val albums: Array<AlbumData>,
    private val resources: Resources,
    private val packageName: String
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val album = albums[position]
        return PhotoPreviewFragment.create(PhotoDataLoader.loadPhoto(album.path, resources, packageName))
    }

    override fun getCount(): Int {
        return albums.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return albums[position].title
    }
}
