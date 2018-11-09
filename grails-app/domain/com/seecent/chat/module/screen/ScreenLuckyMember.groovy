package com.seecent.chat.module.screen

import com.seecent.chat.member.Member
import com.seecent.platform.DateBase

class ScreenLuckyMember extends DateBase {

    static belongsTo = [screen: ChatScreen, lucky: ScreenLucky]

    Member member
    ScreenLuckyItem item

    static constraints = {
    }
}
