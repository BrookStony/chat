package com.seecent.chat.services

import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account

public interface ChatAuthorizeService {

    String createUrl(String relativeUrl)
    String createOAuthBaseUrl(Account account, String relativeUrl)
    String createOAuthUserinfoUrl(Account account, String relativeUrl)
    OauthResult authorize(Account account, String code) throws ApiErrorException
    Boolean check(String accessToken, String code) throws ApiErrorException
}
