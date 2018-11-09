package com.seecent.platform.common

class ErrorCode {
    public static final int SYSTEM_BUSY = -1
    public static final int SUCCESS = 0
    public static final int AUTHENTICATION_FAILED = 4001
    public static final int INVALID_TOKEN = 4002
    public static final int TOKEN_EXPIRED = 4003
    public static final int INVALID_PARAM = 4004
    public static final int INVALID_ACCESS_TOKEN = 4005
    public static final int INVALID_REFRESH_TOKEN = 4006
    public static final int INVALID_APPID = 4007
    public static final int INVALID_SECRET = 4008
    public static final int PARAM_MISS = 4009
    public static final int TOKEN_PARAM_MISS = 4010
    public static final int ACCESS_TOKEN_PARAM_MISS = 4011
    public static final int REFRESH_TOKEN_PARAM_MISS = 4012
    public static final int APPID_PARAM_MISS = 4013
    public static final int SECRET_PARAM_MISS = 4014
    public static final int PARAM_ERROR = 4015
    public static final int INVALID_VALUE = 4016
    public static final int PARAM_VALUE_NULL = 4017
    public static final int NOT_FOUND = 4018
    public static final int MEDIAID_NULL = 4019

    public static final int REQUEST_JSON_ERROR = 4031
    public static final int REQUEST_XML_ERROR = 4032
    public static final int PARSE_JSON_ERROR = 4033
    public static final int PARSE_XML_ERROR = 4034
    public static final int UNAUTHORIZED_API = 4035
    public static final int UNAUTHORIZED_THE_API = 4036

    public static final int EXCEPTION = 4040
    public static final int TIMEOUT = 4041

    public static final int CONF_ASSETS_BASEPATH_NULL = 4051
    public static final int CONF_CURL_UPLOADURL_NULL = 4052
    public static final int CONF_CURL_DOWNLOADURL_NULL = 4053
    public static final int CONF_CURL_WAITFOR_NULL = 4054
}
