package com.trunksoft.chat.message.handler

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.TextMessage
import com.trunksoft.chat.status.ChatMessageStatus
import com.trunksoft.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveTextMsgHandler implements ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(ReceiveTextMsgHandler)

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
    @Selector(value = "message.text", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def content = XmlMsgUtil.attrValue(xml, "Content")
        def msgId = XmlMsgUtil.attrLongValue(xml, "MsgId")
        TextMessage.withTransaction {
            def textMessage = new TextMessage(account: context.account, fromUser: context.fromUser)
            textMessage.msgid = msgId
            textMessage.msgTime = context.createTime
            textMessage.content = content
            textMessage.status = ChatMessageStatus.RECEIVE_SUCCESS
            println "textMessage: " + textMessage
            textMessage.save(failOnError: true)
        }
        log.info("[after] handle")
    }
}
