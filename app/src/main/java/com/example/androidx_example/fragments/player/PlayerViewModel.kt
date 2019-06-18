package com.example.androidx_example.fragments.player

import androidx.lifecycle.*
import com.example.androidx_example.data.Video
import com.example.androidx_example.until.getSuccess

class PlayerViewModel() : ViewModel() {

    // 视频数据编号
    var videoId: Int = 0

    // 当前是否在加载视频信息
    val videoDataIsLoading: MutableLiveData<Boolean>by lazy {
        MutableLiveData<Boolean>().also { it.value = false }
    }
    // 视频信息数据
    val video: MutableLiveData<Video>by lazy {
        MutableLiveData<Video>().also { loadVideo(it) }
    }


    fun reloadVideo() {
        this.loadVideo(video)
    }

    private fun loadVideo(field: MutableLiveData<Video>) {
        getSuccess(
            apiName = "video",
            params = hashMapOf("id" to videoId),
            successDo = { res -> field.postValue(res.getObjectData(Video::class.java)) },
            completeDo = { videoDataIsLoading.postValue(true) }
        )
    }
}