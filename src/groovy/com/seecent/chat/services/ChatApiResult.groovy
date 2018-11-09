package com.seecent.chat.services

class ChatApiResult implements Serializable {
    Integer code
    String message

    public ChatApiResult(Integer code, String message) {
        this.code = code
        this.message = message
    }

    public static ChatApiResult create(code, message) {
        if(null != code){
            return new ChatApiResult(code as Integer, message?.toString())
        }
        return new ChatApiResult(0, "ok")
    }

    boolean isOk() {
        if(0 == code){
            return true
        }
        return false
    }

}
