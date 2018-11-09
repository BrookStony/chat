package com.trunksoft.chat.services

import com.trunksoft.chat.member.MemberGroup
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account

public interface ChatGroupService {

    MemberGroup add(Account account, MemberGroup group) throws ApiErrorException
    MemberGroup push(Account account, MemberGroup group) throws ApiErrorException
    MemberGroup pull(Account account, Long groupId) throws ApiErrorException
    ChatApiResult remove(Account account, Long groupId) throws ApiErrorException
    List<MemberGroup> pullAll(Account account) throws ApiErrorException

}