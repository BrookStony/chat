package com.trunksoft.chat.module.screen

import com.trunksoft.chat.member.Member
import com.trunksoft.platform.DateBase

class ScreenLuckyMember extends DateBase {

    static belongsTo = [screen: ChatScreen, lucky: ScreenLucky]

    Member member
    ScreenLuckyItem item

    static constraints = {
    }
}
