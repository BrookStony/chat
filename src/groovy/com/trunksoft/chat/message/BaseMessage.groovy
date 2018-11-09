package com.trunksoft.chat.message

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.services.MessageResult
import com.trunksoft.chat.status.ChatMessageStatus
import com.trunksoft.chat.type.MessageType

abstract class BaseMessage extends DateBase {

    Long msgid
    Long msgTime
    Member fromUser
    Member toUser

    MessageType type
    ChatMessageStatus status

    Integer errcode = 0
    String errmsg = "ok"

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
        }
        else {
            if(toUser) {
                this.status = ChatMessageStatus.SEND_FAILURE
            }
            else if(fromUser){
                this.status = ChatMessageStatus.RECEIVE_FAILURE
            }
        }
    }
}
