package com.example.androidx_example.fragments.home

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.androidx_example.data.Video
import com.example.androidx_example.data.VideoDataSourceFactory

class HomeViewModel : ViewModel() {

    val videoRows by lazy { getVideoRowsLiveData() }

    val recyclerPosition by lazy { getRecyclerViewPosition() }

    private val _videos: MutableLiveData<List<Video>>by lazy {
        MutableLiveData<List<Video>>().also { loadVideos(it) }
    }

    private val _feedbackLabels: MutableLiveData<List<String>>by lazy {
        MutableLiveData<List<String>>().also { loadFeedbackLabels(it) }
    }

    fun getVideos(): LiveData<List<Video>> {
        return _videos
    }

    fun getFeedbackLabels(): LiveData<List<String>> {
        return _feedbackLabels
    }

    private fun loadVideos(videos: MutableLiveData<List<Video>>) {
        videos.value = listOf(
            Video(
                id = 0,
                videoTitle = "3个有才华的妻子，因为思念丈夫而留下深情的诗句",
                videoSourceUrl = "视频地址",
                videoLabel = "趣味科普人文",
                videoThumbUrl = "http://i1.hdslb.com/bfs/archive/0037921aa530e40603dd93545eb165f73c2b0cc5.jpg"
            ),
            Video(
                id = 0,
                videoTitle = "“吸猫吸狗”可能会培育超级病毒",
                videoLabel = "趣味科普人文",
                videoSourceUrl = "视频地址",
                videoThumbUrl = "http://i2.hdslb.com/bfs/archive/dbf1cbf84016fc9392cb851fe24384014205406c.jpg"
            )
        )
    }

    private fun loadFeedbackLabels(labels: MutableLiveData<List<String>>) {
        labels.value = listOf(
            "血腥恐怖",
            "色情低俗",
            "封面恶心",
            "标题党/封面党"
        )
    }

    companion object {

        private lateinit var sVideoRowsInstance: LiveData<PagedList<Video>>
        private lateinit var sRecyclerViewPosition: MutableLiveData<RecyclerPositionData>

        fun getVideoRowsLiveData(): LiveData<PagedList<Video>> {
            sVideoRowsInstance = if (::sVideoRowsInstance.isInitialized) sVideoRowsInstance
            else VideoDataSourceFactory().toLiveData(
                pageSize = 10
            )
            return sVideoRowsInstance
        }

        fun getRecyclerViewPosition(): MutableLiveData<RecyclerPositionData> {
            sRecyclerViewPosition = if (::sRecyclerViewPosition.isInitialized) sRecyclerViewPosition
            else MutableLiveData<RecyclerPositionData>().apply {
                value = RecyclerPositionData()
            }
            return sRecyclerViewPosition
        }
    }
}