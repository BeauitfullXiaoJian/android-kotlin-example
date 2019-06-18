package com.example.androidx_example.data

import java.io.Serializable

data class Video(
    val id: Int,
    val videoTitle: String,
    val videoSourceUrl: String,
    val videoThumbUrl: String,
    val videoLabel: String,
    val likeNum: Int = 0,
    val dislikeNum: Int = 0,
    val coinNum: Int = 0,
    val shareNum: Int = 0,
    val favoriteNum: Int = 0,
    val viewNum: Int = 0,
    val commentNum:Int =0,
    val uploadAt:String
) : Serializable
//
//class VideoBuilder {
//    companion object {
//        fun create() = Video(
//            id = 0,
//            videoTitle = "",
//            videoSourceUrl = "",
//            videoThumbUrl = "",
//            videoLabel = "",
//            likeNum = 0,
//            dislikeNum = 0
//        )
//    }
//}