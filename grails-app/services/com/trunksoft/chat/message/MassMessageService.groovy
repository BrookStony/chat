package com.trunksoft.chat.message

import com.trunksoft.chat.status.MassMessageStatus
import grails.transaction.Transactional

class MassMessageService {

    static transactional = false

    @Transactional
    def updateStatus(Long msgId, String status, int totalCount, int filterCount, int sentCount, int errorCount) {
        if(msgId && status) {
            def massMessage = MassMessage.findByMsgId(msgId)
            if(massMessage) {
                if("send success" == status) {
                    massMessage.status = MassMessageStatus.SEND_SUCCESS
                    massMessage.errcode = 0
                    massMessage.errmsg = "send success"
                }
                else if("send fail" == status) {
                    massMessage.status = MassMessageStatus.SEND_FAILURE
                    massMessage.errmsg = "send fail"
                }
                else if(status.contains("err")) {
                    massMessage.status = MassMessageStatus.SEND_ERR
                    massMessage.errmsg = "err"
                    def errnum = status.substring(3, status.length() - 1)
                    massMessage.errnum = errnum as Integer
                }
                massMessage.totalCount = totalCount
                massMessage.filterCount = filterCount
                massMessage.sentCount = sentCount
                massMessage.errorCount = errorCount
                massMessage.save(failOnError: true)
            }
        }
    }
}
