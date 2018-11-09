package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.platform.DateBase

class PictureGroup extends DateBase {

    static belongsTo = [account: Account]
    String name

    static constraints = {
        name(nullable: true)
    }
}
