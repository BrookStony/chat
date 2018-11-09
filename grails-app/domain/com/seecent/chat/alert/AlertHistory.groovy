package com.seecent.chat.alert

import com.seecent.platform.DateBase
import com.seecent.chat.Account

class AlertHistory extends DateBase {

    static belongsTo = [account: Account]

    AlertType type

    static constraints = {
    }
}
