package com.seecent.chat.message.handler

import com.seecent.chat.event.SubscribeEvent
import com.seecent.chat.message.TextMessage
import com.seecent.chat.wechat.WechatMessageService
import com.seecent.chat.wechat.WechatSynchDomainService
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveSubscribeEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveSubscribeEventHandler)

    @Autowired
    WechatSynchDomainService wechatSynchDomainService

    @Autowired
    WechatMessageService wechatMessageService

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[subscribe]]></Event>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.subscribe", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        SubscribeEvent.withTransaction {
            def member = wechatSynchDomainService.synchUser(context.account, context.fromUserName)
            context.fromUser = member
            def subscribeEvent = new SubscribeEvent(account: context.account, fromUser: context.fromUser)
            subscribeEvent.eventTime = context.createTime
            subscribeEvent.save(failOnError: true)
            println "subscribeEvent: " + subscribeEvent.toString()
            if(member){
                def content = "${member.nickName}您好！欢迎订阅INMS公众服务号，您的会员编号：${member.noLabel()}."
                def textMessage = new TextMessage(account: member.account, toUser: member, content: content)
                def msgResult = wechatMessageService.send(member.account, textMessage)
                if(msgResult.isOk()){
                    textMessage.bindResult(msgResult)
                    textMessage.save(failOnError: true)
                }
                wechatSynchDomainService.synchUserGroup(context.account, member)
            }
        }
        log.info("[after] handle")
    }
}
