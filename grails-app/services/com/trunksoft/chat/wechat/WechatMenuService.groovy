package com.trunksoft.chat.wechat

import com.trunksoft.chat.services.ChatApiResult
import com.trunksoft.chat.services.ChatMenuService
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account
import com.trunksoft.chat.type.ChatMenuType
import com.trunksoft.chat.util.ChatUtil
import com.trunksoft.chat.util.WeChatMenuUtil
import com.trunksoft.chat.weixin.WeChatMenu

class WechatMenuService implements ChatMenuService {

    static transactional = false

    def wechatAccountService

    @Override
    ChatApiResult create(Account account, List<WeChatMenu> menus) throws ApiErrorException {
        def menuJson = WeChatMenuUtil.toJson(menus)
        log.info(" create account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/menu/create', query: params) {
                type "application/json"
                text menuJson
            }
            def json = response.json
            log.info(" create menuJson: " + menuJson + ", response.json: " + json)
            if(json){
                return ChatApiResult.create(json.errcode, json.errmsg)
            }
            else {
                return new ChatApiResult(-1, "failure")
            }
        }
    }

    @Override
    ChatApiResult remove(Account account) throws ApiErrorException {
        log.info(" remove account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = get(path: '/menu/delete', query: params)
            def json = response.json
            println "json: " + json
            if(json){
                log.info(" remove response.json: " + json)
                return ChatApiResult.create(json.errcode, json.errmsg)
            }
            else {
                return new ChatApiResult(-1, "failure")
            }
        }
    }

    @Override
    List<WeChatMenu> pull(Account account) throws ApiErrorException {
        log.info(" pull account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = get(path: '/menu/get', query: params)
            def json = response.json
            if(json){
                def result = ChatApiResult.create(json.errcode, json.errmsg)
                if(result.isOk()){
                    log.info(" pull success! response.json: " + json)
                    List<WeChatMenu> menus = new ArrayList<WeChatMenu>()
                    json.menu.button?.eachWithIndex { button, int i ->
                        def code = i + 1
                        def menu = new WeChatMenu(code: code, no: code, name: ChatUtil.toString(button.name), level: 1)
                        if("click" == button.type){
                            menu.eventKey = button.key
                            menu.type = ChatMenuType.CLICK
                        }
                        else {
                            menu.url = button.url
                            menu.type = ChatMenuType.VIEW
                        }
                        def subButtons = button.sub_button
                        if(subButtons && !subButtons.empty){
                            menu.subMenus = new ArrayList<WeChatMenu>()
                            subButtons.eachWithIndex { sub, int j ->
                                def subcode = (i + 1) * 10 + j + 1
                                def subMenu = new WeChatMenu(code: subcode, no: subcode, name: ChatUtil.toString(sub.name), level: 2)
                                if("click" == sub.type){
                                    subMenu.eventKey = sub.key
                                    subMenu.type = ChatMenuType.CLICK
                                }
                                else {
                                    subMenu.url = sub.url
                                    subMenu.type = ChatMenuType.VIEW
                                }
                                menu.subMenus.add(subMenu)
                            }
                        }
                        menus.add(menu)
                    }
                    return menus
                }
                else {
                    log.error(" pull error! response.json: " + json)
                    throw new ApiErrorException(result.message, result.code)
                }
            }
        }
    }
}
