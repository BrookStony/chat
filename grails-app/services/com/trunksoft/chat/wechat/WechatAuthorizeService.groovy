package com.trunksoft.chat.wechat

import com.trunksoft.chat.Account
import com.trunksoft.chat.services.ChatAuthorizeService
import com.trunksoft.chat.services.OauthResult
import com.trunksoft.chat.services.exception.ApiErrorException
import grails.converters.JSON
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class WechatAuthorizeService implements ChatAuthorizeService {

    static transactional = false

    def grailsApplication

    @Override
    String createUrl(String relativeUrl) {
        def config = grailsApplication.config.chat
        String url = config.home_url as String
        if(url) {
            if(relativeUrl.startsWith("/")) {
                url = url + relativeUrl
            }
            else {
                url = url + "/" + relativeUrl
            }
            return URLEncoder.encode(url, "utf-8")
        }
        return relativeUrl
    }

    /**
     * 对于以snsapi_base为scope的网页授权，就静默授权的，用户无感知
     * @param account
     * @param relativeUrl
     * @return
     */
    @Override
    String createOAuthBaseUrl(Account account, String relativeUrl) {
        def config = grailsApplication.config.chat
        def homeUrl = config.home_url as String
        def oauthUrl = config.oauth_url as String
        if(homeUrl && oauthUrl) {
            def redirect_uri = homeUrl
            if(relativeUrl.startsWith("/")) {
                redirect_uri = redirect_uri + relativeUrl
            }
            else {
                redirect_uri = redirect_uri + "/" + relativeUrl
            }
            def state = 123
            String url = oauthUrl + "?appid=${account.appId}&redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}&response_type=code&scope=snsapi_base&state=${state}#wechat_redirect".toString()
            return url
        }
        return relativeUrl
    }

    /**
     * 对于已关注公众号的用户，如果用户从公众号的会话或者自定义菜单进入本公众号的网页授权页，即使是scope为snsapi_userinfo，也是静默授权，用户无感知。
     * @param account
     * @param relativeUrl
     * @return
     */
    @Override
    String createOAuthUserinfoUrl(Account account, String relativeUrl) {
        def config = grailsApplication.config.chat
        def homeUrl = config.home_url as String
        def oauthUrl = config.oauth_url as String
        if(homeUrl && oauthUrl) {
            def redirect_uri = homeUrl
            if(relativeUrl.startsWith("/")) {
                redirect_uri = redirect_uri + relativeUrl
            }
            else {
                redirect_uri = redirect_uri + "/" + relativeUrl
            }
            def state = 123
            String url = oauthUrl + "?appid=${account.appId}&redirect_uri=${URLEncoder.encode(redirect_uri, "utf-8")}&response_type=code&scope=snsapi_userinfo&state=${state}#wechat_redirect".toString()
            return url
        }
        return relativeUrl
    }

    /**
     * 通过code换取网页授权access_token
     * @param account
     * @param code
     * @return
     * @throws ApiErrorException
     */
    @Override
    OauthResult authorize(Account account, String code) throws ApiErrorException {
        log.info(" <authorize> account[${account.name}] code: ${code}".toString())
        def oauthResult
        def config = grailsApplication.config.chat
        def url = config.oauth_access_token_url as String
        if(url) {
            try {
                withRest(url: url) {
                    def params = [appid: account.appId, secret: account.appSecret, code: code, grant_type: "authorization_code"]
                    def response = get(query: params)
                    log.info(" <authorize> code: " + code + ", response.text: " + response.text)
                    if(response.text) {
                        def json = new JsonSlurper().parseText(response.text)
                        oauthResult = createOauthResult(json)
                        return oauthResult
                    }
                    else {
                        log.info(" <authorize> code: " + code + ", response.json: " + response.json)
                        def json = response.json
                        if(json){
                            oauthResult = createOauthResult(json)
                            return oauthResult
                        }
                        else {
                            oauthResult = new OauthResult(errcode: 5, errmsg: "Response null!")
                            return oauthResult
                        }
                    }
                }
            }
            catch (Exception e) {
                log.error(" <authorize> code: " + code + ", error: " + e.message, e)
                oauthResult = new OauthResult(errcode: 3, errmsg: "Request failure!")
            }
        }
        else {
            oauthResult = new OauthResult(errcode: 4, errmsg: "Config oauth_access_token_url is null!")
        }
        return oauthResult
    }

    private OauthResult createOauthResult(json) {
        if(json.access_token) {
            OauthResult oauthResult = new OauthResult(errcode: 0, errmsg: "Ok!")
            oauthResult.accessToken = json.access_token
            oauthResult.expiresIn = json.expires_in as Integer
            oauthResult.refreshToken = json.refresh_token
            oauthResult.openId = json.openid
            oauthResult.scope = json.scope
            oauthResult.unionId = json.unionid
            return oauthResult
        }
        else {
            return new OauthResult(errcode: json.errcode, errmsg: json.errmsg)
        }
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * @param accessToken
     * @param openId
     * @return
     * @throws ApiErrorException
     */
    @Override
    Boolean check(String accessToken, String openId) throws ApiErrorException {
        log.info(" <check> accessToken: ${accessToken}, openId: ${openId}".toString())
        withRest(url: "https://api.weixin.qq.com/sns/auth") {
            def params = [access_token: accessToken, openid: openId]
            def response = post(query: params)
            log.info(" <check> accessToken: " + accessToken + ", openId: " + openId + ", response.json: " + response.json)
            def json = response.json
            if(json){
                if(null != json.errcode && 0 == json.errcode) {
                    return true
                }
            }
            return false
        }
    }
}
