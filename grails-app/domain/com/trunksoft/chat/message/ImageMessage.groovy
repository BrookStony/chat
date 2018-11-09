package com.trunksoft.chat.message

import com.trunksoft.chat.type.MessageType

class ImageMessage extends ChatMessage {

    MessageType type = MessageType.IMAGE
    String picurl
    String mediaId

    static constraints = {
        picurl(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("ImageMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",picurl: " + picurl)
        sb.append(",mediaId: " + mediaId)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }
}
