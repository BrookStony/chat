package com.seecent.chat.message

import com.seecent.chat.Account
import com.seecent.chat.assets.Article
import com.seecent.chat.assets.Picture
import com.seecent.chat.member.Member
import com.seecent.chat.member.MemberGroup
import com.seecent.chat.services.MassMessageResult
import com.seecent.chat.status.MassMessageStatus
import com.seecent.chat.type.AccountType
import com.seecent.chat.type.MessageType
import com.seecent.chat.type.SexType
import com.seecent.platform.common.Location
import com.seecent.platform.type.LocationType
import com.seecent.util.DateUtil
import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class ChatMassMessageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def articleService
    def materialManageService
    def wechatMassMessageService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = MassMessage.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def massMessageCount = result.totalCount
        def massMessageList = result.collect {
            return toMassMessageMap(it)
        }
        response.setHeader('totalItems', massMessageCount as String)
        respond massMessageList
    }

    def search(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def accountId = params.long("accountId")
        def result = MassMessage.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
        }
        def massMessageCount = result.totalCount
        def massMessageList = result.collect {
            return toMassMessageMap(it)
        }
        response.setHeader('totalItems', massMessageCount as String)
        respond massMessageList
    }

    private def toMassMessageMap(MassMessage it) {
        def map = [id: it.id, type: it.type.name(),
                   isToAll: it.isToAll,
                   content: it.content,
                   materialId: it.materialId, material: [:],
                   articleId: it.articleId, article: [:],
                   totalCount: it.totalCount, filterCount: it.filterCount,
                   sentCount: it.sentCount, errorCount: it.errorCount,
                   status: it.status.name(),
                   msgTime: new Date(it.msgTime).format("yyyy年MM月dd日")]
        if(null != it.groupId) {
            def memberGroup = MemberGroup.findByAccountAndGroupId(it.account, it.groupId)
            if(memberGroup) {
                map.group = memberGroup.name
            }
        }
        if(null != it.locationId) {
            def location = Location.get(it.locationId)
            if(location) {
                if(LocationType.CITY == location.type) {
                    map.location = location.parent?.name + "-" + location.name
                }
                else {
                    map.location = location.name
                }
            }
        }
        if(null != it.sexType) {
            if(1 == it.sexType) {
                map.sexType = "男"
            }
            else if(2 == it.sexType) {
                map.sexType = "女"
            }
        }
        if(MessageType.IMAGE == it.type) {
            def pic = Picture.get(it.materialId)
            if(pic) {
                map.material = [name: pic.name, url: materialManageService.serverUrl(pic.url)]
            }
        }
        else if(MessageType.NEWS == it.type) {
            def article = Article.get(it.articleId)
            if(article) {
                map.article = articleService.toArticle(article, false)
            }
        }
        return map
    }

    def searchSentCount() {
        def accountId = params.long("accountId")
        def accountInstance = Account.get(accountId)
        if(AccountType.SERVICE == accountInstance.type) {
            def startDate = DateUtil.firstDayOfMonth()
            def massMessageCount = MassMessage.createCriteria().count(){
                eq("account", accountInstance)
                or {
                    eq("status", MassMessageStatus.SENDING)
                    eq("status", MassMessageStatus.SENDED)
                    eq("status", MassMessageStatus.SEND_SUCCESS)
                }
                ge("msgTime", startDate.time)
            }
            def remainCount = 0
            if(massMessageCount < 4) {
                remainCount = 4 - massMessageCount
            }
            render ([count: massMessageCount, remainCount: remainCount, accountType: accountInstance.type.name()] as JSON)
        }
        else {
            def (endDate, startDate) = DateUtil.todayBetween()
            def massMessageCount = MassMessage.createCriteria().count(){
                eq("account", accountInstance)
                or {
                    eq("status", MassMessageStatus.SENDING)
                    eq("status", MassMessageStatus.SENDED)
                    eq("status", MassMessageStatus.SEND_SUCCESS)
                }
                between("msgTime", startDate.time, endDate.time)
            }
            def remainCount = 0
            if(massMessageCount < 1) {
                remainCount = 1
            }
            render ([count: massMessageCount, remainCount: remainCount, accountType: accountInstance.type.name()] as JSON)
        }
    }

    @Transactional
    def massSend(){
        def accountId = params.long("accountId")
        def accountInstance = Account.get(accountId)
        if(null == accountInstance) {
            notFound()
            return
        }

        println "params: " + params
        def sendTarget = params.int("sendTarget")
        def sendSexType = params.int("sendSexType")
        def sendGroup = params.long("sendGroup")
        def sendLocation = params.long("sendLocation")
        def locationType = params.locationType as String
        def locationName = params.locationName as String
        def messageType = params.messageType as String
        def content = params.content
        def materialId = params.long("materialId")
        def articleId = params.long("articleId")

        MassMessageResult massMessageResult = new MassMessageResult(4, "发送失败！")
        if(messageType) {
            def type = MessageType.valueOf(messageType)
            def isToAll = true
            def groupId = null
            def toUsers = new HashSet<Long>()
            if(null != sendGroup) {
                groupId = sendGroup
                isToAll = false
            }
            else if(null != sendLocation) {
                isToAll = false
            }
            else if(0 != sendTarget) {
                isToAll = false
            }

            if(!isToAll && null == groupId) {
                def result = Member.createCriteria().list {
                    eq("account", accountInstance)
                    if(null != sendSexType && 0 != sendSexType){
                        eq("sex", SexType.codeOf(sendSexType))
                    }
                    if(locationType && locationName) {
                        if("city" == locationType) {
                            eq("city", locationName)
                        }
                        else if("province" == locationType) {
                            eq("province", locationName)
                        }
                        else if("country" == locationType) {
                            eq("country", locationName)
                        }
                    }
                }
                println "result: " + result
                result.each {
                    toUsers << it.id
                }
            }
            println "toUsers: " + toUsers
            if(isToAll || null != groupId || toUsers.size() > 0) {
                def massMessage = new MassMessage(account: accountInstance)
                massMessage.isToAll = isToAll
                massMessage.groupId = groupId
                massMessage.locationId = sendLocation
                massMessage.sexType = sendSexType
                massMessage.toUsers = toUsers
                massMessage.msgTime = System.currentTimeMillis()
                bindMassMessage(massMessage, type, content as String, materialId, articleId)
                massMessageResult = wechatMassMessageService.send(accountInstance, massMessage)
                massMessage.bindResult(massMessageResult)
                massMessage.save(failOnError: true)
            }
        }
        render ([code: massMessageResult.code, message: massMessageResult.message] as JSON)
    }

    private void bindMassMessage(MassMessage massMessage, MessageType type, String content, Long materialId, Long articleId) {
        massMessage.type = type
        if(MessageType.TEXT == type) {
            massMessage.content = content
        }
        else if(MessageType.NEWS == type) {
            massMessage.articleId = articleId
            if(null != articleId) {
                def article = Article.get(articleId)
                if(article) {
                    massMessage.mediaId = article.mediaId
                }
            }
        }
        else if(MessageType.IMAGE == type) {
            massMessage.materialId = materialId
            if(null != materialId) {
                def picture = Picture.get(materialId)
                if(picture) {
                    massMessage.mediaId = picture.mediaId
                }
            }
        }
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
