package com.seecent.chat.member

import com.seecent.platform.DateBase
import com.seecent.chat.Account

class MemberGroup extends DateBase {

    static belongsTo = [account: Account]
    Long groupId
    String name
    String description

    static constraints = {
        description(nullable: true)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("MemberGroup{id: " + id)
        sb.append(",name: " + name)
        sb.append("}")
    }

}
