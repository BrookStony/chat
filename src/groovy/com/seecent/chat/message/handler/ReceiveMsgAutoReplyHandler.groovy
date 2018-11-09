package com.seecent.chat.message.handler

import com.seecent.chat.module.autoreply.AutoReplyService
import com.seecent.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveMsgAutoReplyHandler implements ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(ReceiveMsgAutoReplyHandler)

    @Autowired
    AutoReplyService autoReplyService

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1348831860</CreateTime>
     * <MsgType><![CDATA[text]]></MsgType>
     * <Content><![CDATA[this is a test]]></Content>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param evt
     */
    @Selector(value = "message.autoReply", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def content = XmlMsgUtil.attrValue(xml, "Content")
        def account = context.account
        def toUser = context.fromUser
        if(content) {
            autoReplyService.executeRules(account, toUser, content)
        }
        log.info("[after] handle")
    }
}
