package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.platform.DateBase

class PictureGroup extends DateBase {

    static belongsTo = [account: Account]
    String name

    static constraints = {
        name(nullable: true)
    }
}
