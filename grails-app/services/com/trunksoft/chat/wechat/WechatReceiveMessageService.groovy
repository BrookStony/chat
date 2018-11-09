package com.trunksoft.chat.wechat

import com.trunksoft.chat.message.handler.ReceiveContext
import com.trunksoft.chat.services.ReceiveMessageService
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.util.XmlMsgUtil

class WechatReceiveMessageService implements ReceiveMessageService {

    static transactional = false

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1348831860</CreateTime>
     * <MsgType><![CDATA[text]]></MsgType>
     * <Content><![CDATA[this is a test]]></Content>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onTextMessage(ReceiveContext context) throws ApiErrorException {
        def xml = context.xml
        def toUserName = XmlMsgUtil.attrTrimValue(xml, "ToUserName")
        def fromUserName = XmlMsgUtil.attrTrimValue(xml, "FromUserName")
        println "toUserName: " + toUserName
        println "fromUserName: " + fromUserName
        if(toUserName && fromUserName){
            def createTime = XmlMsgUtil.attrLongValue(xml, "CreateTime", System.currentTimeMillis())
            def content = XmlMsgUtil.attrValue(xml, "Content")
            def msgId = XmlMsgUtil.attrLongValue(xml, "MsgId")
            println "createTime: " + createTime
            println "content: " + content
            println "msgId: " + msgId
        }
    }

    /**
     *<xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1348831860</CreateTime>
     * <MsgType><![CDATA[image]]></MsgType>
     * <PicUrl><![CDATA[this is a url]]></PicUrl>
     * <MediaId><![CDATA[media_id]]></MediaId>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onImageMessage(ReceiveContext context) throws ApiErrorException {

    }

    /**
     *<xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1357290913</CreateTime>
     * <MsgType><![CDATA[voice]]></MsgType>
     * <MediaId><![CDATA[media_id]]></MediaId>
     * <Format><![CDATA[Format]]></Format>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onVoiceMessage(ReceiveContext context) throws ApiErrorException {

    }

    /**
     *<xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1357290913</CreateTime>
     * <MsgType><![CDATA[video]]></MsgType>
     * <MediaId><![CDATA[media_id]]></MediaId>
     * <ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onVideoMessage(ReceiveContext context) throws ApiErrorException {

    }

    /**
     *<xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1351776360</CreateTime>
     * <MsgType><![CDATA[location]]></MsgType>
     * <Location_X>23.134521</Location_X>
     * <Location_Y>113.358803</Location_Y>
     * <Scale>20</Scale>
     * <Label><![CDATA[位置信息]]></Label>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onLocationMessage(ReceiveContext context) throws ApiErrorException {

    }

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
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onLinkMessage(ReceiveContext context) throws ApiErrorException {

    }

}
