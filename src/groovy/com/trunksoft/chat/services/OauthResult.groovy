package com.trunksoft.chat.services

class OauthResult {

    Integer errcode
    String errmsg
    String accessToken
    Integer expiresIn
    String refreshToken
    String openId
    String scope
    String unionId

    public boolean isOk() {
        return 0 == errcode
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("{errcode: " + errcode)
        sb.append(",errmsg: " + errmsg)
        sb.append(",accessToken: " + accessToken)
        sb.append(",expiresIn: " + expiresIn)
        sb.append(",refreshToken: " + refreshToken)
        sb.append(",openId: " + openId)
        sb.append(",scope: " + scope)
        sb.append(",unionId: " + unionId)
        sb.append("}")
    }
}
