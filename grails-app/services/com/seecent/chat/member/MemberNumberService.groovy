package com.seecent.chat.member

import com.seecent.chat.status.MemberNumberStatus
import com.seecent.chat.Account

class MemberNumberService {

    static transactional = false

    def initBeautifulNumber(Account account) {
        if(!MemberNumber.countByAccount(account)){
            new MemberNumber(no: 666, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 888, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 999, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 6666, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 8888, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 9999, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 66666, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 88888, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 99999, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 222222, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 333333, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 555555, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 999999, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 666666, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 888888, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 999999, special: true, status: MemberNumberStatus.LOCK, account: account).save()
            new MemberNumber(no: 201314, special: true, status: MemberNumberStatus.LOCK, account: account).save()
        }
    }

    Integer currentNumber(Account account) {
        def params = [max: 1, offset: 0]
        def memberNumbers = MemberNumber.createCriteria().list(params) {
            eq("account", account)
            eq("special", false)
            eq("status", MemberNumberStatus.USED)
            order("no", "desc")
        }
        if(memberNumbers && !memberNumbers.empty){
            return memberNumbers[0].no
        }
        return 0
    }

    Map<Integer, Integer> allocatedNumbers(Account account) {
        def map = [:]
        def memberNumbers = MemberNumber.createCriteria().list {
            eq("account", account)
            eq("special", true)
            or {
                eq("status", MemberNumberStatus.LOCK)
                eq("status", MemberNumberStatus.USED)
            }
        }
        memberNumbers.each {
            map.put(it.no, it.no)
        }
        return map
    }

    Integer allocation(Account account, Integer no, allocatedNoMap) {
        while(null != allocatedNoMap.get(no)) {
            no += 1
        }
        def newMemberNumber = new MemberNumber(no: no, status: MemberNumberStatus.USED, account: account)
        newMemberNumber.account = Account.proxy(account.id)
        newMemberNumber.save()
        return newMemberNumber.no
    }

}
