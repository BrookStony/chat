package com.trunksoft.chat.wechat

import com.trunksoft.chat.member.Member
import com.trunksoft.chat.member.MemberGroup
import com.trunksoft.chat.services.ChatApiResult
import com.trunksoft.chat.services.ChatUserService
import com.trunksoft.chat.services.ListOpenIdsResult
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account
import com.trunksoft.chat.type.SexType
import com.trunksoft.chat.util.ChatNickNameUtil
import com.trunksoft.chat.util.ChatUtil
import grails.converters.JSON

class WechatUserService implements ChatUserService {

    static transactional = false

    @Override
    Member push(Account account, Member member) throws ApiErrorException {

    }

    @Override
    Member pull(Account account, String openId) throws ApiErrorException {
        log.info(" <pull> account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken, openid: openId, lang: "zh_CN"]
            def response = get(path: '/user/info', query: params)
            def json = response.json
            if(json){
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
                    log.info(" <pull> openId[${openId}] success! response.json: ".toString() + response.json)
                    Member member = new Member(openId: openId)
                    member.openId = json.openid
                    Integer subscribe = json.subscribe as Integer
                    if(null != subscribe && subscribe == 1){
                        member.subscribe = true
                        member.nickName = ChatNickNameUtil.toNickName(json.nickname)
                        member.sex = SexType.codeOf(json.sex as Integer)
                        member.language = json.language
                        member.city = ChatUtil.toString(json.city)
                        member.province = ChatUtil.toString(json.province)
                        member.country = ChatUtil.toString(json.country)
                        member.headimgurl = json.headimgurl
                        member.subscribeTime = new Date((json.subscribe_time as Long) * 1000)
                        member.unionid = json.unionid
                    }
                    return member
                }
                else {
                    log.error(" <pull> openId[${openId}] error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(apiResult.message, apiResult.code)
                }
            }
        }
    }

    @Override
    List<Member> pull(Account account, List<String> openIds) throws ApiErrorException {
        List<Member> members = new ArrayList<Member>()
        openIds.each{ openId ->
            def member = pull(account, openId)
            if(member){
                members.add(member)
            }
        }
        return members
    }

    @Override
    ListOpenIdsResult listOpenIds(Account account, String nextOpenId) throws ApiErrorException {
        log.info(" <listOpenIds> account[${account.name}]".toString())
        ListOpenIdsResult result = null
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            if(nextOpenId){
                params.nextopenid = nextOpenId
            }
            def response = get(path: '/user/get', query: params)
            def json = response.json
            if(json){
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
                    result = new ListOpenIdsResult()
                    result.total = json.total as Integer
                    result.count = json.count as Integer
                    result.openIds = json.data?.openid?.collect {
                        return it
                    }
                    result.nextOpenId = json.next_openid
                    log.info(" <listOpenIds> nextOpenId[${nextOpenId}] success! response.json: ".toString() + response.json)
                }
                else {
                    log.error(" <listOpenIds> nextOpenId[${nextOpenId}] error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(apiResult.message, apiResult.code)
                }
            }
        }
        return result
    }

    @Override
    ChatApiResult updateRemark(Account account, Member member) throws ApiErrorException {
        log.info(" <updateRemark> account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data = [openid: member.openId, remark: member.name] as JSON
            def params = [access_token: account.refreshToken]
            def response = post(path: '/user/info/updateremark', query: params) {
                type "application/json"
                text data.toString()
            }
            def json = response.json
            if(json){
                log.info(" <updateRemark> openId: " + member.openId + ", remark: " + member.name + ", response.json: " + response.json)
                return ChatApiResult.create(json.errcode, json.errmsg)
            }
            else {
                return new ChatApiResult(-1, "failure")
            }
        }
    }

    @Override
    Long searchGroup(Account account, Member member) throws ApiErrorException {
        log.info(" searchGroup account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data = [openid: member.openId] as JSON
            def params = [access_token: account.refreshToken]
            def response = post(path: '/groups/getid', query: params) {
                type "application/json"
                text data.toString()
            }
            def json = response.json
            if(json){
                def result =  ChatApiResult.create(json.errcode, json.errmsg)
                if(result.isOk()) {
                    log.info(" <searchGroup> openId: " + member.openId + ", success! response.json: " + response.json)
                    return json.groupid as Long
                }
                else {
                    log.error(" <searchGroup> openId: " + member.openId + ", error! response.json: " + response.json)
                    throw new ApiErrorException(result.message, result.code)
                }
            }
            else {
                return null
            }
        }
    }

    @Override
    ChatApiResult changeGroup(Account account, Member member, MemberGroup group) throws ApiErrorException {
        log.info(" <changeGroup> account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data = [openid: member.openId, to_groupid: group.id] as JSON
            def params = [access_token: account.refreshToken]
            def response = post(path: '/members/update', query: params) {
                type "application/json"
                text data.toString()
            }
            def json = response.json
            if(json){
                log.info(" <changeGroup> openId: " + member.openId + ", response.json: " + response.json)
                return ChatApiResult.create(json.errcode, json.errmsg)
            }
            else {
                return new ChatApiResult(-1, "failure")
            }
        }
    }

}

