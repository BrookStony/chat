package com.trunksoft.chat.common

import com.trunksoft.chat.Account

class Tag {

    String label
    String description

    static belongsTo = [account: Account]

    static constraints = {
        label(blank: false, unique: 'account')
        description(nullable: true)
    }
}
