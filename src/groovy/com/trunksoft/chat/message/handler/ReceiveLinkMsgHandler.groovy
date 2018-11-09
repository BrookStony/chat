package com.trunksoft.chat.message.handler

import com.trunksoft.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveLinkMsgHandler implements ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(ReceiveTextMsgHandler)

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1351776360</CreateTime>
     * <MsgType><![CDATA[link]]></MsgType>
     * <Title><![CDATA[公众平台官网链接]]></Title>
     * <Description><![CDATA[公众平台官网链接]]></Description>
     * <Url><![CDATA[url]]></Url>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param evt
     */
    @Selector(value = "message.link", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        println "message.text"
        log.info("[before] handle evt->${evt}")
        def xml = evt.data.xml
        def toUserName = XmlMsgUtil.attrTrimValue(xml, "ToUserName")
        def fromUserName = XmlMsgUtil.attrTrimValue(xml, "FromUserName")
        println "handle toUserName: " + toUserName
        println "handle fromUserName: " + fromUserName
        if(toUserName && fromUserName){
            def createTime = XmlMsgUtil.attrLongValue(xml, "CreateTime", System.currentTimeMillis())
            def content = XmlMsgUtil.attrValue(xml, "Content")
            def msgId = XmlMsgUtil.attrLongValue(xml, "MsgId")
            def title = XmlMsgUtil.attrTrimValue(xml, "Title")
            def description = XmlMsgUtil.attrTrimValue(xml, "Description")
            def url = XmlMsgUtil.attrTrimValue(xml, "Url")
            println "handle createTime: " + createTime
            println "handle content: " + content
            println "handle msgId: " + msgId
        }
        log.info("[after] handle")
    }
}
