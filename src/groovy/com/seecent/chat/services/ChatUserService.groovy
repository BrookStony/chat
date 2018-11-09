package com.seecent.chat.services

import com.seecent.chat.member.Member
import com.seecent.chat.member.MemberGroup
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account

public interface ChatUserService {

    Member push(Account account, Member member) throws ApiErrorException
    Member pull(Account account, String openId) throws ApiErrorException
    List<Member> pull(Account account, List<String> openIds) throws ApiErrorException
    ListOpenIdsResult listOpenIds(Account account, String nextOpenId) throws ApiErrorException
    ChatApiResult updateRemark(Account account, Member member) throws ApiErrorException
    Long searchGroup(Account account, Member member) throws ApiErrorException
    ChatApiResult changeGroup(Account account, Member member, MemberGroup group) throws ApiErrorException

}
