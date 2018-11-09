package com.trunksoft.chat.member

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.Account

class MemberGrade extends DateBase {

    static belongsTo = [account: Account]

    Integer grade
    String name
    Integer score
    String image
    Integer imageAmount = 1
    String description

    static constraints = {
        grade(range:  1..50)
        image(nullable: true)
        description(nullable: true)
    }

}
