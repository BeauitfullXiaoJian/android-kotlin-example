package com.example.androidx_example.fragments.player

import androidx.lifecycle.*
import com.example.androidx_example.data.Video
import com.example.androidx_example.data.VideoDetailInfo
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.getSuccess

class PlayerViewModel() : ViewModel() {

    // 视频数据编号
    var videoId: Int = 0

    // 当前是否在加载视频信息
    val videoDataIsLoading: MutableLiveData<Boolean>by lazy {
        MutableLiveData<Boolean>().also { it.value = false }
    }
    // 视频信息数据
    val video: MutableLiveData<Video> by lazy {
        MutableLiveData<Video>().also { loadVideo(it) }
    }

    // 视频详细信息
    val videoDetail: MutableLiveData<VideoDetailInfo> by lazy {
        MutableLiveData<VideoDetailInfo>().also { loadVideoDetailInfo(it) }
    }

    // 相关推荐视频
    val videoRecommend: MutableLiveData<Array<Video>>by lazy {
        MutableLiveData<Array<Video>>().also { loadRecommendVideo(it) }
    }

    /**
     * 重新加载视频信息
     */
    fun reloadDetailFragmentData() {
        loadVideoDetailInfo(videoDetail)
        loadRecommendVideo(videoRecommend)
    }

    private fun loadVideoDetailInfo(field: MutableLiveData<VideoDetailInfo>) {
        getSuccess(
            apiName = "video/more",
            params = hashMapOf("id" to videoId),
            successDo = { res -> field.postValue(res.getObjectData(VideoDetailInfo::class.java)) },
            completeDo = { videoDataIsLoading.postValue(false) }
        )
    }

    private fun loadVideo(field: MutableLiveData<Video>) {
        getSuccess(
            apiName = "video",
            params = hashMapOf("id" to videoId),
            successDo = { res -> field.postValue(res.getObjectData(Video::class.java)) }
        )
    }

    private fun loadRecommendVideo(field: MutableLiveData<Array<Video>>) {
        getSuccess(apiName = "video/recommend",
            params = hashMapOf("id" to videoId),
            successDo = { res -> field.postValue(res.getObjectList(Video::class.java).toTypedArray()) },
            completeDo = { videoDataIsLoading.postValue(false) })
    }
}