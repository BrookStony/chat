package com.seecent.chat.util

class ChatNickNameUtil {

    static String toNickName(value) {
        fromWechat(toString(value))
    }

    static String fromWechat(String name) {
        return name
    }

    static String toWechat(String name) {
        return name
    }

    static String checkNickName(value) {
        String nickName = toString(value)
        if(-1 != nickName.indexOf("\\x")){
            nickName = nickName.replaceAll("\\\\x", "&x")
        }
        return nickName
    }

    static String toString(value) {
        return toString(value, "UTF-8")
    }

    static String toString(value, String charset) {
        if(value){
            return new String(value.toString().getBytes("ISO-8859-1"), charset)
        }
        return value
    }

}
