package com.seecent.platform.system

import com.seecent.chat.Account
import com.seecent.platform.DateBase

class Group extends DateBase {

    static hasMany = [accounts: Account]

    String name
    Boolean allAcount

    static constraints = {
    }
}
