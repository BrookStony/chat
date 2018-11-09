package com.trunksoft.chat.services

import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account

public interface ChatAuthorizeService {

    String createUrl(String relativeUrl)
    String createOAuthBaseUrl(Account account, String relativeUrl)
    String createOAuthUserinfoUrl(Account account, String relativeUrl)
    OauthResult authorize(Account account, String code) throws ApiErrorException
    Boolean check(String accessToken, String code) throws ApiErrorException
}
