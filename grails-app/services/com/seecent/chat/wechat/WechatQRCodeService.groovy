package com.seecent.chat.wechat

import com.seecent.chat.Account
import com.seecent.chat.account.QRCode
import com.seecent.chat.services.ChatApiResult
import com.seecent.chat.services.ChatQRCodeService
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.type.QRCodeType
import grails.converters.JSON

class WechatQRCodeService implements ChatQRCodeService {

    static transactional = false

    def wechatAccountService

    @Override
    QRCode create(Account account, QRCode qrCode) throws ApiErrorException {
        log.info("<create> account[${account.name}]".toString())
        withRest(url: account.apiUrl) {
            def data
            if(QRCodeType.QR_LIMIT_SCENE == qrCode.type) {
                data = [action_name: "QR_LIMIT_SCENE", action_info: [scene: [scene_id: qrCode.sceneId]]]
            }
            else if(QRCodeType.QR_LIMIT_STR_SCENE == qrCode.type) {
                data = [action_name: "QR_LIMIT_STR_SCENE", action_info: [scene: [scene_str: qrCode.sceneStr]]]
            }
            else {
                if(!qrCode.expireSeconds) {
                    qrCode.expireSeconds = 604800l
                }
                data = [expire_seconds: qrCode.expireSeconds, action_name: "QR_SCENE", action_info: [scene: [scene_id: qrCode.sceneId]]]
            }
            def qrCodeJson = (data as JSON).toString()
            def response = post(path: '/qrcode/create', query: [access_token: account.refreshToken]) {
                type "application/json"
                text  qrCodeJson
            }
            def json = response.json
//            log.info("<create> qrCode: ${qrCode}, response.json: ".toString() + response.json)
            if(json){
                ChatApiResult apiResult = ChatApiResult.create(json.errcode, json.errmsg)
                if(apiResult.isOk()){
//                    log.info("<create> qrCode: ${qrCode}, success! response.json: ".toString() + response.json)
                    qrCode.ticket = json.ticket as String
                    qrCode.expireSeconds = json.expire_seconds as Long
                    qrCode.url = json.url as String
                }
                else {
                    log.error("<create> qrCode: ${qrCode}, error! response.json: ".toString() + response.json)
                    throw new ApiErrorException(apiResult.message, apiResult.code)
                }
            }
        }
        return qrCode
    }
}
