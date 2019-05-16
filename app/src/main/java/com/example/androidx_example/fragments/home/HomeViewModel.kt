package com.example.androidx_example.fragments.home

import androidx.lifecycle.*
import com.example.androidx_example.R
import com.example.androidx_example.data.Video

class HomeViewModel : ViewModel() {

    private val videos: MutableLiveData<List<Video>> = MutableLiveData()

    fun getVideos(): LiveData<List<Video>> {
        loadVideos()
        return videos
    }

    private fun loadVideos() {
        val list = ArrayList<Video>();
        list.add(
            Video(
                id = 0,
                videoTitle = "",
                videoSourceUrl = "",
                videoThumbUrl = ""
            )
        );
        videos.value = list
    }
}