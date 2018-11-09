package com.trunksoft.chat.module.autoreply

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.ImageMessage
import com.trunksoft.chat.message.TextMessage
import com.trunksoft.chat.type.MessageType
import grails.converters.JSON
import grails.transaction.Transactional

import java.util.concurrent.ConcurrentHashMap

class AutoReplyService {

    static transactional = false

    def articleService
    def wechatMediaService
    def wechatMessageService

    private static ConcurrentHashMap<Long, AutoReplyRule> ruleCache = new ConcurrentHashMap<Long, AutoReplyRule>()

    void init() {
        log.info("<init> init ruleCache!")
        def rules = AutoReplyRule.findAllByStatus(AutoReplyRuleStatus.ACTIVATE)
        ruleCache.clear()
        for(AutoReplyRule rule : rules) {
            ruleCache.put(rule.id, rule)
        }
    }

    void refresh() {
        log.info("<refresh> refresh ruleCache!")
        def rules = AutoReplyRule.findAllByStatus(AutoReplyRuleStatus.ACTIVATE)
        ruleCache.clear()
        for(AutoReplyRule rule : rules) {
            log.info("rule id: ${rule.id}, account id: ${rule.account.id}, status: ${rule.status}")
            ruleCache.put(rule.id, rule)
        }
    }

    void add(AutoReplyRule rule) {
        log.info("<add> add rule[${rule.id}], name: ${rule.name}")
        ruleCache.put(rule.id, rule)
    }

    void update(AutoReplyRule rule) {
        log.info("<update> update rule[${rule.id}], name: ${rule.name}")
        ruleCache.put(rule.id, rule)
    }

    void remove(AutoReplyRule rule) {
        log.info("<remove> remove rule[${rule.id}], name: ${rule.name}")
        ruleCache.remove(rule.id)
    }

    /**
     *
     * @param account
     * @param toUser
     * @param content
     */
    def executeRules(Account account, Member toUser, String content) {
        log.info("<executeRules> account[${account.name}] toUser: ${toUser.openId}, content: ${content}")
        def autoReplyRule = matches(account, content)
        if(autoReplyRule) {
            reply(account, toUser, autoReplyRule)
        }
    }

    private static AutoReplyRule matches(Account account, String content) {
        for(AutoReplyRule rule : ruleCache.values()) {
            if(AutoReplyRuleStatus.ACTIVATE == rule.status && rule.account.id == account.id) {
                if(rule.keyword && content.contains(rule.keyword)) {
                    return rule
                }
            }
        }
        return null
    }

    /**
     *
     * @param account
     * @param toUser
     * @param rule
     * @return
     */
    @Transactional
    def reply(Account account, Member toUser, AutoReplyRule rule) {
        if(MessageType.TEXT == rule.messageType){
            def textMessage = new TextMessage()
            textMessage.toUser = toUser
            textMessage.content = rule.msg
            textMessage.account = account
            def msgResult = wechatMessageService.send(account,textMessage)
            if(msgResult.isOk()){
                textMessage.bindResult(msgResult)
                textMessage.save(failOnError: true)
            }
            return msgResult
        }
        else if(MessageType.IMAGE == rule.messageType){
            def image = Picture.get(rule.materialId)
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
                if(msgResult.isOk()){
                    imageMessage.bindResult(msgResult)
                    imageMessage.save(failOnError: true)
                }
                return msgResult
            }
        }
        else if(MessageType.NEWS == rule.messageType){
            def article = Article.get(rule.articleId)
            if(article) {
                def newsMessage = articleService.createNewsMessage(article)
                newsMessage.toUser = toUser
                def msgResult = wechatMessageService.send(account, newsMessage)
                if(msgResult.isOk()){
                    newsMessage.bindResult(msgResult)
                    newsMessage.save(failOnError: true)
                }
                return msgResult
            }
        }
        return null
    }
}
