package com.trunksoft.chat.services

import com.trunksoft.chat.Account
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.statistics.MemberDayStat

/**
 * Created by Administrator on 2015/9/10.
 */
public interface ChatStatDataService {

    List<MemberDayStat> syncMemberSummary(Account account, Date beginDate, Date endDate) throws ApiErrorException
    List<MemberDayStat> syncMemberCumulate(Account account, Date beginDate, Date endDate) throws ApiErrorException
}