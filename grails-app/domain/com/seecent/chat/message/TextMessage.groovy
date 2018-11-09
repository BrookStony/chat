package com.seecent.chat.message

import com.seecent.chat.type.MessageType

class TextMessage extends ChatMessage {

    MessageType type = MessageType.TEXT
    String content

    static constraints = {
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("TextMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",content: " + content)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }

}
