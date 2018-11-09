package com.trunksoft.chat.message.handler

import com.trunksoft.chat.message.MassMessageService
import com.trunksoft.chat.util.XmlMsgUtil
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.event.Event
import reactor.spring.annotation.Selector

@Component
@CompileStatic
class ReceiveMassFinishEventHandler implements ReceiveEventHandler {
    Logger log = LoggerFactory.getLogger(ReceiveMassFinishEventHandler)

    @Autowired
    MassMessageService massMessageService

    /**
     * <xml>
     * <ToUserName><![CDATA[gh_3e8adccde292]]></ToUserName>
     * <FromUserName><![CDATA[oR5Gjjl_eiZoUpGozMo7dbBJ362A]]></FromUserName>
     * <CreateTime>1394524295</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[MASSSENDJOBFINISH]]></Event>
     * <MsgID>1988</MsgID>
     * <Status><![CDATA[sendsuccess]]></Status>
     * <TotalCount>100</TotalCount>
     * <FilterCount>80</FilterCount>
     * <SentCount>75</SentCount>
     * <ErrorCount>5</ErrorCount>
     * </xml>
     */
    @Selector(value = "event.massfinish", reactor = "@reactor")
    void handle(Event<ReceiveContext> evt) {
        log.info("[before] handle evt->${evt}")
        def context = evt.data
        def xml = context.xml
        def msgId = XmlMsgUtil.attrLongValue(xml, "MsgID")
        def status = XmlMsgUtil.attrTrimValue(xml, "Status")
        def totalCount = XmlMsgUtil.attrIntValue(xml, "TotalCount", -1)
        def filterCount = XmlMsgUtil.attrIntValue(xml, "FilterCount", -1)
        def sentCount = XmlMsgUtil.attrIntValue(xml, "SentCount", -1)
        def errorCount = XmlMsgUtil.attrIntValue(xml, "ErrorCount", -1)
        if(msgId && status) {
            massMessageService.updateStatus(msgId, status, totalCount, filterCount, sentCount, errorCount)
        }
        log.info("[after] handle")
    }
}
