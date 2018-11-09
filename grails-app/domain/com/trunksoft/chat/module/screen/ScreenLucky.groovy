package com.trunksoft.chat.module.screen

import com.trunksoft.platform.DateBase

class ScreenLucky extends DateBase {

    static belongsTo = [screen: ChatScreen]

    static hasMany = [items: ScreenLuckyItem]

    String name
    String description

    static constraints = {
    }
}
