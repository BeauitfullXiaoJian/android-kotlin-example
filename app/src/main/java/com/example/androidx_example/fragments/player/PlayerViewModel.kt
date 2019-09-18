package com.example.androidx_example.fragments.player

import androidx.lifecycle.*
import com.example.androidx_example.data.Pagination
import com.example.androidx_example.data.Video
import com.example.androidx_example.data.VideoComment
import com.example.androidx_example.data.VideoDetailInfo
import com.example.androidx_example.until.tool.getSuccess

class PlayerViewModel : ViewModel() {

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
    val videoRecommend: MutableLiveData<Array<VideoDetailInfo>>by lazy {
        MutableLiveData<Array<VideoDetailInfo>>().also { loadRecommendVideo(it) }
    }

    // 当前是否在加载评论信息
    val videoCommentIsLoading: MutableLiveData<Boolean>by lazy {
        MutableLiveData<Boolean>().also { it.value = false }
    }

    // 视频评论
    val videoComment: MutableLiveData<Array<VideoComment>>by lazy {
        MutableLiveData<Array<VideoComment>>().also { it.value = arrayOf() }
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

    private fun loadRecommendVideo(field: MutableLiveData<Array<VideoDetailInfo>>) {
        getSuccess(apiName = "video/recommend",
            params = hashMapOf("id" to videoId),
            successDo = { res -> field.postValue(res.getObjectList(VideoDetailInfo::class.java).toTypedArray()) },
            completeDo = { videoDataIsLoading.postValue(false) })
    }

    fun loadVideoComment(page: Pagination) {
        page.isLoading = true
        getSuccess(apiName = "video/comments",
            params = hashMapOf(
                "id" to videoId,
                "page" to page.currentPageNum
            ),
            successDo = { res ->
                val pageData = res.getPageData(VideoComment::class.java)
                page.updateTotal(pageData.total)
                videoComment.postValue(videoComment.value!! + pageData.rows.toTypedArray())
            },
            completeDo = {
                page.isLoading = false
                videoCommentIsLoading.postValue(false)
            }
        )
    }
}