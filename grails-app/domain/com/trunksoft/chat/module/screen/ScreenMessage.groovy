package com.trunksoft.chat.module.screen

import com.trunksoft.platform.DateBase

class ScreenMessage extends DateBase {

    static belongsTo = [screen: ChatScreen]

    Long messageId
    Boolean visible = false

    static constraints = {
    }
}
