package com.seecent.chat.message

import com.seecent.chat.Account
import com.seecent.chat.type.TemplateType
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class MessageTemplateController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = MessageTemplate.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def messageTemplateCount = result.totalCount
        def messageTemplateList = result.collect {
            [id: it.id, title: it.title, type: it.type.name(),
             primaryIndustry: it.primaryIndustry, secondaryIndustry: it.secondaryIndustry,
             templateId: it.templateId]
        }
        response.setHeader('totalItems', messageTemplateCount as String)
        respond messageTemplateList
    }
    def show(MessageTemplate messageTemplateInstance) {
        respond messageTemplateInstance
    }

    def create() {
        respond new MessageTemplate(params)
    }

    @Transactional
    def save(MessageTemplate messageTemplateInstance) {
        if (messageTemplateInstance == null) {
            notFound()
            return
        }
        messageTemplateInstance.type = TemplateType.WECHAT
        if (messageTemplateInstance.hasErrors()) {
            respond messageTemplateInstance.errors, view: 'create'
            return
        }
        messageTemplateInstance.save failOnError: true
        respond messageTemplateInstance, [status: CREATED]
    }

    def edit(MessageTemplate messageTemplateInstance) {
        respond messageTemplateInstance
    }

    @Transactional
    def update(MessageTemplate messageTemplateInstance) {
        if (messageTemplateInstance == null) {
            notFound()
            return
        }
        if (messageTemplateInstance.hasErrors()) {
            respond messageTemplateInstance.errors, view: 'edit'
            return
        }
        messageTemplateInstance.save flush: true
        respond messageTemplateInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def messageTemplateInstance = MessageTemplate.get(params.id)
        if (messageTemplateInstance == null) {
            notFound()
            return
        }
        messageTemplateInstance.delete()
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
