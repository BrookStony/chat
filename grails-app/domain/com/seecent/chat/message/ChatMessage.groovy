package com.seecent.chat.message

import com.seecent.chat.assets.Material
import com.seecent.platform.DateBase
import com.seecent.chat.member.Member
import com.seecent.chat.services.MessageResult
import com.seecent.chat.status.ChatMessageStatus
import com.seecent.chat.Account
import com.seecent.chat.type.MessageType

class ChatMessage extends DateBase {

    static belongsTo = [account: Account]

    Long msgid
    Long msgTime
    Long materialId
    Member fromUser
    Member toUser

    MessageType type
    ChatMessageStatus status

    Integer errcode = 0
    String errmsg = "ok"

    static constraints = {
        msgid(nullable: true)
        materialId(nullable: true)
        fromUser(nullable: true)
        toUser(nullable: true)
        errmsg(nullable: true)
    }

    static mapping = {
        version(false)
        type enumType: 'ordinal'
        status enumType: 'ordinal'
        errmsg length: 2000
    }

    void bindResult(MessageResult result) {
        this.errcode = result.code
        if(result.message && result.message.length() <= 200){
            this.errmsg = result.message
        }
        this.msgid = result.msgid
        if(result.isOk()) {
            if(toUser) {
                this.status = ChatMessageStatus.SEND_SUCCESS
            }
            else if(fromUser){
                this.status = ChatMessageStatus.RECEIVE_SUCCESS
            }
            else if(!toUser && !fromUser){
                this.status = ChatMessageStatus.SEND_SUCCESS
            }
        }
        else {
            if(toUser) {
                this.status = ChatMessageStatus.SEND_FAILURE
            }
            else if(fromUser){
                this.status = ChatMessageStatus.RECEIVE_FAILURE
            }
            else if(!toUser && !fromUser){
                this.status = ChatMessageStatus.SEND_FAILURE
            }
        }
    }
}
