package com.seecent.chat.services

class MassMessageResult {
    Long msgId
    Long msgDataId
    Integer code
    String message

    public MassMessageResult(Integer code, String message) {
        this.code = code
        this.message = message
    }

    public MassMessageResult(Integer code, String message, Long msgId, Long msgDataId) {
        this.code = code
        this.message = message
        this.msgId = msgId
        this.msgDataId = msgDataId
    }

    public static MassMessageResult create(Integer code, String message, Long msgId, Long msgDataId) {
        if(null != msgId && null != code){
            return new MassMessageResult(code, message, msgId, msgDataId)
        }
        else if(null != code){
            return new MassMessageResult(code, message, null, null)
        }
        return new MassMessageResult(4, "fail", null, null)
    }

    public void error(Integer code, String message) {
        this.code = code
        this.message = message
    }

    boolean isOk() {
        if(0 == code){
            return true
        }
        return false
    }
}
