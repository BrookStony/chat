package com.seecent.chat.event

import com.seecent.chat.Account
import com.seecent.chat.member.Member
import com.seecent.chat.type.ChatEventType
import com.seecent.platform.DateBase

class ChatEvent extends DateBase {

    static belongsTo = [account: Account]

    Long eventTime
    Member fromUser
    Member toUser

    ChatEventType type

    static constraints = {
        fromUser(nullable: true)
        toUser(nullable: true)
    }

    static mapping = {
        version(false)
        type enumType: 'ordinal'
    }

}
