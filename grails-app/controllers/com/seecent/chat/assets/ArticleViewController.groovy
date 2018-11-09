package com.seecent.chat.assets

import com.seecent.chat.log.ArticleItemLog
import com.seecent.chat.log.ArticleLog
import com.seecent.chat.type.ArticleEventType

class ArticleViewController {

    def articleManageService
    def materialManageService

    def index() {
        def data = [:]
        def id = params.long("id")
        def item_id = params.long("item_id")
        if(null != id){
            def article = Article.get(id)
            if(article) {
                def readCount = countArticle(id, ArticleEventType.VIEW, false)
                def praiseCount = countArticle(id, ArticleEventType.PRAISE, true)
                def filePath = articleManageService.completeFilePath(article.path)
                def content = articleManageService.read(filePath)
                data = [id                : article.id, account: article.account, type: "Article",
                        title             : article.title, author: article.author,
                        description       : article.description, content: content,
                        coverImage        : article.coverImageUrl?: materialManageService.serverUrl(article.coverImagePath),
                        originalUrl       : article.originalUrl, dateCreated: article.dateCreated.format("yyyy-MM-dd"),
                        coverDisplayInText: article.coverDisplayInText, readCount: readCount + 1, praiseCount: praiseCount]
            }
        }
        else if(null != item_id){
            def articleItem = ArticleItem.get(item_id)
            if(articleItem){
                def readCount = countArticleItem(item_id, ArticleEventType.VIEW, false)
                def praiseCount = countArticleItem(item_id, ArticleEventType.PRAISE, true)
                def filePath = articleManageService.completeFilePath(articleItem.path)
                def content = articleManageService.read(filePath)
                data = [id: articleItem.id, account: articleItem.account, type: "ArticleItem",
                        title: articleItem.title, author: articleItem.author,
                        description: articleItem.description, content: content,
                        coverImage: articleItem.coverImageUrl?: materialManageService.serverUrl(articleItem.coverImagePath),
                        originalUrl: articleItem.originalUrl, dateCreated: articleItem.dateCreated.format("yyyy-MM-dd"),
                        coverDisplayInText: articleItem.coverDisplayInText, readCount: readCount + 1, praiseCount: praiseCount]
            }
        }
        render(view: 'index', model: [article: data])
    }

    /**
     * 点赞
     * @return
     */
    def praise() {
        def id = params.long("id")
        def item_id = params.long("item_id")
        def praiseCount = 0
        if(null != id){
            praiseCount = countArticle(id, ArticleEventType.PRAISE, true)
        }
        else if(null != item_id){
            praiseCount = countArticleItem(item_id, ArticleEventType.PRAISE, true)
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
