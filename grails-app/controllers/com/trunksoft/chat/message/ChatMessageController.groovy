package com.trunksoft.chat.message

import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.type.MessageType
import com.trunksoft.util.DateUtil
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class ChatMessageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def articleService
    def chatMessageDBService
    def materialManageService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if(params.msgTab && params.msgTab != "total"){
            def (endTime, beginTime) = DateUtil.dateBetween(params.msgTab)
            params.beginTime = beginTime?.time
            params.endTime = endTime?.time
        }
        else {
            params.beginTime = params.date('startDate', 'yyyy-MM-dd')?.time
            params.endTime = params.date('endDate', 'yyyy-MM-dd')?.time
        }
        def (chatMessageCount, chatMessages) = chatMessageDBService.list(params)
        response.setHeader('totalItems', chatMessageCount as String)
        respond chatMessages
    }

    /**
     * 查询用户聊天记录
     * @param max
     * @return
     */
    def findUserChatRecords(Integer max){
        params.max = Math.min(max ?: 10, 100)
        def userId = params.long("userId")
        def result = ChatMessage.createCriteria().list(params) {
            or {
                eq("fromUser", Member.proxy(userId))
                eq("toUser", Member.proxy(userId))
            }
            order("msgTime", "desc")
        }
        def data = result.collect {
            def chatMsg = toChatMessage(it)
            if(MessageType.TEXT == it.type){
                def textMessage = TextMessage.get(it.id)
                chatMsg.content = textMessage?.content
            }
            else if(MessageType.IMAGE == it.type){
                if(!it.materialId && it.fromUser){
                    def imageMessage = ImageMessage.get(it.id)
                    if(imageMessage && imageMessage.picurl){
                        chatMsg.picUrl = imageMessage.picurl
                    }
                }
                else {
                    def picture = Picture.get(it.materialId)
                    if(picture && picture.url) {
                        chatMsg.pictureName = picture.name
                        chatMsg.picturePath = picture.path
                        chatMsg.picUrl = materialManageService.serverUrl(picture.url)
                    }
                }
            }
            else if(MessageType.NEWS == it.type){
                def newsMessage = NewsMessage.get(it.id)
                if(newsMessage && newsMessage.articles && newsMessage.articles.size() > 0){
                    def newsArticles = newsMessage.articles.sort { a,b-> a.no <=> b.no}
                    def articles =  newsArticles.collect { article ->
                        toNewsArticle(article)
                    }
                    chatMsg.news = [coverImage: articles[0].picurl, articles: articles]
                }
            }
            return chatMsg
        }
        def messageCount = result.totalCount
        respond ([messages: data, totalCount: messageCount])
    }

    private def toChatMessage(ChatMessage it) {
        def map = [id: it.id, account: [id: it.account.id, name: it.account.name],
         msgid: it.msgid, msgTime: it.msgTime, type: it.type.ordinal(), status: it.status.ordinal()]
        if(it.fromUser){
            map.fromUser = [id: it.fromUser.id, nickName: it.fromUser.nickName, headimgurl: it.fromUser.headimgurl]
        }
        else if(it.toUser) {
            map.toUser = [id: it.toUser.id, nickName: it.toUser.nickName, headimgurl: it.toUser.headimgurl]
        }
        return map
    }

    private def toNewsArticle(NewsArticle it) {
        def url = it.url
        def picurl = materialManageService.serverUrl(it.coverImage)
        if(null != it.articleItemId) {
            url = articleService.articleItemServerUrl(it.articleItemId)
        }
        else if(null != it.articleId) {
            url = articleService.articleServerUrl(it.articleId)
        }
        [id: it.id, no: it.no + 1, title:it.title, description: it.description, url: url, picurl: picurl]
    }

    @Transactional
    def delete(Long id) {
        def messageInstance = ChatMessage.get(id)
        if (messageInstance == null) {
            notFound()
            return
        }
        messageInstance.delete()

        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
