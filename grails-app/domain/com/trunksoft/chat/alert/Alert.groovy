package com.trunksoft.chat.alert

import com.trunksoft.chat.Account

class Alert {

    static belongsTo = [account: Account]

    String title
    AlertType type

    static constraints = {
    }
}
