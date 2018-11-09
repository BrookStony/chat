package com.seecent.chat.member

import com.seecent.chat.Account

class OpenIdCacheService {

    static transactional = false

    private static Hashtable<Long, Hashtable<Long, String>> openIdCache = new Hashtable<Long, Hashtable<Long, String>>()

    void init() {
        try {
            openIdCache.clear()
            def accounts = Account.list()
            accounts.each { account ->
                def cache = new Hashtable<Long, String>()
                def members = Member.findAllByAccount(account)
                members.each { member ->
                    if(member && member.openId){
                        cache.put(member.id, member.openId)
                    }
                }
                openIdCache.put(account.id, cache)
            }
        } catch (Exception e) {
            log.error(" init cache error:", e)
        }
    }

    void put(Long accountId, Long memberId) {
        try {
            def cache = openIdCache.get(accountId)
            if(!cache){
                cache = new Hashtable<Long, String>()
                openIdCache.put(accountId, cache)
            }
            cache.put(accountId, memberId)
        } catch (Exception e) {
            log.error(" put error:", e)
        }
    }

    String get(Long accountId, Long memberId) {
        try {
            def cache = openIdCache.get(accountId)
            if(cache){
                return cache.get(memberId)
            }
        } catch (Exception e) {
            log.error(" get error:", e)
        }
        return null
    }
}
