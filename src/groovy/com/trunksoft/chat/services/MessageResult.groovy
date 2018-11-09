package com.trunksoft.chat.services

class MessageResult {
    Integer msgid
    Integer code
    String message

    public MessageResult(Integer msgid, Integer code, String message) {
        this.msgid = msgid
        this.code = code
        this.message = message
    }

    public static MessageResult create(msgid, code, message) {
        if(null != msgid && null != code){
            return new MessageResult(msgid as Integer, code as Integer, message?.toString())
        }
        else if(null != code){
            return new MessageResult(null, code as Integer, message?.toString())
        }
        return new MessageResult(null, 0, "ok")
    }

    boolean isOk() {
        if(0 == code){
            return true
        }
        return false
    }
}
