package com.seecent.platform.system

import com.seecent.chat.Account
import com.seecent.platform.DateBase

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
