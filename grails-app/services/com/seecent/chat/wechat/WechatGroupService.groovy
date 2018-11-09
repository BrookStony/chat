package com.seecent.chat.wechat

import com.seecent.chat.member.MemberGroup
import com.seecent.chat.services.ChatApiResult
import com.seecent.chat.services.ChatGroupService
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account
import com.seecent.chat.util.ChatUtil
import grails.converters.JSON
import wslite.rest.RESTClientException

class WechatGroupService implements ChatGroupService {

    static transactional = false

    @Override
    MemberGroup add(Account account, MemberGroup group) throws ApiErrorException {
        log.info(" add account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data = [group: [name: group.name]] as JSON
            def response = post(path: '/groups/create', query: [access_token: account.refreshToken]) {
                type "application/json"
                text  data.toString()
            }
            def json = response.json
            if(json){
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
                    log.info(" add group: ${group.name}, success! response.json: ".toString() + response.json)
                    group.groupId = json.group.id as Long
                }
                else {
                    log.error(" add group: ${group.name}, error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(apiResult.message, apiResult.code)
                }
            }
        }
        return group
    }

    @Override
    MemberGroup push(Account account, MemberGroup group) throws ApiErrorException {
        log.info(" push account[${account.name}]".toString())
//        try {
            withRest(url: account.apiUrl) {
                def data = [group: [id: group.groupId, name: group.name]] as JSON
                def response = post(path: '/groups/update', query: [access_token: account.refreshToken]) {
                    type "application/json"
                    text data.toString()
                }
                def json = response.json
                if(json){
                    ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                    if(apiResult.isOk()){
                        log.info(" push group: ${group.name}, success! response.json: ".toString() + response.json)
                    }
                    else {
                        log.error(" push group: ${group.name}, error! response.json: ".toString() + response.json)
                        throw new ApiErrorException(json.errmsg as String, json.errcode as Integer)
                    }
                }
            }
//        }
//        catch (RESTClientException hex) {
//            log.error(" push group: " + group.id + ", error: " + hex.message)
//        }
        return group
    }

    @Override
    MemberGroup pull(Account account, Long groupId) throws ApiErrorException {
        log.info(" pull account[${account.name}]".toString())
        MemberGroup group = null
        withRest(url: account.apiUrl) {
            def response = get(path: '/groups/get', query: [access_token: account.refreshToken])
            def json = response.json
            if(json){
                if(json.errcode && json.errmsg){
                    log.error(" pull groupId: ${groupId}, error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(json.errmsg as String, json.errcode as Integer)
                }
                else {
                    log.error(" pull groupId: ${groupId}, success! response.json: ".toString() + response.json)
                }
            }
        }
        return group
    }

    @Override
    ChatApiResult remove(Account account, Long groupId) throws ApiErrorException {
        log.info(" remove account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data = [group: [id: groupId]] as JSON
            def response = delete(path: '/groups/delete', query: [access_token: account.refreshToken]) {
                type "application/json"
                text  data.toString()
            }
            def json = response.json
            if(json){
                log.info(" remove groupId: ${groupId}, response.json: ".toString() + response.json)
                return ChatApiResult.create(json.errcode, json.errmsg)
            }
            else {
                return new ChatApiResult(-1, "failure")
            }
        }
    }

    @Override
    List<MemberGroup> pullAll(Account account) throws ApiErrorException {
        log.info(" pullAll account[${account.name}]".toString())
        List<MemberGroup> groups = null
        withRest(url: account.apiUrl) {
            def response = get(path: '/groups/get', query: [access_token: account.refreshToken])
            def json = response.json
            if(json){
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
                    log.info(" pullAll account[${account.name}] success! response.json: ".toString() + response.json)
                    groups = new ArrayList<MemberGroup>()
                    json.groups?.each {
                        def group = new MemberGroup(name: ChatUtil.toString(it.name))
                        group.groupId = it.id as Long
                        groups.add(group)
                    }
                }
                else {
                    log.error(" pullAll account[${account.name}] error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(apiResult.message, apiResult.code)
                }
            }
        }
        return groups
    }

}
