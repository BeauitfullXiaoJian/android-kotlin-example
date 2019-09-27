package com.example.androidx_example.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.example.androidx_example.entity.MessageSaveData

@Dao
interface MessageSaveDataDao : BaseDao<MessageSaveData> {
    @Query("select * from  msg_data where id > :startDataId limit :limit")
    fun getPageMessage(
        startDataId: Int,
        limit: Int
    ): Array<MessageSaveData>

    @Query("select * from msg_data where work_request_id = :requestId limit 1")
    fun findMsgByRequestId(requestId: String): MessageSaveData?

    @Query("select * from msg_data order by id desc")
    fun messages(): DataSource.Factory<Int, MessageSaveData>
}