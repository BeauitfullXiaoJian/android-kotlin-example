package com.example.androidx_example.until.sql

import androidx.room.Room
import com.example.androidx_example.App
import com.example.androidx_example.dao.AppDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

object RoomUntil {

    lateinit var db: AppDatabase
        private set

    fun initDB(): Completable {
        return Completable.fromRunnable {
            db = Room.databaseBuilder(
                App.instance,
                AppDatabase::class.java,
                SQLiteUntil.DATABASE_NAME
            ).build()
        }.observeOn(Schedulers.newThread())
    }
}