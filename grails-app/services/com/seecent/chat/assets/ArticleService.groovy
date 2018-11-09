package com.seecent.chat.assets

import com.seecent.chat.member.Member
import com.seecent.chat.message.NewsArticle
import com.seecent.chat.message.NewsMessage

class ArticleService {

    static transactional = false

    def grailsApplication
    def articleManageService
    def materialManageService
    def wechatAuthorizeService

    /**
     *
     * @param it
     * @param loadContent
     * @return
     */
    def toArticle(Article it, boolean loadContent = true) {
        def content = ""
        if(loadContent){
            content = articleManageService.readContent(it)
        }
        def articleItems = toArticleItems(it.articleItems, loadContent)
        boolean isMulArticle = false
        if(articleItems.size() > 0) {
            isMulArticle = true
        }
        [id: it.id, title: it.title, author: it.author,
         description: it.description?: "", content: content,
         coverImage: toCoverImage(it), originalUrl: it.originalUrl,
         account: [id: it.account.id, name: it.account.name],
         dateCreated: it.dateCreated.format("yyyy年MM月dd日"),
         articleItems: articleItems, isMulArticle: isMulArticle]
    }

    def toArticleItems(articleItems, boolean loadContent = true) {
        def articleItemList = []
        articleItems?.each { ArticleItem item ->
            articleItemList << toArticleItem(item, loadContent)
        }
        if(articleItemList.size() > 1){
            articleItemList.sort { a, b ->
                a.no <=> b.no
            }
        }
        return articleItemList
    }

    def toArticleItem(ArticleItem item, boolean loadContent = true) {
        def content = ""
        if(loadContent){
            content = articleManageService.readContent(item)
        }
        [id: item.id, no: item.no, title: item.title, author: item.author,
         description: item.description?: "", content: content,
         coverImage: toCoverImage(item), originalUrl: item.originalUrl,
         account: [id: item.account.id, name: item.account.name],
         dateCreated: item.dateCreated.format("yyyy年MM月dd日")]
    }

    def toCoverImage(Article it) {
        [id: it.coverImageId, path: it.coverImagePath, url: materialManageService.serverUrl(it.coverImagePath)]
    }

    def toCoverImage(ArticleItem it) {
        [id: it.coverImageId, path: it.coverImagePath, url: materialManageService.serverUrl(it.coverImagePath)]
    }

    /**
     * 创建图文消息对象
     * @param article
     */
    def createNewsMessage(Article article) {
        def newsMessage = new NewsMessage()
        newsMessage.account = article.account
        newsMessage.materialId = article.id
        def newsArticle = new NewsArticle()
        newsArticle.articleId = article.id
        newsArticle.title = article.title
        newsArticle.description = article.description
        newsArticle.url = articleOAuthUrl(article)
        newsArticle.picurl = materialManageService.serverUrl(article.coverImagePath)
        newsArticle.viewUrl = articleViewUrl(article.id)
        newsArticle.coverImage = article.coverImagePath
        newsMessage.addToArticles(newsArticle)
        if(article.articleItems){
            article.articleItems.each {
                def nx_newsArticle = new NewsArticle()
                nx_newsArticle.articleId = it.article.id
                nx_newsArticle.articleItemId = it.id
                nx_newsArticle.no = it.no + 1
                nx_newsArticle.title = it.title
                nx_newsArticle.description = it.description
                nx_newsArticle.url =  articleItemOAuthUrl(it)
                nx_newsArticle.picurl = materialManageService.serverUrl(it.coverImagePath)
                nx_newsArticle.viewUrl = articleItemViewUrl(it.id)
                nx_newsArticle.coverImage = it.coverImagePath
                newsMessage.addToArticles(nx_newsArticle)
            }
        }
        return newsMessage
    }

    /**
     *
     * @param id
     * @return
     */
    def articleServerUrl(Long id) {
        def config = grailsApplication.config.chat.assets
        def url = config.articleServer as String
        return url + "?id=" + id
    }

    def articleItemServerUrl(Long id) {
        def config = grailsApplication.config.chat.assets
        def url = config.articleServer as String
        return url + "?item_id=" + id
    }

    def articleViewUrl(Long id) {
        return "articleView?id=" + id
    }

    def articleItemViewUrl(Long id) {
        return "articleView?item_id=" + id
    }

    def articleOAuthRelativeUrl(Article article) {
        return "oauthArticleView?id=" + article.id
    }

    def articleItemOAuthRelativeUrl(ArticleItem articleItem) {
        return "oauthArticleVie?item_id=" + articleItem.id
    }

    def articleOAuthUrl(Article article) {
        return wechatAuthorizeService.createOAuthBaseUrl(article.account, "oauthArticleView?id=${article.id}".toString())
    }

    def articleItemOAuthUrl(ArticleItem articleItem) {
        return wechatAuthorizeService.createOAuthBaseUrl(articleItem.account, "oauthArticleView?item_id=${articleItem.id}".toString())
    }
}
