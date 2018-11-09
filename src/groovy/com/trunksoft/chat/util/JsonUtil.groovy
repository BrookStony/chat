package com.trunksoft.chat.util

import org.codehaus.groovy.grails.web.json.JSONArray

class JsonUtil {

    static def toLongList(JSONArray jsonArray) {
        def list = []
        for(int i=0; i< jsonArray.size(); i++){
            def v = jsonArray.getLong(i)
            if(v){
                list << v
            }
        }
        return list
    }

    static def toList(JSONArray jsonArray) {
        def list = []
        for(int i=0; i< jsonArray.size(); i++){
            def v = jsonArray.getString(i)
            if(v && !v.trim().equals("") && !v.trim().toLowerCase().equals("null")){
                list << v
            }
        }
        return list
    }
}
