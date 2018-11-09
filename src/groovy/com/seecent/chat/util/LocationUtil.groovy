package com.seecent.chat.util

class LocationUtil {
    static boolean isCentralCity(String city) {
        if("上海".equals(city)){
            return true
        }
        if("北京".equals(city)){
            return true
        }
        if("重庆".equals(city)){
            return true
        }
        if("天津".equals(city)){
            return true
        }
        return false
    }
}
