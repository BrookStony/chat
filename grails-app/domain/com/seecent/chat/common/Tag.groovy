package com.seecent.chat.common

import com.seecent.chat.Account

class Tag {

    String label
    String description

    static belongsTo = [account: Account]

    static constraints = {
        label(blank: false, unique: 'account')
        description(nullable: true)
    }
}
