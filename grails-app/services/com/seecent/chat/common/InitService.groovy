package com.seecent.chat.common

import com.seecent.chat.message.MessageTemplate
import com.seecent.chat.status.AccountStatus
import com.seecent.platform.auth.Requestmap
import com.seecent.platform.auth.Role
import com.seecent.platform.auth.RoleMenu
import com.seecent.platform.auth.User
import com.seecent.platform.auth.UserRole
import com.seecent.platform.status.ApiAccountStatus
import com.seecent.chat.Account
import com.seecent.chat.northapi.ApiAccount
import com.seecent.chat.type.AccountType
import com.seecent.chat.type.TemplateType
import com.seecent.platform.system.Menu
import grails.util.Environment

class InitService {

    def grailsApplication

    def autoReplyService
    def menuInitService
    def locationInitService
    def memberGradeInitService
    def memberNumberService
    def bindAccountInitService

    def jsonMarshallerService

    def init() {
//        if (Environment.current == Environment.DEVELOPMENT) {
            initAccount()
            Account.list().each {
                memberNumberService.initBeautifulNumber(it)
                memberGradeInitService.initFromXml(it, "membergrade.xml")
                initMessageTemplate(it)
                initApiAccount(it)
            }
//        }
        menuInitService.initFromXml("menu.xml")
        initUsers()

        locationInitService.init()
        jsonMarshallerService.registerObjectMarshaller()

        autoReplyService.init()
        bindAccountInitService.init()
    }

    def initUsers() {
        if(!Requestmap.count()){
//            for (String url in [
//                    '/', '/index', '/index.gsp', '/**/favicon.ico',
//                    '/**/js/**', '/**/css/**', '/**/images/**',
//                    '/login/**', '/logout/**',
//                    '/rest/**', '/receiveMessage/**', '/memberApi/**',
//                    '/chatMessageApi/**', '/accessTokenApi/**']) {
//                new Requestmap(url: url, configAttribute: 'permitAll').save()
//            }
        }
        if(!User.count()){
            def systemRole = new Role(name: "系统管理员", authority: Role.ROLE_SYSTEM_ADMIN, description: "具有系统全部权限").save(flush: true)
            def adminRole = new Role(name: "管理员", authority: Role.ROLE_ADMIN, description: "管理员权限").save(flush: true)
            def userRole = new Role(name: "用户", authority: Role.ROLE_USER, description: "一般用户权限").save(flush: true)
            def administrator = new User(username: "administrator", password: "admin", name: "Administrator", email: "administrator@example.com").save(flush: true)
            def admin = new User(username: "admin", password: "admin", name: "Admin", email: "admin@example.com").save(flush: true)
            def user = new User(username: "chat", password: "chat", name: "Chat", email: "chat@example.com").save(flush: true)
            UserRole.create(administrator, systemRole, true)
            UserRole.create(admin, adminRole, true)
            UserRole.create(user, userRole, true)

            Menu.list().each {
                RoleMenu.create(it, systemRole)
                RoleMenu.create(it, adminRole)
                RoleMenu.create(it, userRole)
            }
        }
    }

    def initAccount() {
        try{
            if(!Account.count()){
                Account account = new Account(name: "晶纯网络-INMS", weixin: "IDNMS-SIN", appId: "wx38214d0f5a5f7ebf", appSecret: "32b6bd672bbfe615548445ed57259899")
                account.weixinId = "gh_392b58a54c6d"
                account.token = "kindsoft"
                account.apiUrl = "https://api.weixin.qq.com/cgi-bin"
                account.accessToken = "Q6Vm40jEVbCZSn3br-exWBnoZRkQXq4OdK7x-y5b4h3ny3KJ9uWh_uDmuE1hdBkM6xJdS2_ueuaAUQoILlr5jw"
                account.type = AccountType.SERVICE
                account.status = AccountStatus.ACTIVATE
                account.save(flush: true, failOnError: true)
                account.errors.each {
                    println it
                }

                Account account2 = new Account(name: "综合网管-INMS", weixin: "IDNMS-SIN", appId: "wx38214d0f5a5f7ebf", appSecret: "32b6bd672bbfe615548445ed57259899")
                account2.weixinId = "gh_392b58a54c6d"
                account2.token = "kindsoft"
                account2.apiUrl = "https://api.weixin.qq.com/cgi-bin"
                account2.accessToken = "Q6Vm40jEVbCZSn3br-exWBnoZRkQXq4OdK7x-y5b4h3ny3KJ9uWh_uDmuE1hdBkM6xJdS2_ueuaAUQoILlr5jw"
                account2.type = AccountType.SERVICE
                account2.status = AccountStatus.ACTIVATE
                account2.save(flush: true, failOnError: true)
                account2.errors.each {
                    println it
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    def initApiAccount(Account account) {
        try{
            if(!ApiAccount.countByAccount(account)){
                ApiAccount apiAccount = new ApiAccount(name: "信网综合网管", username: "IDNMS-SIN", appId: "idnms-sin", appSecret: "888888888")
                apiAccount.accessToken = "6666888" + account.id
                apiAccount.status = ApiAccountStatus.ACTIVATE
                apiAccount.account = account
                apiAccount.save(flush: true, failOnError: true)
                apiAccount.errors.each {
                    println it
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
    }

    def initMessageTemplate(Account account) {
        if(!MessageTemplate.countByAccount(account)){
            def template = new MessageTemplate(account: account)
            template.templateId = "HoTS3Oia0tav_30iVcg9cm8ueu9uSgKxB0keJLM8l6g"
            template.title = "告警通知"
            template.primaryIndustry = "IT科技"
            template.secondaryIndustry = "IT软件与服务"
            template.content = "{{first.DATA}}\n" +
                    "\n" +
                    "告警内容：{{content.DATA}}\n" +
                    "告警发生时间：{{occurtime.DATA}}\n" +
                    "{{remark.DATA}}"
            template.sample = "腾讯云监控告警恢复通知\n" +
                    "\n" +
                    "告警内容：10.221.44.198 磁盘分区0使用90%, 剩余0.7G.\n" +
                    "告警发生时间：2014-04-08 17:00:02\n" +
                    "\n" +
                    "请尽快处理（您可以点击这里参考告警类型说明及处理建议）."
            template.type = TemplateType.WECHAT
            template.save(flush: true, failOnError: true)

            def template2 = new MessageTemplate(account: account)
            template2.templateId = "Du_JOgHG8ou0CgGPUEQTcBqMa2Akosv_WlTsE9BDFBg"
            template2.title = "告警恢复通知"
            template2.primaryIndustry = "IT科技"
            template2.secondaryIndustry = "IT软件与服务"
            template2.content = "{{first.DATA}}\n" +
                    "\n" +
                    "告警内容：{{content.DATA}}\n" +
                    "告警发生时间：{{occurtime.DATA}}\n" +
                    "告警恢复时间：{{recovertime.DATA}}\n" +
                    "告警持续时长：{{lasttime.DATA}}\n" +
                    "{{remark.DATA}}"
            template2.sample = "腾讯云监控告警恢复通知\n" +
                    "\n" +
                    "告警内容：10.221.44.198 磁盘分区0使用90%, 剩余0.7G.\n" +
                    "告警发生时间：2014-04-08 17:00:02\n" +
                    "告警恢复时间：2014-04-08 17:20:02\n" +
                    "告警持续时长：20分钟\n" +
                    "\n" +
                    "您可以点击这里参考告警类型说明及处理建议."
            template2.type = TemplateType.WECHAT
            template2.save(flush: true, failOnError: true)
        }
    }
}
