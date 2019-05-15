package com.example.androidx_example.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidx_example.R

class HomeViewModel : ViewModel() {

    private val menuRows: MutableLiveData<List<MenuItem>> = MutableLiveData()

    fun getMenuRows(): LiveData<List<MenuItem>> {
        loadMenuItem()
        return menuRows
    }

    private fun loadMenuItem() {
        val list = ArrayList<MenuItem>();
        list.add(MenuItem(R.drawable.home_01, "店铺管理"));
        list.add(MenuItem(R.drawable.home_02, "店铺管理"));
        list.add(MenuItem(R.drawable.home_03, "店铺管理"));
        list.add(MenuItem(R.drawable.home_04, "店铺管理"));
        menuRows.value = list
    }
}