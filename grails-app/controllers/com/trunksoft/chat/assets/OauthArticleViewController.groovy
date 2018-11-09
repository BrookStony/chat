package com.trunksoft.chat.assets

import com.trunksoft.chat.log.ArticleItemLog
import com.trunksoft.chat.log.ArticleLog
import com.trunksoft.chat.message.handler.ArticleEventContext
import com.trunksoft.chat.type.ArticleEventType

import static org.springframework.http.HttpStatus.*

class OauthArticleViewController {

    def reactor

    def articleService
    def articleManageService
    def materialManageService
    def clientService
    def wechatAuthorizeService

    def index() {
        def data = [:]
        def id = params.long("id")
        def item_id = params.long("item_id")
        def code = params.code as String
        if(null != id){
            def article = Article.get(id)
            if(article) {
                def openId = null
                def unionId = null
                def ip = clientService.clientIP(request)
                log.info("<view> article[${article.id}] clientIP: ${ip}".toString())
                log.info("<view> article[${article.id}] code: ${code}".toString())
                if(code) {
                    def oauthResult = wechatAuthorizeService.authorize(article.account, code)
                    if(oauthResult && oauthResult.isOk()) {
                        openId = oauthResult.openId
                        unionId = oauthResult.unionId
                        def context = new ArticleEventContext(account: article.account, article: article, dateTime: System.currentTimeMillis())
                        context.type = ArticleEventType.VIEW
                        context.clientip = ip
                        context.openId = openId
                        context.unionId = unionId
                        reactor.notify("articleLog.handler", reactor.event.Event.wrap(context))
                    }
                }
                else {
                    def type = params.type
                    log.info("<view> article[${article.id}] redirect type: ${type}".toString())
                    if(!type) {
                        def redirectUrl = articleService.articleOAuthUrl(article) + "&type=share"
                        redirect(url: redirectUrl)
                        return
                    }
                }
                def readCount = countArticle(id, ArticleEventType.VIEW, false)
                def praiseCount = countArticle(id, ArticleEventType.PRAISE, true)
                def filePath = articleManageService.completeFilePath(article.path)
                def content = articleManageService.read(filePath)
                data = [id                : article.id, account: article.account, type: "Article",
                        title             : article.title, author: article.author,
                        description       : article.description, content: content,
                        coverImage        : article.coverImageUrl?: materialManageService.serverUrl(article.coverImagePath),
                        originalUrl       : article.originalUrl, dateCreated: article.dateCreated.format("yyyy-MM-dd"),
                        coverDisplayInText: article.coverDisplayInText,
                        openId: openId, unionId: unionId,
                        readCount: readCount + 1, praiseCount: praiseCount]
            }
        }
        else if(null != item_id){
            def articleItem = ArticleItem.get(item_id)
            if(articleItem){
                def openId = null
                def unionId = null
                def ip = clientService.clientIP(request)
                log.info("<view> articleItem[${articleItem.id}] clientIP: ${ip}".toString())
                if(code) {
                    def oauthResult = wechatAuthorizeService.authorize(articleItem.account, code)
                    if(oauthResult && oauthResult.isOk()) {
                        openId = oauthResult.openId
                        unionId = oauthResult.unionId
                        def context = new ArticleEventContext(account: articleItem.account, articleItem: articleItem, dateTime: System.currentTimeMillis())
                        context.type = ArticleEventType.VIEW
                        context.clientip = ip
                        context.openId = openId
                        context.unionId = unionId
                        reactor.notify("articleItemLog.handler", reactor.event.Event.wrap(context))
                    }
                }
                else {
                    def type = params.type
                    log.info("<view> articleItem[${articleItem.id}] redirect type: ${type}".toString())
                    if(!type) {
                        def redirectUrl = articleService.articleItemOAuthUrl(articleItem) + "&type=share"
                        redirect(url: redirectUrl)
                        return
                    }
                }
                def readCount = countArticleItem(item_id, ArticleEventType.VIEW, false)
                def praiseCount = countArticleItem(item_id, ArticleEventType.PRAISE, true)
                def filePath = articleManageService.completeFilePath(articleItem.path)
                def content = articleManageService.read(filePath)
                data = [id: articleItem.id, account: articleItem.account, type: "ArticleItem",
                        title: articleItem.title, author: articleItem.author,
                        description: articleItem.description, content: content,
                        coverImage: articleItem.coverImageUrl?: materialManageService.serverUrl(articleItem.coverImagePath),
                        originalUrl: articleItem.originalUrl, dateCreated: articleItem.dateCreated.format("yyyy-MM-dd"),
                        coverDisplayInText: articleItem.coverDisplayInText,
                        openId: openId, unionId: unionId,
                        readCount: readCount + 1, praiseCount: praiseCount]
            }
        }
        render(view: 'index', model: [article: data])
    }

