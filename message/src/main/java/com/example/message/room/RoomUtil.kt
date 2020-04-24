package com.example.message

import android.content.Context
import androidx.room.Room
import com.example.message.room.RoomDB
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

object RoomUtil {

    private const val DATABASE_NAME = "message.db"

    lateinit var db: RoomDB
        private set

    fun initDB(appContext: Context): Completable {
        return Completable.fromRunnable {
            db = Room.databaseBuilder(
                appContext,
                RoomDB::class.java,
                DATABASE_NAME
            ).build()
        }.observeOn(Schedulers.newThread())
    }
}