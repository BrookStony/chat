package com.trunksoft.platform.system

import com.trunksoft.chat.Account
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class UserGroupController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def criteria = UserGroup.where {
            if (params.filter) {
                name =~ "%${params.filter}%"
            }
        }
        def userGroupCount = criteria.count()
        def userGroupList = criteria.collect {
            [id: it.id, name: it.name, accounts: it.accounts.collect { account ->
                [id: account.id, name: account.name]
            }]
        }
        response.setHeader('totalItems', userGroupCount as String)
        respond userGroupList
    }

    def searchUserGroups() {
        def userGroupList = UserGroup.listOrderByName().collect {
            [id: it.id, name: it.name]
        }
        respond userGroupList
    }

    def show(UserGroup userGroupInstance) {
        respond userGroupInstance
    }

    def create() {
        respond new UserGroup(params)
    }

    @Transactional
    def save(UserGroup userGroupInstance) {
        if (userGroupInstance == null) {
            notFound()
            return
        }
        userGroupInstance.allAcount = userGroupInstance.accounts && userGroupInstance.accounts.size() == Account.count()
        if (userGroupInstance.hasErrors()) {
            respond userGroupInstance.errors, view: 'create'
            return
        }
        userGroupInstance.save failOnError: true
        respond userGroupInstance, [status: CREATED]
    }

    def edit(UserGroup userGroupInstance) {
        respond userGroupInstance
    }

    @Transactional
    def update(UserGroup userGroupInstance) {
        if (userGroupInstance == null) {
            notFound()
            return
        }
        userGroupInstance.allAcount = userGroupInstance.accounts && userGroupInstance.accounts.size() == Account.count()
        if (userGroupInstance.hasErrors()) {
            respond userGroupInstance.errors, view: 'edit'
            return
        }
        userGroupInstance.save flush: true
        respond userGroupInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def userGroupInstance = UserGroup.get(params.id)
        if (userGroupInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}