package com.trunksoft.platform.northapi

import com.trunksoft.platform.common.ErrorCode

class NorthApiResult implements Serializable {

    public static final NorthApiResult SUCCESS = new NorthApiResult(ErrorCode.SUCCESS, "success")

    Integer code
    String message

    public NorthApiResult(Integer code, String message) {
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
