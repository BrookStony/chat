package com.trunksoft.chat

import com.trunksoft.chat.account.QRCode
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.type.QRCodeType
import com.trunksoft.util.DateUtil

class TestController {

    def wechatMessageService
    def wechatMenuService
    def wechatGroupService
    def wechatUserService
    def wechatSynchDomainService
    def locationInitService
    def wechatStatDataService
    def wechatQRCodeService
    def memberStatService

    def index() {
        try {
//            menuInitService.initFromXml("menu.xml")
//            locationInitService.init()
//            println "sssssssss"
            def account = Account.get(1l)
            if(account) {
                def (endDate, beginDate) = DateUtil.yesterdayBetween()
                def qrCode1 = new QRCode(account: account, type: QRCodeType.QR_SCENE, expireSeconds: 604800l, sceneId: 10001)
                def qrCode2 = new QRCode(account: account, type: QRCodeType.QR_LIMIT_SCENE, sceneId: 10002)
                def qrCode3 = new QRCode(account: account, type: QRCodeType.QR_LIMIT_STR_SCENE, sceneStr: "123")

                qrCode1 = wechatQRCodeService.create(account, qrCode1)
                qrCode1.save(flush: true)

                qrCode2 = wechatQRCodeService.create(account, qrCode2)
                qrCode2.save(flush: true)

                qrCode3 = wechatQRCodeService.create(account, qrCode3)
                qrCode3.save(flush: true)

//                memberStatService.executeDailyStat(account)
               // wechatStatDataService.syncMemberSummary(account, beginDate, endDate)
               // wechatStatDataService.syncMemberCumulate(account, beginDate, endDate)
            }

//            wechatAccountService.refreshAccessToken(account)
//            println "token: " + account.refreshToken
//            def member = Member.findByOpenId("o_yizjh_NxiH6SS8A9uPJ0rvIB_A")
//            wechatSynchDomainService.doSynch(account)
//            wechatMenuService.remove(account)
//            testMenu(account)
//            def msg = new ImageMessage(account: account, toUser: member, mediaId: "vFGKzpqcn3AXIbXDoxnPFvfxvYOf4Q13kKi7rvxJfjMXeyP86cbksUWPKC8ct-8W")
//            wechatMessageService.send(account, msg)
//            msg.save()
//            msg.errors.each {
//                println it
//            }
            println "md5: " + "Hello".encodeAsMD5()
            println "sha1: " + "Hello".encodeAsSHA1()
        }
        catch (ApiErrorException e) {
            println "ApiErrorException code: " + e.code + ", message: " + e.message
        }
    }

}
