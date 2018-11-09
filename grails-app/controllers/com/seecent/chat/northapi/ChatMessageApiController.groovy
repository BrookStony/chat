package com.seecent.chat.northapi

import com.seecent.chat.member.Member
import com.seecent.chat.message.ChatMessage
import com.seecent.chat.message.TextMessage
import com.seecent.chat.message.TemplateMessage
import com.seecent.chat.message.MessageTemplate
import com.seecent.chat.util.ChatMessageUtil
import com.seecent.chat.util.JsonUtil
import com.seecent.chat.util.NorthApiUtil
import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class ChatMessageApiController {

    def messageFilterService
    def wechatMessageService

    static allowedMethods = [sendAlarmMsg: "POST", sendClearAlarmMsg: "POST", sendTextMsg: "POST", sendTemplateMsg: "POST"]

    def index() { }

    def sendAlarmMsg() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info(" sendAlarmMsg apiAccount[" + apiAccount.id + "] json: " + json)
                    result = NorthApiUtil.checkAlarmMsg(json)
                    if(result.isOk()){
                        def msgIds = [:]
                        def members = queryMembersByJson(json)
                        if(members && members.size() > 0){
                            def messageTemplate = MessageTemplate.findByTemplateId("HoTS3Oia0tav_30iVcg9cm8ueu9uSgKxB0keJLM8l6g")
                            if(messageTemplate){
                                def data = createAlarmMsgData(json.data)
                                def dataJson = (data as JSON).toString()
                                def content = ChatMessageUtil.templateContent(messageTemplate, data)
                                members.each{ member ->
                                    if(messageFilterService.check(member.id, content)) {
                                        def templateMessage = new TemplateMessage(account: member.account, toUser: member, content: content, dataJson: dataJson)
                                        templateMessage.data = data
                                        templateMessage.url = json.url
                                        templateMessage.topColor = "#FF0000"
                                        templateMessage.template = messageTemplate
                                        def msgResult = wechatMessageService.send(member.account, templateMessage)
                                        templateMessage.bindResult(msgResult)
                                        templateMessage.save(failOnError: true)
                                        if(msgResult.isOk()){
                                            msgIds.put(member.id, templateMessage.id)
                                        }
                                    }
                                    else {
                                        log.info(" sendAlarmMsg apiAccount[" + apiAccount.id + "] filter content: " + content)
                                    }
                                }
                            }
                        }
                        render ([errcode: result.code, errmsg: result.message, msgIds: msgIds] as JSON)
                        return
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" sendAlarmMsg error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    private def createAlarmMsgData(data) {
        def msgData = [:]
        msgData.first = [value: data.title]
        msgData.content = [value: data.content]
        msgData.occurtime = [value: data.occurtime]
        msgData.remark = [value: data.remark]
        return msgData
    }

    def sendClearAlarmMsg() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info(" sendClearAlarmMsg apiAccount[" + apiAccount.id + "] json: " + json)
                    result = NorthApiUtil.checkClearAlarmMsg(json)
                    if(result.isOk()){
                        def msgIds = [:]
                        def members = queryMembersByJson(json)
                        if(members && members.size() > 0){
                            def messageTemplate = MessageTemplate.findByTemplateId("Du_JOgHG8ou0CgGPUEQTcBqMa2Akosv_WlTsE9BDFBg")
                            if(messageTemplate){
                                def data = createClearAlarmMsgData(json.data)
                                def dataJson = (data as JSON).toString()
                                def content = ChatMessageUtil.templateContent(messageTemplate, data)
                                members.each{ member ->
                                    if(messageFilterService.check(member.id, content)) {
                                        def templateMessage = new TemplateMessage(account: member.account, toUser: member, content: content, dataJson: dataJson)
                                        templateMessage.data = data
                                        templateMessage.url = json.url
                                        templateMessage.topColor = "#44B549"
                                        templateMessage.template = messageTemplate
                                        def msgResult = wechatMessageService.send(member.account, templateMessage)
                                        templateMessage.bindResult(msgResult)
                                        templateMessage.save(failOnError: true)
                                        if (msgResult.isOk()) {
                                            msgIds.put(member.id, templateMessage.id)
                                        }
                                    }
                                    else {
                                        log.info(" sendClearAlarmMsg apiAccount[" + apiAccount.id + "] filter content: " + content)
                                    }
                                }
                            }
                        }
                        render ([errcode: result.code, errmsg: result.message, msgIds: msgIds] as JSON)
                        return
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" sendClearAlarmMsg error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    private def createClearAlarmMsgData(data) {
        def msgData = [:]
        msgData.first = [value: data.title]
        msgData.content = [value: data.content]
        msgData.occurtime = [value: data.occurtime]
        msgData.recovertime = [value: data.recovertime]
        msgData.lasttime = [value: data.lasttime]
        msgData.remark = [value: data.remark]
        return msgData
    }

    def sendTextMsg() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info(" sendTextMsg apiAccount[" + apiAccount.id + "] json: " + json)
                    def msgIds = [:]
                    def content = json.content as String
                    if(content){
                        def members = queryMembersByJson(json)
                        if(members && members.size() > 0){
                            members.each{ member ->
                                if(messageFilterService.check(member.id, content)) {
                                    def textMessage = new TextMessage(account: member.account, toUser: member, content: content)
                                    def msgResult = wechatMessageService.send(member.account, textMessage)
                                    textMessage.bindResult(msgResult)
                                    textMessage.save(failOnError: true)
                                    if (msgResult.isOk()) {
                                        msgIds.put(member.id, textMessage.id)
                                    }
                                }
                                else {
                                    log.info(" sendTextMsg apiAccount[" + apiAccount.id + "] filter content: " + content)
                                }
                            }
                        }
                    }
                    render ([errcode: result.code, errmsg: result.message, msgIds: msgIds] as JSON)
                    return
                }
            }
        }
        catch (Exception e) {
            log.error(" sendTextMsg error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    def sendTemplateMsg() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info(" sendTemplateMsg apiAccount[" + apiAccount.id + "] json: " + json)
                    if(json.template_id && json.data){
                        def msgIds = [:]
                        def members = queryMembersByJson(json)
                        if(members && members.size() > 0){
                            def messageTemplate = MessageTemplate.findByTemplateId(json.template_id)
                            if(messageTemplate){
                                def data = json.data
                                def dataJson = data.toString()
                                def content = ChatMessageUtil.templateContent(messageTemplate, data)
                                members.each{ member ->
                                    if(messageFilterService.check(member.id, content)) {
                                        def templateMessage = new TemplateMessage(account: member.account, toUser: member, content: content, dataJson: dataJson)
                                        templateMessage.data = data
                                        templateMessage.url = json.url
                                        templateMessage.template = messageTemplate
                                        def msgResult = wechatMessageService.send(member.account, templateMessage)
                                        templateMessage.bindResult(msgResult)
                                        templateMessage.save(failOnError: true)
                                        if (msgResult.isOk()) {
                                            msgIds.put(member.id, templateMessage.id)
                                        }
                                    }
                                    else {
                                        log.info(" sendTemplateMsg apiAccount[" + apiAccount.id + "] filter content: " + content)
                                    }
                                }
                            }
                        }
                        render ([errcode: result.code, errmsg: result.message, msgIds: msgIds] as JSON)
                        return
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" sendTemplateMsg error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    private def queryMembersByJson(json) {
        if(json.tousers){
            def memberIds = JsonUtil.toLongList(json.tousers)
            if(memberIds.size() > 0){
                return Member.findAllByIdInList(memberIds)
            }
        }
        else if(json.tophones) {
            def phones = JsonUtil.toList(json.tophones)
            if(phones.size() > 0){
                return Member.findAllByPhoneInList(phones)
            }
        }
        return null
    }

    def searchMsgStatus() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    log.info(" searchMsgStatus apiAccount[" + apiAccount.id + "] json: " + json)
                    def msgStatus = [:]
                    if(json.msgIds){
                        def msgIds = JsonUtil.toLongList(json.msgIds)
                        if(msgIds.size() > 0){
                            def chatMessages = ChatMessage.findAllByIdInList(msgIds)
                            chatMessages.each{
                                msgStatus.put(it.id, it.status.ordinal())
                            }
                        }
                    }
                    else {
                        result = NorthApiUtil.missParam("msgIds")
                    }
                    render ([errcode: result.code, errmsg: result.message, msgStatus: msgStatus] as JSON)
                    return
                }
            }
        }
        catch (Exception e) {
            log.error(" searchMsgStatus error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

}
