package com.trunksoft.chat.northapi

import com.trunksoft.chat.message.ChatMessage
import com.trunksoft.platform.common.ErrorCode
import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class NorthApiDebugController {

    def chatNorthApiDebugService

    def index() {}

    def debug() {
        def result = null
//        try{
            def method = params.method
            def url = params.url
            log.info(" debug method: " + method + ", url: " + url + ", params: " + params.json)
            if(method && url){
                if("POST" == method){
                    def data = params.data
                    println "data: " + data
                    result = chatNorthApiDebugService.post(url.toString(), data?.toString(), params)
                }
                else {
                    result = chatNorthApiDebugService.get(url.toString(), params)
                }
            }
//        }
//        catch (Exception e) {
//            e.printStackTrace()
//        }
        if(!result){
            result = [errcode: ErrorCode.PARAM_ERROR, errmsg: "error"] as JSON
        }
        render result.toString()
    }
}
