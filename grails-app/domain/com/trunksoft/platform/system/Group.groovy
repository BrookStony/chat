package com.trunksoft.platform.system

import com.trunksoft.chat.Account
import com.trunksoft.platform.DateBase

class Group extends DateBase {

    static hasMany = [accounts: Account]

    String name
    Boolean allAcount

    static constraints = {
    }
}
