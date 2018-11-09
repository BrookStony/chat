package com.trunksoft.chat.northapi

import com.trunksoft.chat.services.MessageResult

class ChatRestClientService {

    static transactional =  false

    MessageResult bindAccount(String url, String path, String data, params) {
        log.info("<bindAccount> url: ${url}, params: ${params}".toString())
        withRest(url: url) {
            def response = post(path: path, query: params) {
                type "application/json"
                text data
            }
            def json = response.json
            log.info("<bindAccount> data: ${data}, json: " + json)
            if(json){
                def result = MessageResult.create(-1, json.errcode, json.errmsg)
                return result
            }
            else {
                return new MessageResult(null, -1, "failure")
            }
        }
    }
}
