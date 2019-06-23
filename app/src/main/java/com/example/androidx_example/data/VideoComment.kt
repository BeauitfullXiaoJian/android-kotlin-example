package com.example.androidx_example.data

data class VideoComment(
    val user: UpDetail,
    val content: String,
    val likeNum: Int,
    val sendTime:String,
    val repliesNum: Int,
    val repliesRows: List<VideoComment>? = null
)