package com.trunksoft.chat.wechat

import com.trunksoft.chat.Account
import com.trunksoft.chat.message.MassMessage
import com.trunksoft.chat.services.ChatMassMessageService
import com.trunksoft.chat.services.MassMessageResult
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.type.MessageType
import com.trunksoft.chat.util.MassMessageUtil

/**
 * 群发消息接口
 */
class WechatMassMessageService implements ChatMassMessageService{

    static transactional = false

    def wechatAccountService

    /**
     * 群发消息
     * @param account
     * @param msg
     * @return
     * @throws ApiErrorException
     */
    @Override
    MassMessageResult send(Account account, MassMessage msg) throws ApiErrorException {
        log.info(" <send> account[${account.name}]".toString())
        if(!msg.mediaId && MessageType.TEXT != msg.type) {
            return new MassMessageResult(4301, "MediaId is null!")
        }
        def path = "/message/mass"
        if(msg.isToAll || null != msg.groupId) {
            path = path + "/sendall"
        }
        else if(msg.toUsers && msg.toUsers.size() > 0) {
            path = path + "/send"
        }
        else {
            return new MassMessageResult(4302, "No To Users!")
        }

        def data = MassMessageUtil.toJson(msg)
        println "data: " + data
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = post(path: path, query: params) {
                type "application/json"
                text data
            }
            def json = response.json
            log.info(" <send> data: " + data + ", json: " + json)
            if(json){
                def result = MassMessageResult.create(json.errcode as Integer, json.errmsg as String, json.msg_id as Long, json.msg_data_id as Long)
                return result
            }
            else {
                return new MassMessageResult(4000, "failure")
            }
        }
    }

    /**
     * 发送预览消息
     * @param account
     * @param msg
     * @return
     * @throws ApiErrorException
     */
    @Override
    MassMessageResult preview(Account account, MassMessage msg) throws ApiErrorException {
        log.info(" <preview> account[${account.name}]".toString())
        if(!msg.mediaId && MessageType.TEXT != msg.type) {
            return new MassMessageResult(4303, "MediaId is null!")
        }
        if(!msg.touser && !msg.towxname) {
            return new MassMessageResult(4304, "Touser and towxname is null!")
        }

        def data = MassMessageUtil.toPreviewJson(msg)
        println "data: " + data
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = post(path: "/message/mass/preview", query: params) {
                type "application/json"
                text data
            }
            def json = response.json
            log.info(" <preview> data: " + data + ", json: " + json)
            if(json){
                def result = MassMessageResult.create(json.errcode as Integer, json.errmsg as String, json.msg_id as Long, null)
                return result
            }
            else {
                return new MassMessageResult(4000, "failure")
            }
        }
    }

    /**
     * 删除群发消息
     * @param account
     * @param msg
     * @return
     * @throws ApiErrorException
     */
    @Override
    MassMessageResult delete(Account account, MassMessage msg) throws ApiErrorException {
        log.info(" <delete> account[${account.name}] msgId: ${msg.msgId}".toString())
        if(!msg.msgId) {
            return new MassMessageResult(4305, "MsgId is null!")
        }
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = post(path: "/message/mass/delete", query: params) {
                type "application/json"
                text "{msg_id: ${msg.msgId}"
            }
            def json = response.json
            log.info(" <send> msgId: " + msg.msgId + ", json: " + json)
            if(json){
                def result = new MassMessageResult(json.errcode as Integer, json.errmsg as String)
                return result
            }
            else {
                return new MassMessageResult(4000, "failure")
            }
        }
    }

    /**
     * 查询消息状态
     * @param account
     * @param msg
     * @return
     * @throws ApiErrorException
     */
    @Override
    MassMessageResult getStatus(Account account, MassMessage msg) throws ApiErrorException {
        log.info(" <getStatus> account[${account.name}] msgId: ${msg.msgId}".toString())
        if(!msg.msgId) {
            return new MassMessageResult(4305, "MsgId is null!")
        }
        wechatAccountService.refreshAccessToken(account)
        withRest(url: account.apiUrl) {
            def params = [access_token: account.refreshToken]
            def response = post(path: "/message/mass/get", query: params) {
                type "application/json"
                text "{msg_id: ${msg.msgId}"
            }
            def json = response.json
            log.info(" <getStatus> msgId: " + msg.msgId + ", json: " + json)
            if(json){
                if(json.msg_status && "SEND_SUCCESS" == json.msg_status) {
                    return new MassMessageResult(0, "SEND_SUCCESS")
                }
                else {
                    return new MassMessageResult(4000, "failure")
                }
            }
            else {
                return new MassMessageResult(4000, "failure")
            }
        }
    }
}
