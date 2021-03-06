package com.seecent.chat.message

import com.seecent.platform.DateBase
import com.seecent.chat.Account
import com.seecent.chat.type.TemplateType

class MessageTemplate extends DateBase {

    static belongsTo = [account: Account]

    String templateId
    String title
    String content
    String sample
    String primaryIndustry
    String secondaryIndustry
    TemplateType type

    static constraints = {
        sample(nullable: true)
        primaryIndustry(nullable: true)
        secondaryIndustry(nullable: true)
    }

    static mapping = {
        cache(true)
        type enumType: 'ordinal'
        content length: 1000
        sample length: 1000
        templateId index: 'MessageTemplate_TemplateId_Idx'
    }
}
