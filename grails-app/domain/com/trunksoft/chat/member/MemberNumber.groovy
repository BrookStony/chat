package com.trunksoft.chat.member

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.status.MemberNumberStatus
import com.trunksoft.chat.Account

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
