package com.example.androidx_example.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidx_example.entity.ApiSaveData

@Database(entities = [ApiSaveData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apiSaveDataDao(): ApiSaveDataDao
    abstract fun msgSaveDataDao(): MessageSaveDataDao
}