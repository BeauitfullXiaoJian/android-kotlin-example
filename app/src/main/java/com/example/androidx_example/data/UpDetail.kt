package com.example.androidx_example.data

import java.io.Serializable

data class UpDetail(
    val id: Int,
    val nickName: String,
    val avatarImageUrl: String,
    val fansNum: Int
) : Serializable