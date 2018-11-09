package com.seecent.chat.member

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class MemberGroupController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = MemberGroup.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def memberGroupCount = result.totalCount
        def memberGroupList = result.collect {
            return toMemberGroupMap(it)
        }
        response.setHeader('totalItems', memberGroupCount as String)
        respond memberGroupList
    }

    def search() {
        def accountId = params.long("accountId")
        def result = MemberGroup.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
            order("groupId")
        }
        def memberGroupCount = result.totalCount
        def memberGroupList = result.collect {
            return toMemberGroupMap(it)
        }
        response.setHeader('totalItems', memberGroupCount as String)
        respond memberGroupList
    }

    def toMemberGroupMap(MemberGroup it) {
        [id: it.id, name: it.name, groupId: it.groupId]
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
