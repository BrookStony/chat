package com.trunksoft.chat.util

import com.trunksoft.platform.northapi.NorthApiResult
import com.trunksoft.platform.common.ErrorCode
import com.trunksoft.platform.northapi.NorthApiTokenValidator
import com.trunksoft.platform.northapi.NorthApiValidateResult
import com.trunksoft.platform.status.ApiAccountStatus
import com.trunksoft.chat.northapi.ApiAccount

class NorthApiUtil {

    static boolean checkSignature(String signature, String token, Long timestamp, Integer nonce) {
        if(signature && token && timestamp && nonce) {
            def list = [token, timestamp.toString(), nonce.toString()]
            list.sort {a , b ->
                a<=>b
            }
            def tempStr = list.join("")
            def codeStr = EncodeUtil.sha1(tempStr)
            if(codeStr && signature.equals(codeStr)){
                return true
            }
        }
        return false
    }

    static NorthApiResult checkApiAccount(ApiAccount account) {
        if(account && ApiAccountStatus.ACTIVATE == account.status) {
            return NorthApiResult.SUCCESS
        }
        return NorthApiValidateResult.UNAUTHORIZED_API
    }

    static NorthApiResult checkAccessToken(String accessToken) {
        return NorthApiTokenValidator.validate(accessToken)
    }

    static NorthApiResult missParam(String name) {
        return new NorthApiResult(ErrorCode.PARAM_MISS, "miss " + name + " param")
    }

    static NorthApiResult notFound(String name) {
        return new NorthApiResult(ErrorCode.NOT_FOUND, name + " not found")
    }

    static NorthApiResult parseJsonError() {
        return NorthApiValidateResult.PARSE_JSON_ERROR
    }

    static NorthApiResult parseXmlError() {
        return NorthApiValidateResult.PARSE_JSON_ERROR
    }

    static NorthApiResult requestJsonError() {
        return NorthApiValidateResult.REQUEST_JSON_ERROR
    }

    static NorthApiResult requestXmlError() {
        return NorthApiValidateResult.REQUEST_XML_ERROR
    }

    static NorthApiResult exception(String message) {
        return NorthApiValidateResult.EXCEPTION
    }

    static NorthApiResult checkAlarmMsg(json) {
        if(!json)
            return new NorthApiResult(ErrorCode.REQUEST_JSON_ERROR, "request JSON error")
        if(!json.data)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data param")
        if(!json.data.title)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.title param")
        if(!json.data.content)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.content param")
        if(!json.data.occurtime)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.occurtime param")
        if(!json.data.remark)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.remark param")
        return NorthApiResult.SUCCESS
    }

    static NorthApiResult checkClearAlarmMsg(json) {
        if(!json)
            return new NorthApiResult(ErrorCode.REQUEST_JSON_ERROR, "request JSON error")
        if(!json.data)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data param")
        if(!json.data.title)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.title param")
        if(!json.data.content)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.content param")
        if(!json.data.occurtime)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.occurtime param")
        if(!json.data.recovertime)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.recovertime param")
        if(!json.data.lasttime)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.lasttime param")
        if(!json.data.remark)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss data.remark param")
        return NorthApiResult.SUCCESS
    }

    static NorthApiResult checkMassMsg(json) {
        if(!json)
            return new NorthApiResult(ErrorCode.REQUEST_JSON_ERROR, "request JSON error")
        return NorthApiResult.SUCCESS
    }

    static NorthApiResult checkCustomMsg(json) {
        if(!json)
            return new NorthApiResult(ErrorCode.REQUEST_JSON_ERROR, "request JSON error")
        if(!json.touser)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss touser param")
        if(!json.msgType)
            return new NorthApiResult(ErrorCode.PARAM_MISS, "miss msgType param")
        return NorthApiResult.SUCCESS
    }
}
