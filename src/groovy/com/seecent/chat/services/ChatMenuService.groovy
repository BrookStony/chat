package com.seecent.chat.services

import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account
import com.seecent.chat.weixin.WeChatMenu

public interface ChatMenuService {

    ChatApiResult create(Account account, List<WeChatMenu> menus) throws ApiErrorException
    ChatApiResult remove(Account account) throws ApiErrorException
    List<WeChatMenu> pull(Account account) throws ApiErrorException

}