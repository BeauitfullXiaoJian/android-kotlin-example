package com.example.androidx_example.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.androidx_example.entity.ApiSaveData

@Dao
interface ApiSaveDataDao : BaseDao<ApiSaveData> {
    @Query("select api_data from  api_data where api_name = :apiName and api_param_hash = :hashCode and lost_time > :currentTime")
    fun findSaveData(apiName: String, hashCode: Int, currentTime: Long = System.currentTimeMillis()): String?
}