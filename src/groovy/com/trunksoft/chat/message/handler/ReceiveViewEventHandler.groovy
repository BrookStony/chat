package com.trunksoft.chat.message.handler

import com.trunksoft.chat.event.ViewEvent
import com.trunksoft.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveViewEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveViewEventHandler)

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[VIEW]]></Event>
     * <EventKey><![CDATA[www.qq.com]]></EventKey>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.view", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def eventKey = XmlMsgUtil.attrTrimValue(xml, "EventKey")
        ViewEvent.withTransaction {
            def viewEvent = new ViewEvent(account: context.account, fromUser: context.fromUser)
            viewEvent.eventTime = context.createTime
            viewEvent.eventKey = eventKey
            viewEvent.save(failOnError: true)
            println "viewEvent: " + viewEvent.toString()
        }
        log.info("[after] handle")
    }
}
