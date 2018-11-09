package com.trunksoft.chat.northapi

import java.util.concurrent.ConcurrentHashMap

class MessageFilterService {

    private static final long LIMIT_TIME = 3600000
    private static ConcurrentHashMap<String, Long> messageCache = new ConcurrentHashMap<String, Long>(100)

    /**
     * 避免重复发送
     * @param memberId
     * @param content
     * @return
     */
    boolean check(Long memberId, String content) {
        def key = "mb@" + memberId + "@" + content
        def time = messageCache.get(key)
        if(null == time) {
            messageCache.put(key, System.currentTimeMillis())
            return true
        }
        else {
            if((System.currentTimeMillis() - time) < LIMIT_TIME) {
                return false
            }
            else {
                messageCache.put(key, System.currentTimeMillis())
                return true
            }
        }
    }

    void refresh() {
        try {
            def keys = []
            def currentTimeMillis = System.currentTimeMillis()
            messageCache.each {
                if((currentTimeMillis - it.value) > LIMIT_TIME){
                    keys << it.key
                }
            }

            keys.each {
                messageCache.remove(it)
            }

            keys.clear()
        }
        catch (Exception e) {
            log.error("<refresh> error: " + e.message, e)
        }
    }
}
