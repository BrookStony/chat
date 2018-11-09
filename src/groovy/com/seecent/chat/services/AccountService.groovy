package com.seecent.chat.services

import com.seecent.chat.services.exception.AccountException
import com.seecent.chat.Account

public interface AccountService {
    String refreshAccessToken(Account account) throws AccountException
    String changeAccessToken(Account account) throws AccountException
}