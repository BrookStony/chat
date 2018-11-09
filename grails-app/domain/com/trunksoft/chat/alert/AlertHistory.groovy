package com.trunksoft.chat.alert

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.Account

class AlertHistory extends DateBase {

    static belongsTo = [account: Account]

    AlertType type

    static constraints = {
    }
}
