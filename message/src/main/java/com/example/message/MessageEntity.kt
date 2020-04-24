package com.example.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_data")
data class MessageEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "work_request_id")
    var workRequestId: String,

    @ColumnInfo(name = "send_state")
    var sendState: Int,

    @ColumnInfo(name = "from_uid")
    var fromUid: String,

    @ColumnInfo(name = "to_uid")
    var toUid: String,

    @ColumnInfo(name = "send_time")
    var sendTime: Long,

    @ColumnInfo(name = "receiver_time")
    var receiverTime: Long?
)