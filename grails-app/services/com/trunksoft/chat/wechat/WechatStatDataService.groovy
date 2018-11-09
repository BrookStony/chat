package com.trunksoft.chat.wechat

import com.trunksoft.chat.Account
import com.trunksoft.chat.services.ChatStatDataService
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.statistics.MemberDayStat

import java.text.DateFormat
import java.text.SimpleDateFormat

class WechatStatDataService implements ChatStatDataService {

    static transactional = false

    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    def wechatAccountService

    @Override
    List<MemberDayStat> syncMemberSummary(Account account, Date beginDate, Date endDate) throws ApiErrorException {
        log.info("<syncMemberSummary> account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        List<MemberDayStat> memberDayStats = new ArrayList<MemberDayStat>()
        def begin_date = beginDate.format("yyyy-MM-dd")
        def end_date = endDate.format("yyyy-MM-dd")
        println "{\"begin_date\": \"${begin_date}\", \"end_date\": \"${end_date}\"}".toString()
        withRest(url: "https://api.weixin.qq.com/datacube") {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/getusersummary', query: params) {
                type "application/json"
                text "{\"begin_date\": \"${begin_date}\", \"end_date\": \"${end_date}\"}".toString()
            }
            def json = response.json
            log.info("<syncMemberSummary> response.json: " + json)
            println "json: " + json
            if(json && json.list){
                json.list.each{
                    def type = it.user_source as Integer
                    def newUser = it.new_user as Integer
                    def cancelUser = it.cancel_user as Integer
                    def refDate = it.ref_date as String
                    def date = DF.parse(refDate + " 00:00:00")
                    def memberDayStat = memberDayStats.find { it.timestamp == date.time }
                    if(!memberDayStat) {
                        memberDayStat = new MemberDayStat(accountId: account.id, timestamp: date.time)
                        memberDayStat.newAmount = newUser
                        memberDayStat.cancelAmount = cancelUser
                        memberDayStat.netAmount = newUser - cancelUser

                        def calendar = Calendar.getInstance()
                        calendar.setTime(date)
                        memberDayStat.year = calendar.get(Calendar.YEAR)
                        memberDayStat.month = calendar.get(Calendar.MONTH) + 1
                        memberDayStat.day = calendar.get(Calendar.DAY_OF_MONTH)
                        memberDayStat.cumulateAmount = 0
                    }
                    else {

                    }
                    memberDayStats.add(memberDayStat)
                }
            }
        }
        return memberDayStats
    }

    @Override
    List<MemberDayStat> syncMemberCumulate(Account account, Date beginDate, Date endDate) throws ApiErrorException {
        log.info("<syncMemberCumulate> account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        List<MemberDayStat> memberDayStats = new ArrayList<MemberDayStat>()
        def begin_date = beginDate.format("yyyy-MM-dd")
        def end_date = endDate.format("yyyy-MM-dd")
        withRest(url: "https://api.weixin.qq.com/datacube") {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/getusercumulate', query: params) {
                type "application/json"
                text "{\"begin_date\": \"${begin_date}\", \"end_date\": \"${end_date}\"}".toString()
            }
            def json = response.json
            println "json: " + json
            if(json){
                log.info("<syncMemberCumulate> response.json: " + json)
            }
        }
        return memberDayStats
    }
}
