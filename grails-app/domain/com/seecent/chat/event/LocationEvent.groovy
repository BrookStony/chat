package com.seecent.chat.event

import com.seecent.chat.type.ChatEventType

class LocationEvent extends ChatEvent {

    ChatEventType type = ChatEventType.LOCATION
    Double locLatitude
    Double locLongitude
    Double locPrecision

    static constraints = {

    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("LocationEvent{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",locLatitude: " + locLatitude)
        sb.append(",locLongitude: " + locLongitude)
        sb.append(",locPrecision: " + locPrecision)
        sb.append(",eventTime: " + eventTime)
        sb.append("}")
    }
}
