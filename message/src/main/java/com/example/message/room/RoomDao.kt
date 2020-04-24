package com.example.message.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDao {

    @Insert
    fun insert(obj: RoomEntity): Long

    @Update
    fun update(obj: RoomEntity)

    @Query("select * from  message_data where id > :startId limit :limit")
    fun messages(
        startId: Int,
        limit: Int
    ): Array<RoomEntity>
}