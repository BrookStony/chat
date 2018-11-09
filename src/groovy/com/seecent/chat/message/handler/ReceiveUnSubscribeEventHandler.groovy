package com.seecent.chat.message.handler

import com.seecent.chat.event.UnSubscribeEvent
import com.seecent.chat.member.Member
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveUnSubscribeEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveUnSubscribeEventHandler)

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[unsubscribe]]></Event>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.unsubscribe", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        UnSubscribeEvent.withTransaction {
            def unSubscribeEvent = new UnSubscribeEvent(account: context.account, fromUser: context.fromUser)
            unSubscribeEvent.eventTime = context.createTime
            unSubscribeEvent.save(failOnError: true)
            println "unSubscribeEvent: " + unSubscribeEvent.toString()

            def member = Member.get(context.fromUser.id)
            member.subscribe = false
            member.save(failOnError: true)
            println "member: " + member
        }
        log.info("[after] handle")
    }
}