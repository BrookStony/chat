package com.seecent.chat.message.handler

import com.seecent.chat.Account
import com.seecent.chat.assets.Article
import com.seecent.chat.assets.ArticleItem
import com.seecent.chat.type.ArticleEventType

class ArticleEventContext {
    Account account
    Long dateTime
    Article article
    ArticleItem articleItem
    ArticleEventType type
    String clientip
    String openId
    String scope
    String unionId
}
