package com.example.androidx_example.fragments.player

import androidx.lifecycle.*
import com.example.androidx_example.data.Video
import com.example.androidx_example.until.getSuccess

class PlayerViewModel() : ViewModel() {

    var videoId: Int = 0

    val video: MutableLiveData<Video>by lazy {
        MutableLiveData<Video>().also { loadVideo(it) }
    }

    private fun loadVideo(field: MutableLiveData<Video>) {
        getSuccess(
            apiName = "video",
            params = hashMapOf("id" to videoId),
            successDo = { res ->
                field.postValue(res.getObjectData(Video::class.java))
            }
        )
    }
}