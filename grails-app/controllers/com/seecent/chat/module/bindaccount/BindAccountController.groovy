package com.seecent.chat.module.bindaccount

import com.seecent.chat.Account
import com.seecent.chat.member.Member
import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
@Transactional(readOnly = true)
class BindAccountController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def grailsApplication
    def wechatAuthorizeService
    def bindAccountService
    def chatRestClientService

    def index() {
        def accountId = params.long("accountId")
        def code = params.code as String
        def accountInstance = Account.get(accountId)
        if(null == accountInstance) {
            render(view: 'failure', model: [errcode: 4001, errmsg: "找不到账号！"])
            return
        }
        if(code) {
            def oauthResult = wechatAuthorizeService.authorize(accountInstance, code)
            if(oauthResult && oauthResult.isOk()) {
                def openId = oauthResult.openId
//                def unionId = oauthResult.unionId
                def timestamp = System.currentTimeMillis()
                def config = grailsApplication.config.chat
                def bindAccountToken = config.bindAccountToken as String
                def signature = bindAccountService.createSignature(openId, timestamp, bindAccountToken)
                redirect(action: 'login', params: [accountId: accountId, openId: openId, timestamp: timestamp, signature: signature])
            }
            else {
                render(view: 'failure', model: [errcode: 4001, errmsg: "微信网页授权出错！"])
                return
            }
        }
        else {
            def url = wechatAuthorizeService.createOAuthBaseUrl(accountInstance, "index?accountId=${accountId}".toString())
            redirect(url: url)
        }
    }

    def login() {
        def accountId = params.long("accountId")
        def openId = params.openId
        def timestamp = params.long("timestamp")
        def signature = params.signature
        render(view: 'login', model: [accountId: accountId, openId: openId, timestamp: timestamp, signature: signature])
    }

    @Transactional
    def auth() {
        def result = [errcode: 4904, ermsg: errorMessage(4904)]
        def openId = params.openId as String
        def timestamp = params.long("timestamp")
        def signature = params.signature as String
        def username = params.username as String
        def password = params.password as String
        try {
            log.info("<auth> openId: ${openId}, username: ${username}")
            if(openId && timestamp && signature) {
                if(!bindAccountService.checkTimeout(timestamp)) {
                    if(username && password && username == password) {
                        def config = grailsApplication.config.chat
                        def homeUrl = config.home_url as String
                        if(homeUrl) {
                            def data = ([userId: username] as JSON).toString()
                            chatRestClientService.bindAccount(homeUrl, "/bindAccount/bind", data, [openId: openId, timestamp: timestamp, signature: signature])
                        }
                        result = [errcode: 0, ermsg: errorMessage(4900)]
                    }
                    else {
                        result = [errcode: 4913, errmsg: errorMessage(4913)]
                    }
                }
                else {
                    result = [errcode: 4910, errmsg: errorMessage(4910)]
                }
            }
            else {
                if(!openId) {
                    result = [errcode: 4901, errmsg: errorMessage(4901)]
                }
                else if(!timestamp) {
                    result = [errcode: 4902, errmsg: errorMessage(4902)]
                }
                else if(!signature) {
                    result = [errcode: 4905, errmsg: errorMessage(4905)]
                }
            }
        }
        catch (Exception e) {
            log.error("<auth> openId: ${openId}, username: ${username}, error: " + e.message, e)
            result = [errcode: 4903, errmsg: errorMessage(4903)]
        }
        log.info("<auth> openId: ${openId}, username: ${username}, result: " + result)
        render(view: 'success', model: result)
    }

    @Transactional
    def bind() {
        def result = [errcode: 4904, ermsg: errorMessage(4904)]
        def openId = params.openId as String
        def timestamp = params.long("timestamp")
        def signature = params.signature as String
        def json = request.JSON
        try {
            log.info("<bind> openId: ${openId}, json: ${json}")
            if(openId && timestamp && signature) {
                if(!bindAccountService.checkTimeout(timestamp)) {
                    def userId = json.userId as String
                    log.info("<bind> openId: ${openId}, userId: ${userId}")
                    if(userId) {
                        def member = Member.findByOpenId(openId)
                        if(member) {
                            def bindAccountSetting = BindAccountSetting.findByAccount(member.account)
                            if(bindAccountSetting) {
                                if(bindAccountService.checkSignature(openId, timestamp, signature, bindAccountSetting.token)) {
                                    def memberBindAccount = new MemberBindAccount(account: member.account, member: member)
                                    memberBindAccount.userId = userId
                                    memberBindAccount.phone = json.phone
                                    memberBindAccount.name = json.name
                                    memberBindAccount.status = MemberBindStatus.BIND_SUCCESS
                                    memberBindAccount.save(failOnError: true)
                                    result = [errcode: 0, errmsg: errorMessage(4900)]
                                    bindAccountService.sendBindSucessMsg(member, bindAccountSetting)
                                }
                                else {
                                    result = [errcode: 4911, errmsg: errorMessage(4911)]
                                }
                            }
                        }
                        else {
                            result = [errcode: 4912, errmsg: errorMessage(4912, [openId])]
                        }
                    }
                    else {
                        result = [errcode: 4908, errmsg: errorMessage(4908)]
                    }
                }
                else {
                    result = [errcode: 4910, errmsg: errorMessage(4910)]
                }
            }
            else {
                if(!openId) {
                    result = [errcode: 4901, errmsg: errorMessage(4901)]
                }
                else if(!timestamp) {
                    result = [errcode: 4902, errmsg: errorMessage(4902)]
                }
                else if(!signature) {
                    result = [errcode: 4905, errmsg: errorMessage(4905)]
                }
            }
        }
        catch (Exception e) {
            log.error("<bind> openId: ${openId}, error: " + e.message, e)
            result = [errcode: 4903, errmsg: errorMessage(4903)]
        }
        log.info("<bind> openId: ${openId}, result: " + result)
        render(result as JSON)
    }

    private String errorMessage(int code, params = null) {
        if(params) {
            return message(code: "chat.error.code." + code + ".message", params)
        }
        return message(code: "chat.error.code." + code + ".message")
    }

    @Transactional
    def test() {
        def member = Member.findByOpenId("o_yizjh_NxiH6SS8A9uPJ0rvIB_A")
        if(member) {
            def account = member.account
            def bindAccountSetting = BindAccountSetting.findByAccount(account)
            if(bindAccountSetting) {
                bindAccountService.sendBindMsg(member, bindAccountSetting)
            }
        }
    }
}
