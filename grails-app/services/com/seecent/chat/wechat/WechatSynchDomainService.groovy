package com.seecent.chat.wechat

import com.seecent.chat.util.EmojiFilterUtil
import com.seecent.platform.common.Location
import com.seecent.chat.member.Member
import com.seecent.chat.member.MemberGrade
import com.seecent.chat.member.MemberGroup
import com.seecent.chat.services.ListOpenIdsResult
import com.seecent.chat.services.SynchDomainService
import com.seecent.chat.Account
import com.seecent.chat.util.LocationUtil

class WechatSynchDomainService implements SynchDomainService {

    static transactional = false

    def wechatAccountService
    def wechatGroupService
    def wechatUserService
    def memberNumberService

    void doSynch(Account account) {
        try {
            synchGroups(account)
            synchUsers(account)
            synchUsersGroup(account)
        }
        catch (Exception e) {
            log.error(" doSynch account[${account.name}] error!", e)
        }
    }

    void synchGroups(Account account) {
        try {
            log.info(" synchGroups account[${account.name}]".toString())
            wechatAccountService.refreshAccessToken(account)
            def groupIds = []
            def groupMap = [:]
            def memberGroups = MemberGroup.findAllByAccount(account)
            memberGroups.each {
                groupIds << it.id
                groupMap.put(it.id, it)
            }
            def groups = wechatGroupService.pullAll(account)
            groups.each { group ->
                def memberGroup = groupMap.get(group.id)
                if(memberGroup){
                    groupIds.remove(group.id)
                }
                else {
                    memberGroup = new MemberGroup()
                }
                memberGroup.groupId = group.groupId
                memberGroup.name = group.name
                memberGroup.account = Account.proxy(account.id)
                memberGroup.save(failOnError: true)
            }

            //删除用户组
            if(groupIds.size() > 0) {
                groupIds.each {
                    def memberGroup = groupMap.get(it)
                    if(memberGroup){
                        def members = Member.findAllByAccountAndGroup(account, memberGroup)
                        for(Member member : members) {
                            member.group = null
                            member.save(failOnError: true)
                        }
                        memberGroup.delete()
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" synchGroups account[${account.name}] error!".toString(), e)
        }
    }

    void synchUsers(Account account) {
        try {
            log.info(" synchUsers account[${account.name}]".toString())
            def memberMap = [:]
            Member.findAllByAccount(account).each { member ->
                memberMap.put(member.openId, member)
            }
            ListOpenIdsResult result = synchNextUsers(account, null, memberMap)
            if(result){
                int total = result.total
                int count = result.count
                String nextOpenId = result.nextOpenId
                while (total != count && 0 != count && nextOpenId) {
                    result = synchNextUsers(account, nextOpenId, memberMap)
                    if(result){
                        count += result.count
                        if(nextOpenId != result.nextOpenId){
                            nextOpenId = result.nextOpenId
                        }
                        else {
                            count = total
                            nextOpenId = null
                        }
                    }
                }
            }

            memberMap.each {
                def member = (Member) it.value
                member.subscribe = false
                member.save(flush: true)
            }
        }
        catch (Exception e) {
            log.error(" synchUsers account[${account.name}] error!".toString(), e)
        }
    }

    private ListOpenIdsResult synchNextUsers(Account account, String nextOpenId, Map<String, Member> memberMap) {
        try {
            log.info(" synchNextUsers account[${account.name}]".toString())
            def defaultGrade = MemberGrade.findByAccountAndGrade(account, 1)
            ListOpenIdsResult result = wechatUserService.listOpenIds(account, nextOpenId)
            if(result.openIds && !result.openIds.empty){
                def currentNo = memberNumberService.currentNumber(account)
                def allocatedNoMap = memberNumberService.allocatedNumbers(account)
                wechatAccountService.refreshAccessToken(account)
                def members = wechatUserService.pull(account, result.openIds)
                members.each { user ->
                    def member = memberMap.remove(user.openId)
                    if(!member){
                        member = new Member()
                        member.grade = defaultGrade
                        member.no = memberNumberService.allocation(account, currentNo + 1, allocatedNoMap)
                        currentNo = member.no
                    }
                    if(user.subscribe){
                        member.openId = user.openId
                        member.nickName = user.nickName
                        if(member.nickName){
                            member.nickName.replaceAll("[\\ue000-\\uefff]", "")
                            member.nickName = EmojiFilterUtil.filterEmoji(member.nickName)
                        }
                        member.sex = user.sex
                        member.language = user.language
                        member.city = user.city
                        member.province = user.province
                        member.country = user.country
                        member.headimgurl = user.headimgurl
                        member.subscribeTime = user.subscribeTime
                        member.unionid = user.unionid
                        if(LocationUtil.isCentralCity(member.province)){
                            member.location = Location.findCentralCity(member.province, member.city).get()
                        }
                        else {
                            member.location = Location.findCity(member.province, member.city).get()
                        }
                    }
                    member.subscribe = user.subscribe
                    member.account = Account.proxy(account.id)
                    member.save(failOnError: true)
                }
            }
            return result
        }
        catch (Exception e) {
            log.error(" synchNextUsers account[${account.name}] error!".toString(), e)
        }
        return null
    }

    void synchUsersGroup(Account account) {
        try {
            log.info(" synchUsersGroup account[${account.name}]".toString())
            def members = Member.findAllByAccountAndSubscribe(account, true)
            if(members && !members.empty) {
                def groupMap = [:]
                def groups = MemberGroup.findAllByAccount(account)
                groups.each {
                    groupMap.put(it.groupId, it)
                }
                wechatAccountService.refreshAccessToken(account)
                members.each { member ->
                    def groupId = wechatUserService.searchGroup(account, member)
                    if(null != groupId){
                        def group = (MemberGroup) groupMap.get(groupId)
                        if(group){
                            member.group = group
                            member.save(failOnError: true)
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" synchUsersGroup account[${account.name}] error!".toString(), e)
        }
    }

    Member synchUser(Account account, String openId) {
        try {
            log.info(" synchUser account[${account.name}] openId: ${openId}".toString())
            def defaultGrade = MemberGrade.findByAccountAndGrade(account, 1)
            wechatAccountService.refreshAccessToken(account)
            def user = wechatUserService.pull(account, openId)
            if(user){
                def currentNo = memberNumberService.currentNumber(account)
                def allocatedNoMap = memberNumberService.allocatedNumbers(account)
                def member = Member.findByOpenId(user.openId)
                if(!member){
                    member = new Member()
                    member.grade = defaultGrade
                    member.no = memberNumberService.allocation(account, currentNo + 1, allocatedNoMap)
                }
                member.openId = user.openId
                member.nickName = user.nickName
                if(member.nickName){
                    member.nickName.replaceAll("[\\ue000-\\uefff]", "")
                    member.nickName = EmojiFilterUtil.filterEmoji(member.nickName)
                }
                member.sex = user.sex
                member.language = user.language
                member.city = user.city
                member.province = user.province
                member.country = user.country
                member.headimgurl = user.headimgurl
                member.subscribeTime = user.subscribeTime
                member.unionid = user.unionid
                if(LocationUtil.isCentralCity(member.province)){
                    member.location = Location.findCentralCity(member.province, member.city).get()
                }
                else {
                    member.location = Location.findCity(member.province, member.city).get()
                }
                member.subscribe = user.subscribe
                member.account = Account.proxy(account.id)
                member.save(failOnError: true)
                return member
            }
        }
        catch (Exception e) {
            log.error(" synchUser account[${account.name}] openId: ${openId}, error!".toString(), e)
        }
        return null
    }

    void synchUserGroup(Account account, Member member) {
        try {
            log.info(" synchUserGroup account[${account.name}] member: ${member.id}".toString())
            if(member.subscribe){
                wechatAccountService.refreshAccessToken(account)
                def groupId = wechatUserService.searchGroup(account, member)
                if(null != groupId){
                    def group = MemberGroup.get(groupId)
                    if(group){
                        member.group = group
                        member.save(failOnError: true)
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" synchUserGroup account[${account.name}] member: ${member.id}, error!".toString(), e)
        }
    }
}
