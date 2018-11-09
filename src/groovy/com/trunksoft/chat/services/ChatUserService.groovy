package com.trunksoft.chat.services

import com.trunksoft.chat.member.Member
import com.trunksoft.chat.member.MemberGroup
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account

public interface ChatUserService {

    Member push(Account account, Member member) throws ApiErrorException
    Member pull(Account account, String openId) throws ApiErrorException
    List<Member> pull(Account account, List<String> openIds) throws ApiErrorException
    ListOpenIdsResult listOpenIds(Account account, String nextOpenId) throws ApiErrorException
    ChatApiResult updateRemark(Account account, Member member) throws ApiErrorException
    Long searchGroup(Account account, Member member) throws ApiErrorException
    ChatApiResult changeGroup(Account account, Member member, MemberGroup group) throws ApiErrorException

}
