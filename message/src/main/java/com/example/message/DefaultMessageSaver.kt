package com.example.message

import android.content.Context
import com.example.message.room.RoomEntity

class DefaultMessageSaver : MessageSaver {
    override fun save(msg: Message) {
        dao().insert(msgEntity(msg))
    }

    override fun update(msg: Message) {
        dao().update(msgEntity(msg))
    }

    companion object {

        fun dao() = RoomUtil.db.roomDao()

        fun create(appContext: Context): DefaultMessageSaver {
            RoomUtil.initDB(appContext).subscribe()
            return DefaultMessageSaver()
        }

        fun msgEntity(msg: Message): RoomEntity {
            return RoomEntity(
                fromUid = msg.msgData.fromUid,
                toUid = msg.msgData.toUid,
                sendState = msg.sendState.value,
                sendTime = msg.sendTime,
                receiverTime = msg.receiverTime,
                msgContent = msg.msgData.content,
                msgType = msg.msgData.type
            )
        }
    }
}