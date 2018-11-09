package com.trunksoft.chat.module.bindaccount

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member
import com.trunksoft.platform.DateBase

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
