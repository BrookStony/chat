package com.trunksoft.chat.module.bindaccount

import com.trunksoft.chat.Account

class BindAccountInitService {

    static boolean transactional = false

    def grailsApplication

    def init() {
        log.info("<init> start...")
        try {
            def config = grailsApplication.config.chat
            def homeUrl = config.home_url as String
            def accounts = Account.list()
            accounts.each {
                if(!BindAccountSetting.countByAccount(it)) {
                    def bindAccountSetting = new BindAccountSetting(account: it)
                    bindAccountSetting.bindUrl = join(homeUrl, "bindAccount/auth", "/")
                    bindAccountSetting.bindLabel = "点击这里，立即绑定账号"
                    bindAccountSetting.bindMsg = "点击下面链接完成账号绑定，轻松绑定即可查询订单信息。"
                    bindAccountSetting.unBindMsg = "您还没有绑定账号信息，请点击下面链接完成账号绑定。"
                    bindAccountSetting.bindSuccessMsg = "账号绑定成功！"
                    bindAccountSetting.bindFailureMsg = "账号绑定失败，请点击菜单“账号绑定”或回复文本“账号绑定”重试一次。"
                    bindAccountSetting.token = "T87nb19iLsH5983"
                    bindAccountSetting.save(failOnError: true)
                }
            }
        }
        catch (Exception e) {
            log.error("<init> error: " + e.message, e)
        }
        log.info("<init> end")
    }

    private String join(String baseUrl, String url, String join) {
        if(baseUrl.endsWith(join) || url.startsWith(join)) {
            return baseUrl + url
        }
        else {
            return baseUrl + join + url
        }
    }
}
