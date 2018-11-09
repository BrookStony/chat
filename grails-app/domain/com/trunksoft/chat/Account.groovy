package com.trunksoft.chat

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.status.AccountStatus
import com.trunksoft.chat.type.AccountType

class Account extends DateBase {

    String name
    String weixinId
    String weixin
    String username
    String password
    String appId
    String appSecret
    String apiUrl
    String token
    String accessToken
    String description
    AccountType type = AccountType.SERVICE
    AccountStatus status = AccountStatus.ACTIVATE

    String refreshToken
    Long refreshTime
    Long expiresTime

    static constraints = {
        weixinId(nullable: true)
        weixin(nullable: true)
        username(nullable: true)
        password(nullable: true)
        appId(nullable: true)
        appSecret(nullable: true)
        apiUrl(nullable: true)
        token(nullable: true)
        accessToken(nullable: true)
        refreshToken(nullable: true)
        refreshTime(nullable: true)
        expiresTime(nullable: true)
        description(nullable: true)
    }

    static mapping = {
        type enumType: 'ordinal'
        status enumType: 'ordinal'
    }

    boolean isTokenTimeOut() {
        if(expiresTime && refreshTime){
            long elapsedTime =  System.currentTimeMillis() - refreshTime
            if(elapsedTime < ((expiresTime - 20) * 1000)){
                return false
            }
        }
        return true
    }
}
