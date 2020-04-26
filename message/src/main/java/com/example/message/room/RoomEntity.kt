package com.example.message.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.message.Message

@Entity(tableName = "message_data")
data class RoomEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "send_state")
    var sendState: Int,

    @ColumnInfo(name = "send_time")
    var sendTime: Long,

    @ColumnInfo(name = "receiver_time")
    var receiverTime: Long?,

    @ColumnInfo(name = "from_uid")
    var fromUid: String,

    @ColumnInfo(name = "to_uid")
    var toUid: String,

    @ColumnInfo(name = "content")
    var msgContent: String,

    @ColumnInfo(name = "type")
    var msgType: Message.MessageType
)