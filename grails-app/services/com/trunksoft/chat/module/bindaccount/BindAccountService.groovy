package com.trunksoft.chat.module.bindaccount

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.TextMessage
import grails.transaction.Transactional

class BindAccountService {

    static boolean transactional = false

    def wechatMessageService

    /**
     * 检查请求url是否过期
     * @param timestamp
     * @return
     */
    boolean checkTimeout(Long timestamp) {
        def costTime = System.currentTimeMillis() - timestamp
        if(costTime > 300000) {
            return true
        }
        return false
    }

    /**
     * 验证参数签名
     * @param openId
     * @param timestamp
     * @param signature
     * @param bindAccountToken
     * @return
     */
    boolean checkSignature(String openId, Long timestamp, String signature, String bindAccountToken) {
        return signature == createSignature(openId, timestamp, bindAccountToken)
    }

    /**
     * 生成参数签名
     * @param openId
     * @param timestamp
     * @param bindAccountToken
     * @return
     */
    String createSignature(String openId, Long timestamp, String bindAccountToken) {
        String uncode = openId + timestamp
        if(bindAccountToken) {
            uncode = uncode + bindAccountToken

        }
        return uncode.encodeAsSHA1()
    }

    def sendBindMsg(Member member, BindAccountSetting setting) {
        log.info("<sendBindMsg> member[${member.id}]")
        def timestamp = System.currentTimeMillis()
        def openId = member.openId
        def token = setting.token
        def url = setting.bindUrl
        def signature = createSignature(openId, timestamp, token)
        url = url + "?openId=" + openId + "&timestamp=" + timestamp + "&signature=" + signature
        def content = setting.bindMsg
        content = content + "\n\n"
        content = content + "<a href=\"" + url + "\">" + setting.bindLabel + "</a>"
        def textMessage = new TextMessage(account: member.account, toUser: member, content: content)
        def msgResult = wechatMessageService.send(member.account, textMessage)
        if (msgResult.isOk()) {
            textMessage.bindResult(msgResult)
            textMessage.save(failOnError: true)
        }
    }

    def sendUnBindMsg(Member member, BindAccountSetting setting) {
        log.info("<sendUnBindMsg> member[${member.id}]")
        def timestamp = System.currentTimeMillis()
        def openId = member.openId
        def token = setting.token
        def url = setting.bindUrl
        def signature = createSignature(openId, timestamp, token)
        url = url + "?openId=" + openId + "&timestamp=" + timestamp + "&signature=" + signature
        def content = setting.unBindMsg
        content = content + "\n\n"
        content = content + "<a href=\"" + url + "\">" + setting.bindLabel + "</a>"
        def textMessage = new TextMessage(account: member.account, toUser: member, content: content)
        def msgResult = wechatMessageService.send(member.account, textMessage)
        if (msgResult.isOk()) {
            textMessage.bindResult(msgResult)
            textMessage.save(failOnError: true)
        }
    }

    def sendBindSucessMsg(Member member, BindAccountSetting setting) {
        log.info("<sendBindSucessMsg> member[${member.id}]")
        def textMessage = new TextMessage(account: member.account, toUser: member, content: setting.bindSuccessMsg)
        def msgResult = wechatMessageService.send(member.account, textMessage)
        if (msgResult.isOk()) {
            textMessage.bindResult(msgResult)
            textMessage.save(failOnError: true)
        }
    }

    def sendBindFailureMsg(Member member, BindAccountSetting setting) {
        log.info("<sendBindFailureMsg> member[${member.id}]")
        def timestamp = System.currentTimeMillis()
        def openId = member.openId
        def token = setting.token
        def url = setting.bindUrl
        def signature = createSignature(openId, timestamp, token)
        url = url + "?openId=" + openId + "&timestamp=" + timestamp + "&signature=" + signature
        def content = setting.bindFailureMsg
        content = content + "\n\n"
        content = content + "<a href=\"" + url + "\">" + setting.bindLabel + "</a>"
        def textMessage = new TextMessage(account: member.account, toUser: member, content: content)
        def msgResult = wechatMessageService.send(member.account, textMessage)
        if (msgResult.isOk()) {
            textMessage.bindResult(msgResult)
            textMessage.save(failOnError: true)
        }
    }

}
