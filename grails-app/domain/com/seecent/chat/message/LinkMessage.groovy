package com.seecent.chat.message

import com.seecent.chat.type.MessageType

class LinkMessage extends ChatMessage {

    MessageType type = MessageType.LINK
    String url
    String title
    String description

    static constraints = {
        title(nullable: true)
        description(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("LinkMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",url: " + url)
        sb.append(",title: " + title)
        sb.append(",description: " + description)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }
}
