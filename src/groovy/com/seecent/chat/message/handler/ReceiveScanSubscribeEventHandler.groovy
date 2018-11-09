package com.seecent.chat.message.handler

import com.seecent.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveScanSubscribeEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveSubscribeEventHandler)

    /**
     * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
     * <<FromUserName><![CDATA[FromUser]]></FromUserName>
     * <<CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[subscribe]]></Event>
     * <EventKey><![CDATA[qrscene_123123]]></EventKey>
     * <Ticket><![CDATA[TICKET]]></Ticket>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.scansubscribe", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def xml = evt.data.xml
        def toUserName = XmlMsgUtil.attrTrimValue(xml, "ToUserName")
        def fromUserName = XmlMsgUtil.attrTrimValue(xml, "FromUserName")
        println "handle toUserName: " + toUserName
        println "handle fromUserName: " + fromUserName
        if(toUserName && fromUserName){
            def createTime = XmlMsgUtil.attrLongValue(xml, "CreateTime", System.currentTimeMillis())
            def eventKey = XmlMsgUtil.attrTrimValue(xml, "EventKey")
            def ticket = XmlMsgUtil.attrTrimValue(xml, "Ticket")
            println "handle createTime: " + createTime
        }
        log.info("[after] handle")
    }
}
