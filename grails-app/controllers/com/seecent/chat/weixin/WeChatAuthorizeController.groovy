package com.seecent.chat.weixin

import com.seecent.chat.Account

class WeChatAuthorizeController {

    def wechatAuthorizeService

    def index() {
        def account = Account.get(1)
        if(account) {
            wechatAuthorizeService.authorize(account, "02191a19acbe9f1abff5c2308bd30ffg")
        }

    }
}
