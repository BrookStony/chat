package com.trunksoft.chat.module.screen

import com.trunksoft.platform.DateBase

class ScreenTheme extends DateBase {

    String name
    String description

    static constraints = {
        description(nullable: true)
    }
}
