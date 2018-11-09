package com.trunksoft.chat.weixin

import com.trunksoft.platform.DateBase
import com.trunksoft.chat.Account
import com.trunksoft.chat.type.ChatMenuType

class WeChatMenu extends DateBase {

    static belongsTo = [account: Account]

    static transients = ['subMenus']

    WeChatMenu parent
    Integer no
    Integer code
    String name
    Integer level
    String eventKey
    String url
    ChatMenuType type
    List<WeChatMenu> subMenus

    static constraints = {
        parent(nullable: true)
        eventKey(nullable: true, validator: { val, obj ->
            if(val && val.length() > 256 ){
                return false
            }
            return true
        })
        url(nullable: true, validator: { val, obj ->
            if(val && val.length() > 256 ){
                return false
            }
            return true
        })
    }

    static mapping = {
        type enumType: 'ordinal'
        eventKey length: 256
        url length: 256
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("WeChatMenu{id: " + id)
        sb.append(",no: " + no)
        sb.append(",code: " + code)
        sb.append(",name: " + name)
        sb.append(",level: " + level)
        sb.append(",type: " + type)
        sb.append(",eventKey: " + eventKey)
        sb.append(",url: " + url)
        sb.append("}")
    }

}
