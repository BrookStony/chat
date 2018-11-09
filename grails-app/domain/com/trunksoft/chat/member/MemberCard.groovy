package com.trunksoft.chat.member

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.common.Money
import com.trunksoft.chat.status.MemberCardStatus
import com.trunksoft.chat.Account
import com.trunksoft.chat.type.MemberCardType

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
