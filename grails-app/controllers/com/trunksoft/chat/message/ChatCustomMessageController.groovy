package com.trunksoft.chat.message

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.status.ChatMessageStatus
import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class ChatCustomMessageController {

    private static final TIME_48_HOUR = 48*3600*1000
    private static final TIME_24_HOUR = 24*3600*1000

    def articleService
    def wechatMessageService
    def wechatMediaService
    def materialManageService

    @Transactional
    def customSend(){
        def json = request.JSON
        def msgType = json.msgType as String
        def content = json.sendContent
        def accountId = json.accountId as Long
        def toUserId = json.toUserId as Long
        def account = Account.get(accountId)
        def toUser = Member.get(toUserId)
        // 当用户主动发消息给公众号的时候，微信将会把消息数据推送给开发者,开发者在一段时间内（目前修改为48小时）可以调用客服消息接口
        def sendMsgEnable = true
        def subscribeTime = toUser.subscribeTime.time
        if((System.currentTimeMillis() - subscribeTime) > TIME_48_HOUR) {
            def chatMessages = ChatMessage.createCriteria().list([max: 1]) {
                eq("fromUser", toUser)
                eq("status", ChatMessageStatus.RECEIVE_SUCCESS)
                order("msgTime","desc")
            }
            if(chatMessages && chatMessages.size() > 0){
                def lastMsgTime = chatMessages[0].msgTime
                if((System.currentTimeMillis() - lastMsgTime) > TIME_48_HOUR) {
                    sendMsgEnable = false
                }
            }
            else {
                sendMsgEnable = false
            }
        }

        if(!sendMsgEnable){
            render([message:"由于该用户48小时未与你互动，你不能再主动发信息给他。直到用户下次主动发消息给你才可以对其进行回复"] as JSON)
        }
        else{
            if(msgType && content){
                if("TEXT".equalsIgnoreCase(msgType)){
                    def textMessage = new TextMessage()
                    textMessage.toUser = toUser
                    textMessage.content = content
                    textMessage.account = account
                    def result = wechatMessageService.send(account, textMessage)
                    textMessage.bindResult(result)
                    textMessage.save(failOnError: true)
                    render ([status: OK, message: result.message,errorCode: result.code] as JSON)
                }
                else if("IMAGE".equalsIgnoreCase(msgType)){
                    def image = Picture.get(content as long)
                    if(image) {
                        def imageMessage = new ImageMessage()
                        imageMessage.toUser = toUser
                        imageMessage.account = account
                        imageMessage.materialId = image.id
                        imageMessage.mediaId = image.mediaId
                        if(!image.mediaId || image.isTimeOut()){
                            //先上传picture获得mediaId
                            def materialResult = wechatMediaService.upload(account, image)
                            if(materialResult.isOk()){
                                imageMessage.mediaId = materialResult.mediaId
                                image.bindResult(materialResult)
                                image.save(flush: true)
                            }
                        }
                        def result = wechatMessageService.send(account,imageMessage)
                        imageMessage.bindResult(result)
                        imageMessage.save(failOnError: true)
                        render ([status: OK, message: result.message,errorCode: result.code] as JSON)
                    }
                }
                else if("NEWS".equalsIgnoreCase(msgType)){
                    def articleId = content as Long
                    def article = Article.get(articleId)
                    if(article) {
                        def newsMessage = articleService.createNewsMessage(article)
                        newsMessage.toUser = toUser
                        def result = wechatMessageService.send(account, newsMessage)
                        newsMessage.bindResult(result)
                        newsMessage.save(failOnError: true)
                        render ([status: OK, message: result.message, errorCode: result.code] as JSON)
                    }
                }
            }
        }
    }
}
