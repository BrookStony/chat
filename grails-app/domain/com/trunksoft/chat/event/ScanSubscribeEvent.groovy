package com.trunksoft.chat.event

import com.trunksoft.chat.type.ChatEventType

class ScanSubscribeEvent extends ChatEvent {

    ChatEventType type = ChatEventType.SCANSUBSCRIBE
    String eventKey
    String ticket

    static constraints = {
        eventKey(nullable: true)
        ticket(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("ScanSubscribeEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",eventKey: " + eventKey)
        sb.append(",ticket: " + ticket)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}