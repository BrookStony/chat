package com.seecent.chat.event

import com.seecent.chat.type.ChatEventType

class UnSubscribeEvent extends ChatEvent {

    ChatEventType type = ChatEventType.UNSUBSCRIBE

    static constraints = {

    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("UnSubscribeEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}
