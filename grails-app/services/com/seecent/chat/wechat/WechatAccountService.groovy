package com.seecent.chat.wechat

import com.seecent.chat.services.AccountService
import com.seecent.chat.services.ChatApiResult
import com.seecent.chat.services.exception.AccountException
import com.seecent.chat.Account

class WechatAccountService implements AccountService {

    static transactional = false

    String refreshAccessToken(Account account) throws AccountException {
        if(account.isTokenTimeOut()){
            log.info(" refreshAccessToken account[${account.name}]".toString())
            withRest(url: account.apiUrl) {
                def response = get(path: '/token', query: [grant_type: "client_credential", appid: account.appId, secret: account.appSecret])
                def json = response.json
                log.info(" refreshAccessToken json： " + json)
                if(json) {
                    ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                    if(apiResult.isOk()){
                        Account.withTransaction {
                            account.refreshToken = json.access_token
                            account.expiresTime = json.expires_in as Long
                            account.refreshTime = System.currentTimeMillis()
                            account.save(failOnError: true)
                        }
                    }
                    else {
                        throw new AccountException(apiResult.message, apiResult.code)
                    }
                }
            }
        }
        return account.refreshToken
    }

    String changeAccessToken(Account account) throws AccountException {
        log.info(" changeAccessToken account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def response = get(path: '/token', query: [grant_type: "client_credential", appid: account.appId, secret: account.appSecret])
            def json = response.json
            log.info(" changeAccessToken json： " + json)
            if(json) {
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
                    Account.withTransaction {
                        account.refreshToken = json.access_token
                        account.expiresTime = json.expires_in as Long
                        account.refreshTime = System.currentTimeMillis()
                        account.save(failOnError: true)
                    }
                }
                else {
                    throw new AccountException(apiResult.message, apiResult.code)
                }
            }
        }
        return account.refreshToken
    }
}
