package com.seecent.chat.northapi

import com.seecent.chat.services.NorthApiDebugService
import com.seecent.chat.services.exception.ApiErrorException

class ChatNorthApiDebugService implements NorthApiDebugService {

    static transactional = false

    def post(String url, String body, params) throws ApiErrorException {
        withRest(url: url) {
            def contentType = "application/json"
            if(body && -1 != body.indexOf("<xml")){
                contentType = "application/xml"
            }
            def response = post(query: params) {
                type contentType
                text body
            }
            return response.json
        }
    }

    def get(String url, params) throws ApiErrorException {
        withRest(url: url) {
            def response = get(query: params)
            return response.json
        }
    }
}
