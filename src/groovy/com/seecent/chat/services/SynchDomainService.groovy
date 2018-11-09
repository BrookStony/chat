package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.member.Member

public interface SynchDomainService {
    void doSynch(Account account)
    Member synchUser(Account account, String openId)
    void synchUserGroup(Account account, Member member)
}