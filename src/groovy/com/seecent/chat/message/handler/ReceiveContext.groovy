package com.seecent.chat.message.handler

import com.seecent.chat.Account
import com.seecent.chat.member.Member

class ReceiveContext {
    String fromUserName
    Account account
    Member fromUser
    Long createTime
    def xml
}
