package com.example.androidx_example.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidx_example.entity.ApiSaveData
import com.example.androidx_example.entity.AppErrorLog
import com.example.androidx_example.entity.MessageSaveData

@Database(
    entities = [
        ApiSaveData::class,
        MessageSaveData::class,
        AppErrorLog::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apiSaveDataDao(): ApiSaveDataDao
    abstract fun msgSaveDataDao(): MessageSaveDataDao
    abstract fun appErrorLogDao(): AppErrorLogDao
}