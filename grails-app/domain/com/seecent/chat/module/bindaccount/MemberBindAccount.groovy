package com.seecent.chat.module.bindaccount

import com.seecent.chat.Account
import com.seecent.chat.member.Member
import com.seecent.platform.DateBase

class MemberBindAccount extends DateBase {

    static belongsTo = [account: Account]
    Member member
    String userId
    String name
    String phone
    MemberBindStatus status

    static constraints = {
        name(nullable: true)
        phone(nullable: true)
    }

    static mapping = {
        status enumType: 'ordinal'
    }
}
