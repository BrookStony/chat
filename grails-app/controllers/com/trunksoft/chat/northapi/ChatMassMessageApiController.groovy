package com.trunksoft.chat.northapi

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.MassMessage
import com.trunksoft.chat.services.MassMessageResult
import com.trunksoft.chat.type.MessageType
import com.trunksoft.chat.type.SexType
import com.trunksoft.chat.util.JsonUtil
import com.trunksoft.chat.util.NorthApiUtil
import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND

@Secured('permitAll')
class ChatMassMessageApiController {

    static allowedMethods = [send: "POST"]

    def articleService
    def materialManageService
    def wechatMassMessageService

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
                    result = NorthApiUtil.checkMassMsg(json)
                    if(result.isOk()){
                        def account = apiAccount.account
                        Boolean isToAll = false
                        Long groupId = null
                        def toUser = null
                        if(json.filter) {
                            isToAll = json.filter.isToAll as Boolean
                            groupId = json.filter.groupId as Long
                        }
                        else {
                            toUser = queryMemberOpenIdsByJson(account, json)
                        }
                        MassMessageResult massMessageResult = new MassMessageResult(4, "发送失败！")
                        if(isToAll || null != groupId || (toUser && toUser.size() > 1)) {
                            def msgType = json.msgType as String
                            def content = json.content as String
                            def materialId = json.materialId
                            def articleId = json.articleId
                            if(msgType) {
                                def type = MessageType.valueOf(msgType)
                                def massMessage = new MassMessage(account: account)
                                massMessage.isToAll = isToAll
                                massMessage.groupId = groupId
                                if(json.locationId) {
                                    massMessage.locationId = json.locationId as Long
                                }
                                if(json.sexType) {
                                    massMessage.sexType = json.sexType as Integer
                                }
                                massMessage.toUsers = toUser
                                massMessage.msgTime = System.currentTimeMillis()
                                massMessageResult = bindMassMessage(massMessage, type, content as String, materialId as Long, articleId as Long)
                                if(massMessageResult.isOk()) {
                                    massMessageResult = wechatMassMessageService.send(account, massMessage)
                                }
                                massMessage.bindResult(massMessageResult)
                                massMessage.save(failOnError: true)
                            }
                        }
                        render ([errcode: massMessageResult.code, errmsg: massMessageResult.message] as JSON)
                        return
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

    /**
     *
     * @param account
     * @param json
     * @return
     */
    private def queryMemberOpenIdsByJson(Account account, json) {
        def openIds = []
        if(json.tousers){
            def memberIds = JsonUtil.toLongList(json.tousers)
            if(memberIds.size() > 0){
                openIds = Member.findAllByAccountAndIdInList(account, memberIds).collect {it.openId}
            }
        }
        else if(json.tophones) {
            def phones = JsonUtil.toList(json.tophones)
            if(phones.size() > 0){
                openIds = Member.findAllByAccountAndPhoneInList(account, phones).collect {it.openId}
            }
        }
        else {
            def sendGroup = json.groupId as Long
            def sendSexType = json.sexType as Integer
            def locationType = params.locationType as String
            def locationName = params.locationName as String
            if(null != sendGroup || null != sendSexType || (locationType && locationName)) {
                openIds = Member.createCriteria().list {
                    eq("account", account)
                    if(null != sendSexType && 0 != sendSexType){
                        eq("sex", SexType.codeOf(sendSexType))
                    }
                    if(null != sendGroup) {
                        group {
                            eq("id", sendGroup)
                        }
                    }
                    if(locationType && locationName) {
                        if("city" == locationType) {
                            eq("city", locationName)
                        }
                        else if("province" == locationType) {
                            eq("province", locationName)
                        }
                        else if("country" == locationType) {
                            eq("country", locationName)
                        }
                    }
                }.collect {
                    return it.openId
                }
            }
        }
        return openIds
    }

    private MassMessageResult bindMassMessage(MassMessage massMessage, MessageType type, String content, Long materialId, Long articleId) {
        MassMessageResult massMessageResult = new MassMessageResult(0, "Ok！")
        massMessage.type = type
        if(MessageType.TEXT == type) {
            massMessage.content = content
            if(!content) {
                massMessageResult.error(4001, "content can't be null!")
            }
        }
        else if(MessageType.NEWS == type) {
            massMessage.articleId = articleId
            if(null != articleId) {
                def article = Article.get(articleId)
                if(article) {
                    massMessage.mediaId = article.mediaId
                }
                else {
                    massMessageResult.error(4001, "Article can't be found!")
                }
            }
            else {
                massMessageResult.error(4001, "articleId can't be null!")
            }
        }
        else if(MessageType.IMAGE == type) {
            massMessage.materialId = materialId
            if(null != materialId) {
                def picture = Picture.get(materialId)
                if(picture) {
                    massMessage.mediaId = picture.mediaId
                }
                else {
                    massMessageResult.error(4001, "Picture can't be null!")
                }
            }
            else {
                massMessageResult.error(4001, "materialId can't be null!")
            }
        }
        return massMessageResult
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
