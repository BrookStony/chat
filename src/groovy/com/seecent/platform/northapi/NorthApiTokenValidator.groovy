package com.seecent.platform.northapi

class NorthApiTokenValidator {

    static NorthApiResult validate(String accessToken) {
        if(!accessToken)
            return NorthApiValidateResult.ACCESS_TOKEN_PARAM_MISS
        if(8 != accessToken.toString().length())
            return NorthApiValidateResult.INVALID_ACCESS_TOKEN
        return NorthApiValidateResult.SUCCESS
    }
}
