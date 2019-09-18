package com.example.androidx_example.fragments.album

import com.example.androidx_example.App
import com.example.androidx_example.R
import com.example.androidx_example.data.AlbumData
import com.example.androidx_example.data.PhotoData
import com.example.androidx_example.until.tool.debugInfo
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

typealias AlbumObservable = Observable<Array<AlbumData>>
typealias PhotoObservable = Observable<Array<PhotoData>>

object PhotoDataLoader {

    private val packageName by lazy {
        App.instance.packageName
    }

    private val resources by lazy {
        App.instance.resources
    }

    fun loadAlbum(): AlbumObservable {
        return Observable.create<Array<AlbumData>> { sub ->
            val inputStream = resources.openRawResource(R.raw.album)
            val byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray)
            val albums = Gson().fromJson(byteArray.toString(Charsets.UTF_8), Array<AlbumData>::class.java)
            sub.onNext(albums)
            sub.onComplete()
        }.subscribeOn(AndroidSchedulers.mainThread())
    }

    fun loadPhoto(name: String): PhotoObservable {
        return Observable.create<Array<PhotoData>> { sub ->
            debugInfo(name)
            val resId = resources.getIdentifier(name, "raw", packageName)
            val inputStream = resources.openRawResource(resId)
            val byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray)
            val albums = Gson().fromJson(byteArray.toString(Charsets.UTF_8), Array<PhotoData>::class.java)
            sub.onNext(albums)
            sub.onComplete()
        }.subscribeOn(AndroidSchedulers.mainThread())
    }
}