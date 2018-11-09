package com.seecent.chat.module.bindaccount

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class BindAccountSettingController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = BindAccountSetting.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def bindAccountSettingCount = result.totalCount
        def bindAccountSettingList = result.collect {
            return toBindAccountSettingMap(it)
        }
        response.setHeader('totalItems', bindAccountSettingCount as String)
        respond bindAccountSettingList
    }

    private def toBindAccountSettingMap(BindAccountSetting it) {
        def map = [id: it.id, account: [name: it.account.name], token: it.token,
                   bindUrl: it.bindUrl, bindLabel: it.bindLabel,
                   bindMsg: it.bindMsg, unBindMsg: it.unBindMsg,
                   bindSuccessMsg: it.bindSuccessMsg, sendSuccessMsg: it.sendSuccessMsg,
                   bindFailureMsg: it.bindFailureMsg, sendFailureMsg: it.sendFailureMsg]
        return map
    }

    def show(BindAccountSetting bindAccountSettingInstance) {
        respond bindAccountSettingInstance
    }

    def create() {
        respond new BindAccountSetting(params)
    }

    @Transactional
    def save(BindAccountSetting bindAccountSettingInstance) {
        if (bindAccountSettingInstance == null) {
            notFound()
            return
        }
        if (bindAccountSettingInstance.hasErrors()) {
            respond bindAccountSettingInstance.errors, view: 'create'
            return
        }
        bindAccountSettingInstance.save failOnError: true
        respond bindAccountSettingInstance, [status: CREATED]
    }

    def edit(BindAccountSetting bindAccountSettingInstance) {
        respond bindAccountSettingInstance
    }

    @Transactional
    def update(BindAccountSetting bindAccountSettingInstance) {
        if (bindAccountSettingInstance == null) {
            notFound()
            return
        }
        if (bindAccountSettingInstance.hasErrors()) {
            respond bindAccountSettingInstance.errors, view: 'edit'
            return
        }
        bindAccountSettingInstance.save flush: true
        respond bindAccountSettingInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def bindAccountSettingInstance = BindAccountSetting.get(params.id)
        if (bindAccountSettingInstance == null) {
            notFound()
            return
        }
        bindAccountSettingInstance.delete()
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}