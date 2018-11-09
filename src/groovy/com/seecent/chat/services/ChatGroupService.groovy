package com.seecent.chat.services

import com.seecent.chat.member.MemberGroup
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account

public interface ChatGroupService {

    MemberGroup add(Account account, MemberGroup group) throws ApiErrorException
    MemberGroup push(Account account, MemberGroup group) throws ApiErrorException
    MemberGroup pull(Account account, Long groupId) throws ApiErrorException
    ChatApiResult remove(Account account, Long groupId) throws ApiErrorException
    List<MemberGroup> pullAll(Account account) throws ApiErrorException

}