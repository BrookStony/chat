package com.seecent.chat.module.screen

import com.seecent.platform.DateBase

class ScreenTheme extends DateBase {

    String name
    String description

    static constraints = {
        description(nullable: true)
    }
}
