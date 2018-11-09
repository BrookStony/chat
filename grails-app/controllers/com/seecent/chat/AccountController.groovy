package com.seecent.chat

import com.seecent.chat.common.ExceptionMessage
import com.seecent.chat.jooq.tables.ChatMessageTemplate
import com.seecent.chat.member.Member
import com.seecent.chat.member.MemberGrade
import com.seecent.chat.member.MemberGroup
import com.seecent.chat.member.MemberNumber
import com.seecent.chat.message.ChatMessage
import com.seecent.chat.message.MessageTemplate
import com.seecent.chat.module.bindaccount.BindAccountSetting
import com.seecent.chat.northapi.ApiAccount
import com.seecent.chat.services.exception.AccountException
import grails.converters.JSON
import grails.transaction.NotTransactional
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AccountController {

    def userService
    def wechatAccountService
    def wechatSynchDomainService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = max ?: 100
        def currentUser = userService.currentUser()
        def isSuperAdmin = userService.isSuperAdmin(currentUser)
        def accountIds
        if(!isSuperAdmin) {
            accountIds = currentUser.userGroup?.accounts*.id
        }
        def criteria = Account.where {
            if (params.username) {
                username =~ "%${params.username}%"
            }
            if(!isSuperAdmin && accountIds && accountIds.size() > 0) {
                id in accountIds
            }
        }
        def accountInstanceCount = criteria.count()
        def accountInstanceList = criteria.list(params).collect {
            [id: it.id, name: it.name, weixinId: it.weixinId, weixin: it.weixin,
             username: it.username, password: it.password, type: it.type.ordinal(),
             appId: it.appId, token: it.token, status: it.status.ordinal()]
        }
        response.addHeader('totalItems', accountInstanceCount as String)
        respond accountInstanceList, model: [accountInstanceCount: accountInstanceCount]
    }

    def searchAccounts() {
        def result = Account.createCriteria().list() {
            order("name")
        }
        def accountList = result.collect {
            [id: it.id, name: it.name]
        }
        respond accountList
    }

    def show(Account accountInstance) {
        respond accountInstance
    }

    def create() {
        respond new Account(params)
    }

    @Transactional
    def save(Account accountInstance) {
        if (accountInstance == null) {
            notFound()
            return
        }
        if (accountInstance.hasErrors()) {
            respond accountInstance.errors, view: 'create'
            return
        }

        try{
            accountInstance.save failOnError: true
        }
        catch (AccountException e) {
            def msg = new ExceptionMessage(e.code)
            msg.message = message(code: msg.code(), default: e.message)
            respond msg, [status: UNPROCESSABLE_ENTITY]
        }

        respond accountInstance, [status: CREATED]
    }

    def edit(Account accountInstance) {
        respond accountInstance
    }

    @Transactional
    def update(Account accountInstance) {
        if (accountInstance == null) {
            notFound()
            return
        }
        if (accountInstance.hasErrors()) {
            respond accountInstance.errors, view: 'edit'
            return
        }
        accountInstance.save flush: true
        respond accountInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def accountInstance = Account.get(params.id)
        if (accountInstance == null) {
            notFound()
            return
        }

        Account.withTransaction {
            ChatMessage.where {
                eq("account", accountInstance)
            }.deleteAll()
            MessageTemplate.where {
                eq("account", accountInstance)
            }.deleteAll()
            Member.where {
                eq("account", accountInstance)
            }.deleteAll()
            MemberGroup.where {
                eq("account", accountInstance)
            }.deleteAll()
            MemberGrade.where {
                eq("account", accountInstance)
            }.deleteAll()
            MemberNumber.where {
                eq("account", accountInstance)
            }.deleteAll()
            ApiAccount.where {
                eq("account", accountInstance)
            }.deleteAll()
            BindAccountSetting.where {
                eq("account", accountInstance)
            }
            accountInstance.delete()
        }
        render status: NO_CONTENT
    }

    @NotTransactional
    def synchDomain(Long id) {
        def account = Account.get(id)
        if (account == null) {
            notFound()
            return
        }
        try {
            wechatSynchDomainService.doSynch(account)
        }
        catch (Exception e) {
            log.error(" synchDomain error: " + e.message, e)
        }
        render status: OK
    }

    @NotTransactional
    def refreshAccessToken(Long id) {
        def account = Account.get(id)
        if (account == null) {
            notFound()
            return
        }
        try {
            wechatAccountService.changeAccessToken(account)
        }
        catch (Exception e) {
            log.error(" refreshAccessToken error: " + e.message, e)
        }
        render status: OK
    }

    @NotTransactional
    def queryAccounts(){
        def accountList = Account.list().collect {
            [id: it.id, username: it.username, checked: false]
        }
        respond accountList
    }

    private String currentUsername() {
        return springSecurityService.currentUser.username
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
