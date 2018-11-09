package com.trunksoft.chat.message

import com.trunksoft.chat.Account
import com.trunksoft.util.DateUtil
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TemplateMessageController {

    def templateMessageDBService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        if(params.msgTab && params.msgTab != "total"){
            def (endTime, beginTime) = DateUtil.dateBetween(params.msgTab)
            params.beginTime = beginTime?.time
            params.endTime = endTime?.time
        }
        else {
            params.beginTime = params.date('startDate', 'yyyy-MM-dd')?.time
            params.endTime = params.date('endDate', 'yyyy-MM-dd')?.time
        }
        def (templateMessageCount, templateMessages) = templateMessageDBService.list(params)
        response.setHeader('totalItems', templateMessageCount as String)
        respond templateMessages
    }

    @Transactional
    def delete() {
        def messageInstance = TemplateMessage.get(params.id)
        if (messageInstance == null) {
            notFound()
            return
        }
        messageInstance.delete()

        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
