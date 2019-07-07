package com.example.androidx_example.fragments.player

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabPagerAdapter(fm: FragmentManager, private val videoId: Int) :

    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = listOf("简介", "评论")

    override fun getCount(): Int = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailFragment()
            else -> CommentFragment()
        }
    }
}