package com.example.androidx_example.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.androidx_example.until.debugInfo

class VideoDataSourceFactory : DataSource.Factory<Pagination, Video>() {
    private val sourceLiveData = MutableLiveData<VideoDataSource>()
    private var latestSource: VideoDataSource? = null
    override fun create(): DataSource<Pagination, Video> {
        debugInfo("=====创建数据源=====")
        latestSource = VideoDataSource()
        sourceLiveData.postValue(latestSource)
        return latestSource!!
    }
}