package com.example.androidx_example.until.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

open class BaseViewPagerAdapter<ITEM>(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    protected lateinit var items: Array<ITEM>

    override fun getItem(position: Int): Fragment {
        return Fragment()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun updateItems(items: Array<ITEM>) {
        this@BaseViewPagerAdapter.items = items
        notifyDataSetChanged()
    }
}