package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.chat.status.ArticleItemStatus
import com.seecent.chat.status.ArticleStatus
import com.seecent.chat.util.Uploader
import com.seecent.util.DateUtil
import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class ArticleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

    def articleManageService
    def materialManageService
    def pictureManageService
    def uploadArticleService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.order = params.order ?: "desc"
        def accountId = params.long("accountId")
        def result = Article.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
            ne("status", ArticleStatus.REMOVED)
            order("dateCreated", "desc")
        }
        def articleInstanceCount = result.totalCount
        def articleInstanceList = result.collect {
            toArticle(it, false)
        }
        response.setHeader("totalItems", articleInstanceCount as String)
        respond articleInstanceList
    }

    private def toArticle(Article it, boolean loadContent = true) {
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

    private def toArticleItems(articleItems, boolean loadContent = true) {
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

    private def toArticleItem(ArticleItem item, boolean loadContent = true) {
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

    private def toCoverImage(Article it) {
        [id: it.coverImageId, path: it.coverImagePath, mediaUrl: it.coverImageUrl, url: materialManageService.serverUrl(it.coverImagePath)]
    }

    private def toCoverImage(ArticleItem it) {
        [id: it.coverImageId, path: it.coverImagePath, mediaUrl: it.coverImageUrl, url: materialManageService.serverUrl(it.coverImagePath)]
    }

    def edit(){
        def json = request.JSON
        def articleId = json.id as long
        def articleInstance = Article.get(articleId)
        if(null == articleInstance){
            notFound()
            return
        }
        def data = toArticle(articleInstance, true)
        respond data
    }

    /**创建单图文*/
    @Transactional
    def save(){
        def json = request.JSON
        def accountInstance = Account.get(json.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }
        def article = json.article
        def coverImage = article.coverImage
        def articleContent = article.content as String
        def articleInstance = new Article(article as Map)
        articleInstance.account = accountInstance
        articleInstance.coverImageId = coverImage.id as Long
        articleInstance.coverImagePath = coverImage.path as String
        articleInstance.coverImageUrl = coverImage.mediaUrl as String
        articleInstance.content = articleContent
        articleInstance.status = ArticleStatus.CREATED
        //添加微信永久图文素材
        def result = articleManageService.addToWeChat(articleInstance)
        if(result.isOk()) {
            articleManageService.saveContent(articleInstance, accountInstance, articleContent)
            articleInstance.save(flush: true)
        }
        respond status: CREATED
    }

    @Transactional
    def update(){
        def json = request.JSON
        def accountInstance = Account.get(json.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }

        def article = json.article
        def articleInstance = Article.get(article.id as long)
        if(null == articleInstance){
            notFound()
            return
        }
        def coverImage = article.coverImage
        def articleContent = article.content as String
        articleInstance.title = article.title
        articleInstance.author = article.author
        articleInstance.coverImageId = coverImage.id as Long
        articleInstance.coverImagePath = coverImage.path
        articleInstance.coverImageUrl = coverImage.mediaUrl
        articleInstance.description = article.description
        articleInstance.coverDisplayInText = article.coverDisplayInText?: true
        articleInstance.originalUrl = article.originalUrl as String
        articleInstance.content = articleContent
        //修改微信永久图文素材
        def result = articleManageService.updateToWeChat(articleInstance)
        if(result.isOk()) {
            articleManageService.saveContent(articleInstance, accountInstance, articleContent)
            articleInstance.save(flush: true)
        }

        respond articleInstance, [status: OK]
    }

    @Transactional
    def delete(Article articleInstance) {
        if (articleInstance == null) {
            notFound()
            return
        }
        def filePath = articleInstance.path
        def coverImage = articleInstance.coverImagePath
        articleInstance.delete(flush: true)
        articleManageService.deleteFile(filePath)
        materialManageService.deleteFile(coverImage)
        articleManageService.deleteWeChat(articleInstance)
        render status: NO_CONTENT
    }

    def editMulArticle(){
        def json = request.JSON
        def articleId = json.id as long
        def articleInstance = Article.get(articleId)
        if(null == articleInstance){
            notFound()
            return
        }
        def data = [article: toArticle(articleInstance, true),
                    articleItems: toArticleItems(articleInstance.articleItems, true)
        ]
        respond data
    }

    /**创建多图文*/
    @Transactional
    def saveMulArticle(){
        def json = request.JSON
        def accountInstance = Account.get(json.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }

        def article = json.article as Map
        def articleItems = json.articleItems as List
        def mainCoverImage = article.coverImage
        def articleContent = article.content as String
        def articleInstance = new Article(article)
        articleInstance.account = accountInstance
        articleInstance.coverImageId = mainCoverImage.id as Long
        articleInstance.coverImagePath = mainCoverImage.path as String
        articleInstance.coverImageUrl = mainCoverImage.mediaUrl
        articleInstance.content = articleContent
        articleInstance.status = ArticleStatus.CREATED

        /**关联Article 的ArticleItem*/
        articleItems.eachWithIndex{ articleItem, int i ->
            def articleItemContent = articleItem.content as String
            def articleItemInstance = new ArticleItem(articleItem as Map)
            articleItemInstance.no = i
            articleItemInstance.account = accountInstance
            articleItemInstance.coverImageId = articleItem.coverImage.id as Long
            articleItemInstance.coverImagePath = articleItem.coverImage.path
            articleItemInstance.coverImageUrl = articleItem.coverImage.mediaUrl
            articleItemInstance.content = articleItemContent
            articleItemInstance.status = ArticleItemStatus.CREATED
            articleInstance.addToArticleItems(articleItemInstance)
        }

        //添加微信永久图文素材
        def result = articleManageService.addToWeChat(articleInstance)
        if(result.isOk()) {
            articleInstance.articleItems?.each { ArticleItem articleItemInstance ->
                articleManageService.saveContent(articleItemInstance, accountInstance, articleItemInstance.content)
            }
            articleManageService.saveContent(articleInstance, accountInstance, articleContent)
            articleInstance.save(flush: true)
        }

        respond status: CREATED
    }

    @Transactional
    def updateMulArticle(){
        def json = request.JSON
        def accountInstance = Account.get(json.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }

        def article = json.article as Map
        def articleItems = json.articleItems as List
        def articleId = article.id as long
        def articleInstance = Article.get(articleId)
        if(null == articleInstance){
            notFound()
            return
        }

        def articleContent = article.content as String
        articleInstance.title = article.title as String
        articleInstance.author = article.author as String
        articleInstance.coverImageId = article.coverImage.id as Long
        articleInstance.coverImagePath = article.coverImage.path as String
        articleInstance.coverImageUrl = article.coverImage.mediaUrl
        articleInstance.originalUrl = article.originalUrl as String
        articleInstance.content = articleContent

        def deleteArticleItemIds = []
        articleInstance.articleItems?.each {
            deleteArticleItemIds << it.id
        }

        articleInstance.articleItems?.clear()

        articleItems?.eachWithIndex { item, int i ->
            def articleItemContent = item.content as String
            def articleItemCoverImage = item.coverImage
            def articleItemInstance
            def id = item.id as Long
            if(id != null){
                deleteArticleItemIds.remove(id)
                articleItemInstance = ArticleItem.get(id)
            }
            else {
                articleItemInstance = new ArticleItem()
            }
            articleItemInstance.no = i
            articleItemInstance.account = accountInstance
            articleItemInstance.title = item.title as String
            articleItemInstance.author = item.author as String
            articleItemInstance.coverImageId = articleItemCoverImage.id as Long
            articleItemInstance.coverImagePath = articleItemCoverImage.path as String
            articleItemInstance.coverImageUrl = articleItemCoverImage.mediaUrl
            articleItemInstance.originalUrl = item.originalUrl as String
            articleItemInstance.content = articleItemContent
            articleInstance.addToArticleItems(articleItemInstance)
            //修改微信永久图文素材
            def result = articleManageService.updateToWeChat(articleItemInstance)
            if(result.isOk()) {
                articleManageService.saveContent(articleItemInstance, accountInstance, articleItemInstance.content)
                articleItemInstance.save(flush: true)
            }
        }

        if(deleteArticleItemIds.size() > 0){
            ArticleItem.where {
                id in deleteArticleItemIds
            }.deleteAll()
        }

        //修改微信永久图文素材
        def result = articleManageService.updateToWeChat(articleInstance)
        if(result.isOk()) {
            articleManageService.saveContent(articleInstance, accountInstance, articleContent)
            articleInstance.save(flush: true)
        }

        respond articleInstance, [status: OK]
    }

    def upload(){
        def accountInstance = Account.get(params.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }

        def result = [code: 0]
        def pictureInstance = uploadArticleService.upload(accountInstance, request)
        if(pictureInstance) {
            result = [code: 1, picture: [id: pictureInstance.id, name: pictureInstance.name,
            path: pictureInstance.url, mediaUrl: pictureInstance.mediaUrl,
            url: materialManageService.serverUrl(pictureInstance.url)]]
        }
        render (result as JSON)
    }

    def imageUpload(){
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        Uploader up = new Uploader();
        up.setSavePath("upload");
        String[] fileType = [".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"];
        up.setAllowFiles(fileType);
        up.setMaxSize(300*1024); //单位KB
        //TODO 这个account该怎么传进来了？？
        def account = Account.get(1)
        uploadArticleService.uploadUMEditorPicture(account, up, request)

        String callback = request.getParameter("callback");
        def url = up.getUrl().replace("\\","/")
        String result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": \""+ up.getSize() +"\", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+url +"\"}";
        result = result.replaceAll( "\\\\", "\\\\" );
        if( callback == null ){
            render result
        }else{
            render("<script>"+ callback +"(" + result + ")</script>")
        }
    }

    @Transactional
    def removeCoverImage(){
        def json = request.JSON
        def articleId = json.articleId as Long
        def coverImageId = json.coverImageId as Long

        def coverImageCount = Article.countByCoverImageId(coverImageId) + ArticleItem.countByCoverImageId(coverImageId)
        if(0 == coverImageCount) {
            def pictureInstance = Picture.get(coverImageId)
            if(null == pictureInstance){
                notFound()
                return
            }
            def filePath = pictureInstance.path
            pictureInstance.delete(flush: true)
            materialManageService.deleteFile(filePath)
        }

        def articleInstance = Article.get(articleId)
        if (null != articleInstance) {
            articleInstance.coverImagePath = null
            articleInstance.coverImageId = null
            articleInstance.save(failOnError: true)
        }

        render status: NO_CONTENT
    }

    @Transactional
    def remove() {
        def json = request.JSON
        def id = json.id as Long
        def articleInstance = Article.get(id)
        if(null == articleInstance){
            notFound()
            return
        }

        //将图文涨停改为已删除状态， 不删除数据
//        articleInstance.status = ArticleStatus.REMOVED
//        articleInstance.articleItems?.each {
//            it.status = ArticleItemStatus.REMOVED
//            it.save(failOnError: true)
//        }
//        articleInstance.save(failOnError: true)

        //删除数据
        if(articleInstance.mediaId) {
            def result = articleManageService.deleteWeChat(articleInstance)
            if(result.isOk()) {
                articleInstance.articleItems?.each {
//                    pictureManageService.delete(it.coverImageId, true)
                    articleManageService.deleteFile(it.path)
                }
                def coverImageId = articleInstance.coverImageId
                def coverImageCount = Article.countByCoverImageId(coverImageId) + ArticleItem.countByCoverImageId(coverImageId)
                if(0 == coverImageCount) {
                    pictureManageService.delete(articleInstance.coverImageId, true)
                }
                articleManageService.deleteFile(articleInstance.path)
                articleInstance.delete(flush: true)
            }
        }
        else {
            articleInstance.articleItems?.each {
//                    pictureManageService.delete(it.coverImageId, true)
                articleManageService.deleteFile(it.path)
            }
            def coverImageId = articleInstance.coverImageId
            def coverImageCount = Article.countByCoverImageId(coverImageId) + ArticleItem.countByCoverImageId(coverImageId)
            if(0 == coverImageCount) {
                pictureManageService.delete(articleInstance.coverImageId, true)
            }
            articleManageService.deleteFile(articleInstance.path)
            articleInstance.delete(flush: true)
        }

        render status: NO_CONTENT
    }

//    def showArticle(){
//        def data = []
//        if(params.id){
//            def article = Article.get(params.id as long)
//            def content = articleManageService.read(article.path)
//            data = [id: article.id, account:article.account,
//                    title: article.title, author: article.author,
//                    description: article.description, content: content,
//                    coverImage: [id: article.coverImageId, path: article.coverImagePath,
//                                 url: materialManageService.serverUrl(article.coverImagePath)],
//                    originalUrl: article.originalUrl,dateCreated: article.dateCreated.format("yyyy-MM-dd"),
//                    coverDisplayInText:article.coverDisplayInText]
//        }
//        if( params.article_item_id){
//            def article_item_id = params.article_item_id as long
//            def articleItem = ArticleItem.get(article_item_id)
//            data = [id: articleItem.id, account:articleItem.account,
//                    title: articleItem.title, author: articleItem.author,
//                    description: articleItem.description, content: articleItem.content,
//                    coverImage: [id: articleItem.coverImageId, path: articleItem.coverImage,
//                                 url: materialManageService.serverUrl(articleItem.coverImage)],
//                    originalUrl: articleItem.originalUrl,dateCreated: DateUtil.format(articleItem.dateCreated),
//                    coverDisplayInText:articleItem.coverDisplayInText]
//        }
//        render(view: 'show',model: [article: data])
//    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
