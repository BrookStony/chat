package com.seecent.chat.message

import com.seecent.chat.type.MessageType

class MusicMessage extends ChatMessage {

    MessageType type = MessageType.MUSIC
    String title
    String description
    String musicurl
    String hqmusicurl
    String thumbMediaId

    static constraints = {
        title(nullable: true)
        description(nullable: true)
    }
}
