package com.example.androidx_example.until

import androidx.room.Room
import com.example.androidx_example.App
import com.example.androidx_example.dao.AppDatabase

object RoomUntil {

    lateinit var db: AppDatabase
        private set

    fun initDB() {
        if (!this::db.isInitialized) {
            db = Room.databaseBuilder(
                App.instance,
                AppDatabase::class.java, SQLiteUntil.DATABASE_NAME
            ).build()
        }
    }
}