    /**
     * 点赞
     * @return
     */
    def praise() {
        def data = [:]
        def id = params.long("id")
        def item_id = params.long("item_id")
        def type = params.type as String
        def openId = params.openId as String
        def unionId = params.unionId as String
        def praiseCount = 0
        if("Article" == type && null != id){
            praiseCount = countArticle(id, ArticleEventType.PRAISE, true)
            def ip = clientService.clientIP(request)
            log.info("<praise> article[${id}] clientIP: ${ip}, openId: ${openId}, unionId: ${unionId}".toString())
            if((openId && "null" != openId) || (unionId && "null" != unionId)) {
                def article = Article.get(id)
                if(article) {
                    def context = new ArticleEventContext(account: article.account, article: article, dateTime: System.currentTimeMillis())
                    context.type = ArticleEventType.PRAISE
                    context.clientip = ip
                    context.openId = openId
                    context.unionId = unionId
                    reactor.notify("articleLog.handler", reactor.event.Event.wrap(context))
                }
            }
        }
        else if("ArticleItem" == type && null != item_id){
            praiseCount = countArticleItem(item_id, ArticleEventType.PRAISE, true)
            def ip = clientService.clientIP(request)
            log.info("<praise> articleItem[${item_id}] clientIP: ${ip}, openId: ${openId}, unionId: ${unionId}".toString())
            if((openId && "null" != openId) || (unionId && "null" != unionId)) {
                def articleItem = ArticleItem.get(item_id)
                if (articleItem) {
                    def context = new ArticleEventContext(account: articleItem.account, articleItem: articleItem, dateTime: System.currentTimeMillis())
                    context.type = ArticleEventType.PRAISE
                    context.clientip = ip
                    context.openId = openId
                    context.unionId = unionId
                    reactor.notify("articleItemLog.handler", reactor.event.Event.wrap(context))
                }
            }
        }
        praiseCount = praiseCount + 1
        render praiseCount
    }

    /**
     * 统计文章阅读次数和点赞次数
     * @param id
     * @param eventType
     * @param openid
     * @return
     */
    private def countArticle(Long id, ArticleEventType eventType, boolean openid) {
        def count = 0
        String sql
        if(openid) {
            sql = "select count(distinct openId) from ArticleLog where articleId=:id and type=:type"
        }
        else {
            sql = "select count(timestamp) from ArticleLog where articleId=:id and type=:type"
        }
        def data = ArticleLog.executeQuery(sql, [id: id, type: eventType.ordinal()])
        if(data && !data.empty) {
            count = data[0]
        }
        return count
    }

    /**
     * 统计文章阅读次数和点赞次数
     * @param id
     * @param eventType
     * @param openid
     * @return
     */
    private def countArticleItem(Long id, ArticleEventType eventType, boolean openid) {
        def count = 0
        String sql
        if(openid) {
            sql = "select count(distinct openId) from ArticleItemLog where articleItemId=:id and type=:type"
        }
        else {
            sql = "select count(timestamp) from ArticleItemLog where articleItemId=:id and type=:type"
        }
        def data = ArticleItemLog.executeQuery(sql, [id: id, type: eventType.ordinal()])
        if(data && !data.empty) {
            count = data[0]
        }
        return count
    }
}