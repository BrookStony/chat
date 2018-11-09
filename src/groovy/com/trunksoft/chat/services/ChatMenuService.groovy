package com.trunksoft.chat.services

import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account
import com.trunksoft.chat.weixin.WeChatMenu

public interface ChatMenuService {

    ChatApiResult create(Account account, List<WeChatMenu> menus) throws ApiErrorException
    ChatApiResult remove(Account account) throws ApiErrorException
    List<WeChatMenu> pull(Account account) throws ApiErrorException

}