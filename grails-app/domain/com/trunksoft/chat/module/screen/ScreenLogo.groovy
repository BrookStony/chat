package com.trunksoft.chat.module.screen

import com.trunksoft.platform.DateBase

class ScreenLogo extends DateBase {

    static belongsTo = [screen: ChatScreen]

    String uuid
    String fileName
    String filePath
    String fileType
    String picUrl
    Long mediaSize = 0l

    static constraints = {

    }
}
