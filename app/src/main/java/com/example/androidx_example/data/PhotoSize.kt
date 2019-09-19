package com.example.androidx_example.data

import android.util.Size

data class PhotoSize(val width: Int, val height: Int) {
    val size: Size
        get() = Size(width, height)
}