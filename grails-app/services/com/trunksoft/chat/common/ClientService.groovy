package com.trunksoft.chat.common

import javax.servlet.http.HttpServletRequest

class ClientService {

    static boolean transactional = false

    String clientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for")
        if(ip && !"unknown".equalsIgnoreCase(ip)) {
            return ip
        }
//        ip = request.getHeader("Proxy-Client-IP")
//        if(ip && !"unknown".equalsIgnoreCase(ip)) {
//            return ip
//        }
//        ip = request.getHeader("WL-Proxy-Client-IP")
//        if(ip && !"unknown".equalsIgnoreCase(ip)) {
//            return ip
//        }
        return request.getRemoteAddr()
    }
}
