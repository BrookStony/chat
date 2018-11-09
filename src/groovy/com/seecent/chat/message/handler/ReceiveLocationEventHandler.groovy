package com.seecent.chat.message.handler

import com.seecent.chat.event.LocationEvent
import com.seecent.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveLocationEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveLocationEventHandler)

    /**
     <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[LOCATION]]></Event>
     * <Latitude>23.137466</Latitude>
     * <Longitude>113.352425</Longitude>
     * <Precision>119.385040</Precision>
     * </xml>
     * @param evt
     */
    @Selector(value = "event.location", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def latitude = XmlMsgUtil.attrDoubleValue(xml, "Latitude")
        def longitude = XmlMsgUtil.attrDoubleValue(xml, "Longitude")
        def precision = XmlMsgUtil.attrDoubleValue(xml, "Precision")
        LocationEvent.withTransaction {
            def locationEvent = new LocationEvent(account: context.account, fromUser: context.fromUser)
            locationEvent.eventTime = context.createTime
            locationEvent.locLatitude = latitude
            locationEvent.locLongitude = longitude
            locationEvent.locPrecision = precision
            locationEvent.save(failOnError: true)
            println "locationEvent: " + locationEvent.toString()
        }
        log.info("[after] handle")
    }
}
