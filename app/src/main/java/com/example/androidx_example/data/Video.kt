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