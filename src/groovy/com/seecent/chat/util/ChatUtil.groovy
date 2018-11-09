package com.seecent.chat.util

class ChatUtil {

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
