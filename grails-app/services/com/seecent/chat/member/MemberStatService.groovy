package com.seecent.chat.member

import com.seecent.chat.Account
import com.seecent.chat.statistics.MemberDayStat
import com.seecent.util.DateUtil
import grails.transaction.Transactional
import org.joda.time.DateTime

@Transactional
class MemberStatService {

    def wechatStatDataService

    def executeDailyStat(Account account) {
        try {
            log.info("<executeDailyStat> account[${account.name}]")
            def (endDate, beginDate) = DateUtil.beforeFivedaysBetween()
            println "beginDate-endDate: ${beginDate}-${endDate}"
            def memberDayStatList = wechatStatDataService.syncMemberSummary(account, beginDate, endDate)
            for(MemberDayStat memberDayStat : memberDayStatList) {
                memberDayStat.save()
            }
        }
        catch (Exception e) {
            log.info("<executeDailyStat> error: " + e.message, e)
        }
    }

    def executeDayStat(Account account) {
        println "executeDayStat: " + account.id

        def beginDate = new DateTime().withTime(23, 59, 59, 0).toDate()
        def endDate = new DateTime().withTime(23, 59, 59, 0).toDate()

        def newAmount = Member.where {
            eq("account", account)
            eq("subscribe", true)
            between("subscribeTime", beginDate, endDate)
        }.count()

        def cancelAmount = Member.where {
            eq("account", account)
            eq("subscribe", false)
            between("subscribeTime", beginDate, endDate)
        }.count()

        def netAmount = 0
        if(newAmount > cancelAmount) {
            netAmount = newAmount - cancelAmount
        }

        def cumulateAmount = Member.countByAccount(account)

        def memberDayStat = new MemberDayStat(accountId: account.id)
        memberDayStat.timestamp = new DateTime().millis
        memberDayStat.newAmount = newAmount
        memberDayStat.cancelAmount = cancelAmount
        memberDayStat.netAmount = netAmount
        memberDayStat.cumulateAmount = cumulateAmount
        memberDayStat.save(failOnError: true)
    }
}
