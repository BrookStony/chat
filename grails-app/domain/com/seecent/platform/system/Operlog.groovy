package com.seecent.platform.system
import com.seecent.chat.Account
import com.seecent.platform.auth.User
import com.seecent.platform.type.OperResult
import com.seecent.platform.type.OperType
import com.seecent.platform.CreateBase

class Operlog extends CreateBase {

    Date opertime
    String detail
    String ip
    Menu menu
    String category
    String source
    OperType type
    OperResult result

    User user
    Account account

    static constraints = {
        account(nullable: true)
        ip(nullable: true)
        user(nullable: true)
        detail(length: 2000)
    }

    static mapping = {
        version(false)
        type enumType: 'ordinal'
        result enumType: 'ordinal'
    }

}
