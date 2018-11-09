package com.trunksoft.chat.services

import com.trunksoft.chat.Account
import com.trunksoft.chat.member.Member

public interface SynchDomainService {
    void doSynch(Account account)
    Member synchUser(Account account, String openId)
    void synchUserGroup(Account account, Member member)
}