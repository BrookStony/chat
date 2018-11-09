package com.trunksoft.chat.message

import com.trunksoft.chat.type.MessageType

class VideoMessage extends ChatMessage {

    MessageType type = MessageType.VIDEO
    String mediaId
    String thumbMediaId
    String title
    String description

    static constraints = {
        title(nullable: true)
        description(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("VideoMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",mediaId: " + mediaId)
        sb.append(",thumbMediaId: " + thumbMediaId)
        sb.append(",title: " + title)
        sb.append(",description: " + description)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }
}
