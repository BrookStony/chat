package com.seecent.chat.module.screen

import com.seecent.platform.DateBase

class ScreenLuckyItem extends DateBase {

    static belongsTo = [screen: ChatScreen, lucky: ScreenLucky]

    String name
    Integer no = 0
    Integer num = 1
    String uuid
    String fileName
    String filePath
    String fileType
    String picUrl
    String description

    static constraints = {
        uuid(nullable: true)
        fileName(nullable: true)
        filePath(nullable: true)
        fileType(nullable: true)
        picUrl(nullable: true)
        description(nullable: true)
    }

    static mapping = {
        no(default: 0)
        num(default: 1)
    }
}
