package com.trunksoft.chat.event

import com.trunksoft.chat.type.ChatEventType

class ClickEvent extends ChatEvent {

    ChatEventType type = ChatEventType.CLICK
    String eventKey

    static constraints = {

    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("ClickEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",eventKey: " + eventKey)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}
