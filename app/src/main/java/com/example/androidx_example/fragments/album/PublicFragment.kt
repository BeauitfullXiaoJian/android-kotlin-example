package com.example.androidx_example.fragments.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.androidx_example.R
import com.example.androidx_example.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_public.*


/**
 * 公开广场
 */
class PublicFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_public, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        public_view_pager.adapter = AlbumPagerAdapter(
            childFragmentManager,
            PhotoDataLoader.loadAlbum(resources),
            resources,
            activity!!.packageName
        )
        public_tab.setupWithViewPager(public_view_pager)
    }
}
