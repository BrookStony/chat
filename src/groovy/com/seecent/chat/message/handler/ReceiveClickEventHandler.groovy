package com.seecent.chat.message.handler

import com.seecent.chat.event.ClickEvent
import com.seecent.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveClickEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveClickEventHandler)

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[CLICK]]></Event>
     * <EventKey><![CDATA[EVENTKEY]]></EventKey>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.click", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def eventKey = XmlMsgUtil.attrTrimValue(xml, "EventKey")
        ClickEvent.withTransaction {
            def clickEvent = new ClickEvent(account: context.account, fromUser: context.fromUser)
            clickEvent.eventTime = context.createTime
            clickEvent.eventKey = eventKey
            clickEvent.save(failOnError: true)
            println "clickEvent: " + clickEvent
        }
        log.info("[after] handle")
    }
}
