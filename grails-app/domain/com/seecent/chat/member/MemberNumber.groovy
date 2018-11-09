package com.seecent.chat.member

import com.seecent.platform.DateBase
import com.seecent.chat.status.MemberNumberStatus
import com.seecent.chat.Account

class MemberNumber extends DateBase {

    static belongsTo = [account: Account]

    Integer no
    Boolean special = false
    MemberNumberStatus status

    static constraints = {
        no(blank: false, unique: 'account')
    }

    static mapping = {
        status enumType: 'ordinal'
    }
}
