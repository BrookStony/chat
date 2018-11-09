package com.trunksoft.chat.wechat

import com.trunksoft.chat.message.handler.ReceiveContext
import com.trunksoft.chat.services.ReceiveEventService
import com.trunksoft.chat.services.exception.ApiErrorException

class WechatReceiveEventService implements ReceiveEventService {

    static transactional = false

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[subscribe]]></Event>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onSubscribeEvent(ReceiveContext context) throws ApiErrorException {

    }

    /**
     * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
     * <<FromUserName><![CDATA[FromUser]]></FromUserName>
     * <<CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[subscribe]]></Event>
     * <EventKey><![CDATA[qrscene_123123]]></EventKey>
     * <Ticket><![CDATA[TICKET]]></Ticket>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onScanSubscribeEvent(ReceiveContext context) throws ApiErrorException {

    }

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[SCAN]]></Event>
     * <EventKey><![CDATA[SCENE_VALUE]]></EventKey>
     * <Ticket><![CDATA[TICKET]]></Ticket>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onScanEvent(ReceiveContext context) throws ApiErrorException {

    }

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[LOCATION]]></Event>
     * <Latitude>23.137466</Latitude>
     * <Longitude>113.352425</Longitude>
     * <Precision>119.385040</Precision>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onLocationEvent(ReceiveContext context) throws ApiErrorException {

    }

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[CLICK]]></Event>
     * <EventKey><![CDATA[EVENTKEY]]></EventKey>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onClickEvent(ReceiveContext context) throws ApiErrorException {

    }

    /**
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[VIEW]]></Event>
     * <EventKey><![CDATA[www.qq.com]]></EventKey>
     * </xml>
     * @param xml
     * @throws ApiErrorException
     */
    @Override
    void onViewEvent(ReceiveContext context) throws ApiErrorException {

    }

}
