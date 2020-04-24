package com.example.message.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RoomEntity::class
    ], version = 1, exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}