package com.trunksoft.chat.common

class ExceptionMessage {
    Integer code
    String message

    public ExceptionMessage(Integer code) {
        this.code = code
    }

    String code(){
        return "chat.error.code." + code + ".message"
    }
}
