package com.example.androidx_example.fragments.home

import androidx.lifecycle.*
import com.example.androidx_example.data.Video

class HomeViewModel : ViewModel() {

    private val videos: MutableLiveData<List<Video>> = MutableLiveData()

    fun getVideos(): LiveData<List<Video>> {
        loadVideos()
        return videos
    }

    private fun loadVideos() {
        val list = ArrayList<Video>()
        list.add(
            Video(
                id = 0,
                videoTitle = "3个有才华的妻子，因为思念丈夫而留下深情的诗句",
                videoSourceUrl = "视频地址",
                videoLabel = "趣味科普人文",
                videoThumbUrl = "http://i1.hdslb.com/bfs/archive/0037921aa530e40603dd93545eb165f73c2b0cc5.jpg"
            )
        )
        list.add(
            Video(
                id = 0,
                videoTitle = "“吸猫吸狗”可能会培育超级病毒",
                videoLabel = "趣味科普人文",
                videoSourceUrl = "视频地址",
                videoThumbUrl = "http://i2.hdslb.com/bfs/archive/dbf1cbf84016fc9392cb851fe24384014205406c.jpg"
            )
        )
        videos.value = list
    }
}