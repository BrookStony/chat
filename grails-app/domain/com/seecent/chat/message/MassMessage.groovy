package com.seecent.chat.message

import com.seecent.chat.Account
import com.seecent.chat.status.MassMessageStatus
import com.seecent.chat.type.MessageType
import com.seecent.platform.DateBase
import com.seecent.chat.services.MassMessageResult

class MassMessage extends DateBase {

    static belongsTo = [account: Account]
    static hasMany = [toUsers: Long]

    static transients = ['touser', 'towxname']

    MessageType type
    Boolean isToAll = true
    Long groupId
    Long locationId
    Integer sexType
    Set<Long> toUsers
    String content
    Long materialId
    Long articleId
    String mediaId
    Long msgId
    Long msgDataId
    Long msgTime
    Integer errcode
    String errmsg

    MassMessageStatus status = MassMessageStatus.SENDING
    Integer errnum = 0
    Integer totalCount = -1
    Integer filterCount = -1
    Integer sentCount = -1
    Integer errorCount = -1

    String touser
    String towxname

    static constraints = {
        groupId(nullable: true)
        locationId(nullable: true)
        sexType(nullable: true)
        toUsers(nullable: true)
        content(nullable: true)
        materialId(nullable: true)
        articleId(nullable: true)
        mediaId(nullable: true)
        msgId(nullable: true)
        msgDataId(nullable: true)
        errcode(nullable: true)
        errmsg(nullable: true)
    }

    static mapping = {
        type enumType: 'ordinal'
        status enumType: 'ordinal'
        toUsers lazy: true
    }

    void bindResult(MassMessageResult result) {
        this.errcode = result.code
        if (result.message && result.message.length() <= 200) {
            this.errmsg = result.message
        }
        this.msgId = result.msgId
        this.msgDataId = result.msgDataId
        if (result.isOk()) {
            this.status = MassMessageStatus.SENDED
        }
        else {
            this.status = MassMessageStatus.SEND_FAIL
        }
    }
}
