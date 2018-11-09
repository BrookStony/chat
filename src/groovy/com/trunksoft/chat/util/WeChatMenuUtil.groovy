package com.trunksoft.chat.util

import com.trunksoft.chat.type.ChatMenuType
import com.trunksoft.chat.weixin.WeChatMenu
import grails.converters.JSON

class WeChatMenuUtil {
    static String toJson(List<WeChatMenu> menus) {
        def buttons = []
        menus.each{ menu ->
            if(menu.subMenus && !menu.subMenus.empty){
                def subButtons = []
                menu.subMenus.each{
                    if(ChatMenuType.CLICK == it.type && it.eventKey){
                        subButtons << [type: "click", name: it.name, key: it.eventKey]
                    }
                    else if(ChatMenuType.VIEW == it.type && it.url) {
                        subButtons << [type: "view", name: it.name, url: it.url]
                    }
                }
                buttons << [name: menu.name, sub_button: subButtons]
            }
            else {
                if(ChatMenuType.CLICK == menu.type && menu.eventKey){
                    buttons << [type: "click", name: menu.name, key: menu.eventKey]
                }
                else if(ChatMenuType.VIEW == menu.type && menu.url) {
                    buttons << [type: "view", name: menu.name, url: menu.url]
                }
            }
        }
        return ([button: buttons] as JSON).toString()
    }
}
