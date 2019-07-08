package com.example.androidx_example.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "msg_data")
data class MessageSaveData(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "work_request_id")
    val requestId: String,

    @ColumnInfo(name = "send_state")
    val sendState: Int,

    @ColumnInfo(name = "msg_data")
    val msgData: String,

    @ColumnInfo(name = "receiver_uid")
    val receiverUid: String,

    @ColumnInfo(name = "send_time")
    val sendTime: Long,

    @ColumnInfo(name = "receiver_time")
    val receiverTime: Long? = null
)