package com.trunksoft.chat.event

import com.trunksoft.chat.type.ChatEventType

class SubscribeEvent extends ChatEvent {

    ChatEventType type = ChatEventType.SUBSCRIBE

    static constraints = {
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("SubscribeEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}
