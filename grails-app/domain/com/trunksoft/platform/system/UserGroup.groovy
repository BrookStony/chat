package com.trunksoft.platform.system

import com.trunksoft.chat.Account
import com.trunksoft.platform.DateBase

class UserGroup extends DateBase {

    static hasMany = [accounts: Account]

    String name
    Boolean allAcount = false

    static constraints = {
    }

    static mapping = {
        accounts lazy: true
    }
}
