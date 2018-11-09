package com.trunksoft.chat.message.handler

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.ArticleItem
import com.trunksoft.chat.type.ArticleEventType

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
