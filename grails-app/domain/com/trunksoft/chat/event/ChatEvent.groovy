package com.trunksoft.chat.event

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.type.ChatEventType
import com.trunksoft.platform.DateBase

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
