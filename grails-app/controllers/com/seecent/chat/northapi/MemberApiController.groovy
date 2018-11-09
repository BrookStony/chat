package com.seecent.chat.northapi

import com.seecent.chat.member.Member
import com.seecent.chat.util.NorthApiUtil
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class MemberApiController {

    static allowedMethods = [update: "POST"]

    def index() { }

    def listMembers() {
        String accessToken = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def count = Member.count()
                    def members = Member.list().collect {
                        [id: it.id, no: it.noLabel(), name: it.name]
                    }
                    render ([total: count, members: members] as JSON)
                    return
                }
            }
        }
        catch (Exception e) {
            log.error(" listMembers error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    def list() {
        String accessToken  = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def max = params.max
                    def offset = params.offset
                    def count = Member.count()
                    def members = Member.list(params).collect {
                        [id: it.id, no: it.noLabel(), name: it.name, nick_name: it.nickName, sex: it.sex.ordinal(),
                         phone: it.phone, email: it.email, city: it.city, province: it.province]
                    }
                    render ([total: count, max: max, offset: offset, members: members] as JSON)
                    return
                }
            }
        }
        catch (Exception e) {
            log.error(" listMembers error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    def update() {
        String accessToken  = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try {
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def json = request.JSON
                    if(json){
                        def members = json.members
                        if(members && members instanceof JSONArray){
                            for(int i=0; i< members.size(); i++){
                                def m = members.get(i)
                                if(null != m.id){
                                    def memberId = m.id as Long
                                    def member = Member.get(memberId)
                                    if(member){
                                        if(null != m.name) {
                                            member.name = m.name
                                        }
                                        if(null != m.phone) {
                                            member.phone = m.phone
                                        }
                                        if(null != m.email) {
                                            member.email = m.email
                                        }
                                        member.save(failOnError: true)
                                    }
                                }
                            }
                        }
                    }
                    else {
                        result = NorthApiUtil.requestJsonError()
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" update error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

    def show() {
        String accessToken  = params.access_token
        def result = NorthApiUtil.checkAccessToken(accessToken)
        try{
            Long id = params.long("id")
            if(null == id){
                result = NorthApiUtil.missParams("id")
                render ([errcode: result.code, errmsg: result.message] as JSON)
                return
            }
            if(result.isOk()){
                def apiAccount = ApiAccount.findByAccessToken(accessToken)
                result = NorthApiUtil.checkApiAccount(apiAccount)
                if(result.isOk()){
                    def m = Member.get(id)
                    if(m){
                        def member = [id: m.id, no: m.noLabel(), name: m.name, nick_name: m.nickName, sex: m.sex.ordinal(),
                                      phone: m.phone, email: m.email, city: m.city, province: m.province]
                        render ([member: member] as JSON)
                        return
                    }
                    else {
                        result = NorthApiUtil.notFound("Member[id: ${id}]".toString())
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" listMembers error", e)
            result = NorthApiUtil.exception(null)
        }
        render ([errcode: result.code, errmsg: result.message] as JSON)
    }

}
