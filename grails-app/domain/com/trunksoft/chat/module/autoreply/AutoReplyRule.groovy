package com.trunksoft.chat.module.autoreply

import com.trunksoft.chat.Account
import com.trunksoft.chat.type.MessageType
import com.trunksoft.platform.DateBase

class AutoReplyRule extends DateBase {

    static belongsTo = [account: Account]

    String name
    String keyword
    AutoReplyRuleType type = AutoReplyRuleType.KEYWORD
    MessageType messageType = MessageType.TEXT
    String msg
    Long materialId
    Long articleId
    AutoReplyRuleMatchType matchType = AutoReplyRuleMatchType.CONTAIN
    AutoReplyRuleStatus status = AutoReplyRuleStatus.ACTIVATE

    static constraints = {
        keyword nullable: true
        msg nullable: true
        materialId nullable: true
        articleId nullable: true
    }

    static mapping = {
        keyword length: 512
        type enumType: 'ordinal'
        messageType enumType: 'ordinal'
        matchType enumType: 'ordinal'
        status enumType: 'ordinal'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof AutoReplyRule)) return false

        AutoReplyRule that = (AutoReplyRule) o

        if (id != that.id) return false

        return true
    }

    int hashCode() {
        int result
        result = id.hashCode()
        return result
    }
}
