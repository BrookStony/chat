package com.trunksoft.chat.member

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.Account

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
