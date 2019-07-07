package com.example.androidx_example.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.androidx_example.entity.MessageSaveData

@Dao
interface MessageSaveDataDao : BaseDao<MessageSaveData> {
    @Query("select api_data from  api_data where id > :startDataId limit :limit")
    fun getPageMessage(
        startDataId: Int,
        limit: Int
    ): Array<MessageSaveData>
}