package com.seecent.chat.member

import com.seecent.chat.Account
import com.seecent.chat.message.ChatMessage
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class MemberController {

    def wechatUserService
    def wechatSynchDomainService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = Member.createCriteria().list(params) {
            eq("subscribe", true)
            account {
                eq("id", accountId)
            }
        }
        def memberCount = result.totalCount
        def memberList = result.collect {
            [id: it.id, name: it.name, nickName: it.nickName,
             headimgurl: it.headimgurl, sex: it.sex.ordinal(),
             phone: it.phone, email: it.email, weixin: it.weixin, qq: it.qq,
             city: it.city, province: it.province,
             group: [id: it.group?.id, name: it.group?.name]]
        }
        response.setHeader('totalItems', memberCount as String)
        respond memberList
    }
    def show(Member memberInstance) {
        respond memberInstance
    }

    def create() {
        respond new Member(params)
    }

    @Transactional
    def save(Member memberInstance) {
        if (memberInstance == null) {
            notFound()
            return
        }
        if (memberInstance.hasErrors()) {
            respond memberInstance.errors, view: 'create'
            return
        }
        memberInstance.save failOnError: true
        respond memberInstance, [status: CREATED]
    }

    def edit(Member memberInstance) {
        respond memberInstance
    }

    @Transactional
    def update(Member memberInstance) {
        if (memberInstance == null) {
            notFound()
            return
        }
        if (memberInstance.hasErrors()) {
            respond memberInstance.errors, view: 'edit'
            return
        }
        memberInstance.save flush: true
        respond memberInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def memberInstance = Member.get(params.id)
        if (memberInstance == null) {
            notFound()
            return
        }

        Member.withTransaction {
            ChatMessage.where {
                or {
                    eq("fromUser", memberInstance)
                    eq("toUser", memberInstance)
                }
            }.deleteAll()
            memberInstance.delete()
        }
        render status: NO_CONTENT
    }

    @NotTransactional
    def synchMembers() {
        def json = request.JSON
        def id = json.accountId as Long
        def account = Account.get(id)
        println "account: " + account
        if (null == account) {
            notFound()
            return
        }
        try {
            wechatSynchDomainService.doSynch(account)
        }
        catch (Exception e) {
            log.error(" synchDomain error: " + e.message, e)
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
