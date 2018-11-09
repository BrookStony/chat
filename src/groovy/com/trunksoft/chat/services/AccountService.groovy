package com.trunksoft.chat.services

import com.trunksoft.chat.services.exception.AccountException
import com.trunksoft.chat.Account

public interface AccountService {
    String refreshAccessToken(Account account) throws AccountException
    String changeAccessToken(Account account) throws AccountException
}