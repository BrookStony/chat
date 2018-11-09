package com.seecent.chat.account

import com.seecent.chat.Account
import com.seecent.chat.type.QRCodeType
import com.seecent.platform.DateBase

class QRCode extends DateBase {

    static belongsTo = [account: Account]

    Long expireSeconds
    QRCodeType type
    Long sceneId
    String sceneStr
    String ticket
    String url

    static constraints = {
        expireSeconds(nullable: true)
        sceneId(nullable: true)
        sceneStr(nullable: true)
        ticket(nullable: true)
        url(nullable: true)
    }

    static mapping = {
        version false
        type enumType: 'ordinal'
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("QRCode{id: " + id)
        sb.append(",type: " + type)
        if(sceneId) {
            sb.append(",sceneId: " + sceneId)
        }
        if(sceneStr) {
            sb.append(",sceneStr: " + sceneStr)
        }
        sb.append("}")
    }
}
