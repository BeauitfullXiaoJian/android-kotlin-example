package com.example.androidx_example.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class VideoDataSourceFactory : DataSource.Factory<Pagination, Video>() {
    private val sourceLiveData = MutableLiveData<VideoDataSource>()
    private var latestSource: VideoDataSource? = null
    override fun create(): DataSource<Pagination, Video> {
        latestSource = VideoDataSource()
        sourceLiveData.postValue(latestSource)
        return latestSource!!
    }
}