package com.seecent.chat.member

import com.seecent.platform.DateBase
import com.seecent.chat.common.Money
import com.seecent.chat.status.MemberCardStatus
import com.seecent.chat.Account
import com.seecent.chat.type.MemberCardType

class MemberCard extends DateBase {

    static belongsTo = [account: Account]

    Member member
    String code
    Money money
    Integer score
    Date activateTime
    MemberCardType type
    MemberCardStatus status

    static embedded = ['money']

    static constraints = {
        activateTime(nullable: true)
        member(nullable: true)
    }

    static mapping = {
        type enumType: 'ordinal'
        status enumType: 'ordinal'
    }
}
