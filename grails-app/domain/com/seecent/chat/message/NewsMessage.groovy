package com.seecent.chat.message

import com.seecent.chat.type.MessageType

class NewsMessage extends ChatMessage {

    static hasMany = [articles: NewsArticle]

    MessageType type = MessageType.NEWS

    static constraints = {
    }
}
