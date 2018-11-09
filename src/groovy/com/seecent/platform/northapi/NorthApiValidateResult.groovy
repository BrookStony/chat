package com.seecent.platform.northapi

import com.seecent.platform.common.ErrorCode

class NorthApiValidateResult {
    public static final NorthApiResult SYSTEM_BUSY = new NorthApiResult(ErrorCode.SYSTEM_BUSY, "system is busy")
    public static final NorthApiResult SUCCESS = new NorthApiResult(ErrorCode.SUCCESS, "success")
    public static final NorthApiResult ACCESS_TOKEN_PARAM_MISS = new NorthApiResult(ErrorCode.ACCESS_TOKEN_PARAM_MISS, "access_token parameter is missing")
    public static final NorthApiResult INVALID_ACCESS_TOKEN = new NorthApiResult(ErrorCode.INVALID_ACCESS_TOKEN, "invalid access_token")
    public static final NorthApiResult INVALID_TOKEN = new NorthApiResult(ErrorCode.INVALID_TOKEN, "invalid token")
    public static final NorthApiResult TOKEN_EXPIRED = new NorthApiResult(ErrorCode.TOKEN_EXPIRED, "token expired")
    public static final NorthApiResult INVALID_PARAM = new NorthApiResult(ErrorCode.INVALID_PARAM, "invalid parameter")
    public static final NorthApiResult APPID_PARAM_MISS = new NorthApiResult(ErrorCode.APPID_PARAM_MISS, "appid parameter is missing")
    public static final NorthApiResult SECRET_PARAM_MISS = new NorthApiResult(ErrorCode.SECRET_PARAM_MISS, "secret parameter is missing")
    public static final NorthApiResult INVALID_APPID = new NorthApiResult(ErrorCode.INVALID_APPID, "invalid appid")
    public static final NorthApiResult INVALID_SECRET = new NorthApiResult(ErrorCode.INVALID_SECRET, "invalid secret")
    public static final NorthApiResult REQUEST_JSON_ERROR = new NorthApiResult(ErrorCode.REQUEST_JSON_ERROR, "request json error")
    public static final NorthApiResult REQUEST_XML_ERROR = new NorthApiResult(ErrorCode.REQUEST_XML_ERROR, "request xml error")
    public static final NorthApiResult PARSE_JSON_ERROR = new NorthApiResult(ErrorCode.PARSE_JSON_ERROR, "parse json error")
    public static final NorthApiResult PARSE_XML_ERROR = new NorthApiResult(ErrorCode.PARSE_XML_ERROR, "parse xml error")
    public static final NorthApiResult UNAUTHORIZED_API = new NorthApiResult(ErrorCode.UNAUTHORIZED_API, "unauthorized API")
    public static final NorthApiResult UNAUTHORIZED_THE_API = new NorthApiResult(ErrorCode.UNAUTHORIZED_THE_API, "user not authorized the API")
    public static final NorthApiResult EXCEPTION = new NorthApiResult(ErrorCode.EXCEPTION, "exception")
}
