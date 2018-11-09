package com.trunksoft.chat.message.handler

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member

class ReceiveContext {
    String fromUserName
    Account account
    Member fromUser
    Long createTime
    def xml
}
