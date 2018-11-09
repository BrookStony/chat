package com.trunksoft.chat.message

import com.trunksoft.chat.type.MessageType

class NewsMessage extends ChatMessage {

    static hasMany = [articles: NewsArticle]

    MessageType type = MessageType.NEWS

    static constraints = {
    }
}
