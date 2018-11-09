package com.seecent.chat.message

import com.seecent.chat.Account
import com.seecent.chat.type.MessageType

class TemplateMessage extends BaseMessage {

    static belongsTo = [account: Account]

    static transients = ['data']

    MessageType type = MessageType.TEMPALTE
    MessageTemplate template
    String topColor
    String url
    String content
    String dataJson

    def data

    static constraints = {
        msgid(nullable: true)
        fromUser(nullable: true)
        toUser(nullable: true)
        errmsg(nullable: true)
        topColor(nullable: true)
        url(nullable: true)
    }

    static mapping = {
        version(false)
        type enumType: 'ordinal'
        status enumType: 'ordinal'
        content length: 4000
        dataJson length: 4000
        errmsg length: 2000
    }

}
