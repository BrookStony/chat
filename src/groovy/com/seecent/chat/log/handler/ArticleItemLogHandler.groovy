package com.seecent.chat.message.handler

import com.seecent.chat.log.ArticleItemLog
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ArticleItemLogHandler implements ArticleEventHandler {
    Logger log = LoggerFactory.getLogger(ArticleItemLogHandler)

    @Selector(value = "articleItemLog.handler", reactor = "@reactor")
    void handle(Event<ArticleEventContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        ArticleItemLog.withTransaction {
            def articleItemLog = new ArticleItemLog(accountId: context.account.id, articleItemId: context.articleItem.id)
            articleItemLog.articleId = context.articleItem.article.id
            articleItemLog.timestamp = context.dateTime
            articleItemLog.clientip = context.clientip
            articleItemLog.openId = context.openId
            articleItemLog.unionId = context.unionId
            articleItemLog.type = context.type.ordinal()
            articleItemLog.save(failOnError: true)
        }
        log.info("[after] handle")
    }
}
