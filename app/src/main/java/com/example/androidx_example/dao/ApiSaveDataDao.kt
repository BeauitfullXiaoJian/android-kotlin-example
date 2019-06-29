package com.example.androidx_example.dao

import androidx.room.Insert
import androidx.room.Query

interface ApiSaveDataDao<ApiSaveData> {
    @Query("select * from api_data where api_name = :apiName and api_param_hash = :hashCode and lost_time > :currentTime")
    fun findSaveData(apiName: String, hashCode: Int, currentTime: Long = System.currentTimeMillis())
}