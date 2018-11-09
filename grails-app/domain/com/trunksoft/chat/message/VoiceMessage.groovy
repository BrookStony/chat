package com.trunksoft.chat.message

import com.trunksoft.chat.type.MessageType

class VoiceMessage extends ChatMessage {

    MessageType type = MessageType.VOICE
    String voiceFormat
    String mediaId

    static constraints = {
        voiceFormat(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("VoiceMessage{id: " + id)
        sb.append(",account: " + account?.id)
        sb.append(",fromUser: " + fromUser?.openId)
        sb.append(",toUser: " + toUser?.openId)
        sb.append(",voiceFormat: " + voiceFormat)
        sb.append(",mediaId: " + mediaId)
        sb.append(",status: " + status)
        sb.append(",msgTime: " + msgTime)
        sb.append(",msgid: " + msgid)
        sb.append("}")
    }
}
