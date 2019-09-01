package com.example.androidx_example.fragments.album

import android.content.res.Resources
import com.example.androidx_example.R
import com.example.androidx_example.data.AlbumData
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.data.PhotoSize
import com.google.gson.Gson

object PhotoDataLoader {

    fun loadAlbum(resources: Resources): Array<AlbumData> {
        val inputStream = resources.openRawResource(R.raw.album)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        return Gson().fromJson(byteArray.toString(Charsets.UTF_8), Array<AlbumData>::class.java)
    }

    fun loadPhoto(name: String, resources: Resources, packageName: String): Array<PhotoData> {
        return arrayOf<PhotoData>()
//        resources.getIdentifier(name, "raw", packageName)
//        val inputStream = resources.openRawResource(R.raw.album)
//        val byteArray = ByteArray(inputStream.available())
//        inputStream.read(byteArray)
//        return Gson().fromJson(byteArray.toString(Charsets.UTF_8), Array<PhotoData>::class.java)
    }
}