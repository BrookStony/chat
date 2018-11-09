package com.trunksoft.chat.module.bindaccount

import com.trunksoft.chat.Account
import com.trunksoft.platform.DateBase

class BindAccountSetting extends DateBase {

    static belongsTo = [account: Account]

    String bindUrl
    String bindLabel
    String bindMsg
    String unBindMsg
    String bindSuccessMsg
    boolean sendSuccessMsg = true
    String bindFailureMsg
    boolean sendFailureMsg = true
    String token

    static constraints = {
        unBindMsg(nullable: true)
        bindSuccessMsg(nullable: true)
        bindFailureMsg(nullable: true)
    }

    static mapping = {
        bindMsg length: 1000
        unBindMsg length: 1000
        sendSuccessMsg length: 1000
        sendFailureMsg length: 1000
    }
}
