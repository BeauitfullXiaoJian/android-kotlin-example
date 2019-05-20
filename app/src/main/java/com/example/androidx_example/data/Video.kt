package com.example.androidx_example.data

data class Video(
    val id: Int,
    val videoTitle: String,
    val videoSourceUrl: String,
    val videoThumbUrl: String,
    val videoLabel: String,
    val likeNum: Int = 0,
    val dislikeNum: Int = 0
)

class VideoBuilder {
    companion object {
        fun create() = Video(
            id = 0,
            videoTitle = "",
            videoSourceUrl = "",
            videoThumbUrl = "",
            videoLabel = "",
            likeNum = 0,
            dislikeNum = 0
        )
    }
}