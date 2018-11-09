package com.seecent.chat.module.screen

import com.seecent.platform.DateBase

class ScreenPhoto extends DateBase {

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
