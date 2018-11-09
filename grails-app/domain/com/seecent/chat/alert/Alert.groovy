package com.seecent.chat.alert

import com.seecent.chat.Account

class Alert {

    static belongsTo = [account: Account]

    String title
    AlertType type

    static constraints = {
    }
}
