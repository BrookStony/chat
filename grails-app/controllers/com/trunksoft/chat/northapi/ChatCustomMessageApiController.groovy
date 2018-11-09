package com.trunksoft.chat.northapi

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.ChatMessage
import com.trunksoft.chat.message.ImageMessage
import com.trunksoft.chat.message.MassMessage
import com.trunksoft.chat.message.TextMessage
import com.trunksoft.chat.status.ChatMessageStatus
import com.trunksoft.chat.util.NorthApiUtil
import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Secured('permitAll')
class ChatCustomMessageApiController {

    private static final TIME_48_HOUR = 48*3600*1000
    private static final TIME_24_HOUR = 24*3600*1000

    static allowedMethods = [send: "POST"]

    def articleService
    def materialManageService
    def wechatMessageService
    def wechatMediaService

    def send() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info("<send> apiAccount[" + apiAccount.id + "] json: " + json)
                    result = NorthApiUtil.checkCustomMsg(json)
                    if(result.isOk()){
                        def account = apiAccount.account
                        def toUserId = json.touser as Long
                        def toUser = Member.findByAccountAndId(account, toUserId)
                        if(toUser) {
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
                                render ([errcode: 4004, errmsg: "由于该用户48小时未与你互动，你不能再主动发信息给他。直到用户下次主动发消息给你才可以对其进行回复"] as JSON)
                                return
                            }
                            else if(json.msgType) {
                                def msgType = json.msgType as String
                                if("TEXT".equalsIgnoreCase(msgType)){
                                    def textMessage = new TextMessage()
                                    textMessage.toUser = toUser
                                    textMessage.content = json.content as String
                                    textMessage.account = account
                                    def msgResult = wechatMessageService.send(account, textMessage)
                                    textMessage.bindResult(msgResult)
                                    textMessage.save(failOnError: true)
                                    render ([status: OK, errmsg: msgResult.message, errcode: msgResult.code] as JSON)
                                    return
                                }
                                else if("IMAGE".equalsIgnoreCase(msgType) && json.materialId){
                                    def image = Picture.get(json.materialId as Long)
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
                                        def msgResult = wechatMessageService.send(account,imageMessage)
                                        imageMessage.bindResult(msgResult)
                                        imageMessage.save(failOnError: true)
                                        render ([status: OK, errmsg: msgResult.message, errcode: msgResult.code] as JSON)
                                        return
                                    }
                                }
                                else if("NEWS".equalsIgnoreCase(msgType) && json.articleId){
                                    def articleId = json.articleId as Long
                                    def article = Article.get(articleId)
                                    if(article) {
                                        def newsMessage = articleService.createNewsMessage(article)
                                        newsMessage.toUser = toUser
                                        def msgResult = wechatMessageService.send(account, newsMessage)
                                        newsMessage.bindResult(msgResult)
                                        newsMessage.save(failOnError: true)
                                        render ([status: OK, errmsg: msgResult.message, errcode: msgResult.code] as JSON)
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("<send> error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
