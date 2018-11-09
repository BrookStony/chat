package com.trunksoft.chat.message.handler

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.message.ImageMessage
import com.trunksoft.chat.services.MaterialResult
import com.trunksoft.chat.status.ChatMessageStatus
import com.trunksoft.chat.type.PictureType
import com.trunksoft.chat.util.XmlMsgUtil
import com.trunksoft.chat.wechat.WechatMediaService
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveImageMsgHandler implements ReceiveMessageHandler {
    Logger log = LoggerFactory.getLogger(ReceiveImageMsgHandler)

    @Autowired
    WechatMediaService wechatMediaService

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1348831860</CreateTime>
     * <MsgType><![CDATA[image]]></MsgType>
     * <PicUrl><![CDATA[this is a url]]></PicUrl>
     * <MediaId><![CDATA[media_id]]></MediaId>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param evt
     */
    @Selector(value = "message.image", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def picurl = XmlMsgUtil.attrTrimValue(xml, "PicUrl")
        def mediaId = XmlMsgUtil.attrTrimValue(xml, "MediaId")
        def msgId = XmlMsgUtil.attrLongValue(xml, "MsgId")
        ImageMessage.withTransaction {

            def account = Account.get(context.account.id)

            def imageMessage = new ImageMessage(account: account, fromUser: context.fromUser)
            imageMessage.msgid = msgId
            imageMessage.msgTime = context.createTime
            imageMessage.picurl = picurl
            imageMessage.mediaId = mediaId
            imageMessage.status = ChatMessageStatus.RECEIVE_SUCCESS
            println "imageMessage: " + imageMessage
            imageMessage.save(failOnError: true)
            imageMessage.errors.each {
                println it
            }

            def picture = new Picture(account: account)
            picture.type = PictureType.JPG
            picture.name = mediaId
            picture.mediaId = mediaId
            picture.picurl = picurl
            picture.save(failOnError: true)

            imageMessage.materialId = picture.id
            imageMessage.save(failOnError: true)

            MaterialResult result = wechatMediaService.download(account, picture)
            println "result: " + result.toString()
            picture.bindResult(result)
            picture.save(failOnError: true)
        }
        log.info("[after] handle")
    }
}
