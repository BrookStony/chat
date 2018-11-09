package com.seecent.chat.message.handler

import com.seecent.chat.log.ArticleLog
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ArticleLogHandler implements ArticleEventHandler {
    Logger log = LoggerFactory.getLogger(ArticleLogHandler)

    @Selector(value = "articleLog.handler", reactor = "@reactor")
    void handle(Event<ArticleEventContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        ArticleLog.withTransaction {
            def articleLog = new ArticleLog(accountId: context.account.id, articleId: context.article.id)
            articleLog.timestamp = context.dateTime
            articleLog.clientip = context.clientip
            articleLog.openId = context.openId
            articleLog.unionId = context.unionId
            articleLog.type = context.type.ordinal()
            articleLog.save(failOnError: true)
        }
        log.info("[after] handle")
    }
}