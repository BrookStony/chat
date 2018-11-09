package com.trunksoft.chat.message

import com.trunksoft.chat.type.MessageType

class LocationMessage extends BaseMessage {

    MessageType type = MessageType.LOCATION

    Double locationx
    Double locationy
    Integer scale
    String label

    static constraints = {
        label(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("LocationMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",locationx: " + locationx)
        sb.append(",locationy: " + locationy)
        sb.append(",scale: " + scale)
        sb.append(",label: " + label)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }
}
