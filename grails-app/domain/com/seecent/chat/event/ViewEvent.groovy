package com.seecent.chat.event

import com.seecent.chat.type.ChatEventType

class ViewEvent extends ChatEvent {

    ChatEventType type = ChatEventType.VIEW
    String eventKey

    static constraints = {

    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("ViewEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",eventKey: " + eventKey)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}
