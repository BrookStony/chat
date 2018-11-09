package com.seecent.chat.module.screen

import com.seecent.platform.DateBase

class ScreenMessage extends DateBase {

    static belongsTo = [screen: ChatScreen]

    Long messageId
    Boolean visible = false

    static constraints = {
    }
}
