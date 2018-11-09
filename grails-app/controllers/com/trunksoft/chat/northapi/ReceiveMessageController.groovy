package com.trunksoft.chat.northapi

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member
import com.trunksoft.chat.message.handler.ReceiveContext
import com.trunksoft.chat.util.NorthApiUtil
import com.trunksoft.chat.util.XmlMsgUtil
import org.springframework.security.access.annotation.Secured
import reactor.event.Event

@Secured('permitAll')
class ReceiveMessageController {

    def reactor

    def index() {
        def xml = request.XML
        log.info(" receive params: " + params + ", xml: " + xml)
        String echostr = params.echostr
        try {
            if(xml){
                println "xml: " + xml
                def toUserName = XmlMsgUtil.attrTrimValue(xml, "ToUserName")
                def fromUserName = XmlMsgUtil.attrTrimValue(xml, "FromUserName")
                if(toUserName && fromUserName) {
                    def account = Account.findByWeixinId(toUserName)
                    if(account) {
                        def msgType = XmlMsgUtil.attrTrimValue(xml, "MsgType")
                        def event = XmlMsgUtil.attrTrimValue(xml, "Event")
                        def createTime = XmlMsgUtil.attrLongValue(xml, "CreateTime", System.currentTimeMillis())
                        println "msgType: " + msgType
                        println "event: " + event
                        if(msgType){
                            def fromUser = Member.findByOpenId(fromUserName)
                            def context = new ReceiveContext(account: account, fromUserName: fromUserName, fromUser: fromUser, createTime: createTime * 1000, xml: xml)
                            if(event){
                                onEvent(event, context)
                            }
                            else {
                                onMessage(msgType, context)
                            }
                        }
                    }
                }
                render ""
                return
            }
            else {
                Long accountId = params.long("accountId")
                if(accountId){
                    def account = Account.get(accountId)
                    if(account) {
                        String signature = params.signature
                        Long timestamp = params.long("timestamp")
                        Integer nonce = params.int("nonce")
                        println "checkSignature: " + NorthApiUtil.checkSignature(signature, account.token, timestamp, nonce)
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" receive error: " + e.message, e)
        }
        render echostr
    }

    private void onMessage(String msgType, ReceiveContext context) {
        def fromUser = Member.findByOpenId(context.fromUserName)
        if(fromUser){
            context.fromUser = fromUser
            if("text".equals(msgType)){
                reactor.notify("message.text", Event.wrap(context))
                reactor.notify("message.autoReply", Event.wrap(context))
            }
            else if("image".equals(msgType)){
                reactor.notify("message.image", Event.wrap(context))
            }
            else if("voice".equals(msgType)){
                reactor.notify("message.voice", Event.wrap(context))
            }
            else if("video".equals(msgType)){
                reactor.notify("message.video", Event.wrap(context))
            }
            else if("link".equals(msgType)){
                reactor.notify("message.link", Event.wrap(context))
            }
            else if("location".equals(msgType)){
                reactor.notify("message.location", Event.wrap(context))
            }
        }
    }

    private void onEvent(String event, ReceiveContext context) {
        if("subscribe".equals(event)){
            def ticket = XmlMsgUtil.attrTrimValue(context.xml, "Ticket")
            if(ticket){
                if(context.fromUser) {
                    reactor.notify("event.scansubscribe", Event.wrap(context))
                }
            }
            else {
                reactor.notify("event.subscribe", Event.wrap(context))
            }
        }
        else {
            if(context.fromUser){
                if("unsubscribe".equals(event)){
                    reactor.notify("event.unsubscribe", Event.wrap(context))
                }
                else if("CLICK".equals(event)){
                    reactor.notify("event.click", Event.wrap(context))
                }
                else if("VIEW".equals(event)){
                    reactor.notify("event.view", Event.wrap(context))
                }
                else if("SCAN".equals(event)){
                    reactor.notify("event.scan", Event.wrap(context))
                }
                else if("LOCATION".equals(event)){
                    reactor.notify("event.location", Event.wrap(context))
                }
            }
            if("MASSSENDJOBFINISH".equals(event)){
                reactor.notify("event.massfinish", Event.wrap(context))
            }
        }
    }
}
