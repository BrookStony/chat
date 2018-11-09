package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.statistics.MemberDayStat

/**
 * Created by Administrator on 2015/9/10.
 */
public interface ChatStatDataService {

    List<MemberDayStat> syncMemberSummary(Account account, Date beginDate, Date endDate) throws ApiErrorException
    List<MemberDayStat> syncMemberCumulate(Account account, Date beginDate, Date endDate) throws ApiErrorException
}