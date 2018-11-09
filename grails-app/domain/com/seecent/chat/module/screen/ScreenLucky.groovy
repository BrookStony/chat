package com.seecent.chat.module.screen

import com.seecent.platform.DateBase

class ScreenLucky extends DateBase {

    static belongsTo = [screen: ChatScreen]

    static hasMany = [items: ScreenLuckyItem]

    String name
    String description

    static constraints = {
    }
}
