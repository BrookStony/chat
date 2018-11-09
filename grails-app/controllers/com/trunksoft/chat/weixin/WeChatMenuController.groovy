package com.trunksoft.chat.weixin

import com.trunksoft.chat.Account
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.type.ChatMenuType
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)

class WeChatMenuController {

    def wechatMenuService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        def accountId = params.long("accountId")
        def account = Account.get(accountId)
        def weChatMenuCount = WeChatMenu.countByAccount(account)
        def weChatMenuList = WeChatMenu.where {
            eq("account", account)
        }.order("no").findAll()

        def weChatMenus = []
        weChatMenuList.each { WeChatMenu menu ->
            if(null == menu.parent) {
                def weChatMenuData = toWeChatMenu(menu)
                weChatMenuData.subMenus = []
                weChatMenus << weChatMenuData
            }
        }
        weChatMenuList.each { WeChatMenu menu ->
            if(null != menu.parent) {
                weChatMenus.each { parent ->
                    if(menu.parent.id == parent.id){
                        parent.subMenus << toWeChatMenu(menu)
                    }
                }
            }
        }
        response.setHeader('totalItems', weChatMenuCount as String)
        respond weChatMenus
    }

    private static def toWeChatMenu(WeChatMenu menu) {
        [id: menu.id, name: menu.name,
         parent: [id: menu.parent?.id], account: [id: menu.account.id],
         code: menu.code, no: menu.no, level: menu.level,
         type: menu.type.ordinal(), url: menu.url,
         eventKey: menu.eventKey]
    }

    def show(WeChatMenu weChatMenuInstance) {
        respond weChatMenuInstance
    }

    def create() {
        respond new WeChatMenu(params)
    }

    @Transactional
    def save() {
        def data = request.JSON
        def weChatMenuInstance = new WeChatMenu()
        weChatMenuInstance.properties = data
        if (!weChatMenuInstance.validate()) {
            respond weChatMenuInstance.errors
            return
        }
        weChatMenuInstance.save flush: true
        respond weChatMenuInstance, [status: CREATED]
    }

    def edit(WeChatMenu weChatMenuInstance) {
        respond weChatMenuInstance
    }

    @Transactional
    def update() {
        def data = request.JSON
        def weChatMenuInstance = WeChatMenu.get(data.id as Long)
        if (weChatMenuInstance == null) {
            notFound()
            return
        }
        weChatMenuInstance.properties = data
        if (!weChatMenuInstance.validate()) {
            respond weChatMenuInstance.errors
            return
        }
        respond weChatMenuInstance, [status: OK]
    }

    @Transactional
    def updateAll() {
        def data = request.JSON
        def weChatMenuInstance = WeChatMenu.get(data.id as Long)
        if (weChatMenuInstance == null) {
            notFound()
            return
        }
        weChatMenuInstance.properties = data
        if (!weChatMenuInstance.validate()) {
            respond weChatMenuInstance.errors
            return
        }
        respond weChatMenuInstance, [status: OK]
    }

    @Transactional
    def saveMenu() {
        def menu = request.JSON
        if(null != menu.id){
            def weChatMenuInstance = WeChatMenu.get(menu.id as Long)
            if (weChatMenuInstance == null) {
                notFound()
                return
            }
            weChatMenuInstance.name = menu.name
            weChatMenuInstance.code = menu.no
            weChatMenuInstance.no = menu.no
            if(ChatMenuType.CLICK.ordinal() == menu.type) {
                weChatMenuInstance.type = ChatMenuType.CLICK
                weChatMenuInstance.eventKey = menu.eventKey
            }
            else {
                weChatMenuInstance.type = ChatMenuType.VIEW
                weChatMenuInstance.url = menu.url
            }
            weChatMenuInstance.save(failOnError: true)
        }
        else {
            def weChatMenuInstance = new WeChatMenu()
            weChatMenuInstance.account = Account.proxy(menu.account.id)
            if(menu.parent && null != menu.parent.id){
                weChatMenuInstance.parent = WeChatMenu.proxy(menu.parent.id)
            }
            weChatMenuInstance.name = menu.name
            weChatMenuInstance.level = menu.level
            weChatMenuInstance.code = menu.no
            weChatMenuInstance.no = menu.no
            if(ChatMenuType.CLICK.ordinal() == menu.type) {
                weChatMenuInstance.type = ChatMenuType.CLICK
                weChatMenuInstance.eventKey = menu.eventKey
            }
            else {
                weChatMenuInstance.type = ChatMenuType.VIEW
                weChatMenuInstance.url = menu.url
            }
            weChatMenuInstance.save(failOnError: true)
        }
        render status: OK
    }

    @Transactional
    def saveMenusSort() {
        def menus = request.JSON
        menus.each { menu ->
            def weChatMenu = WeChatMenu.get(menu.id as Long)
            if(weChatMenu){
                weChatMenu.no = menu.no
                weChatMenu.code = menu.no
                weChatMenu.save(failOnError: true)
            }
        }
        render status: OK
    }

    @Transactional
    def delete(WeChatMenu weChatMenuInstance) {
        if (weChatMenuInstance == null) {
            notFound()
            return
        }
        def account = weChatMenuInstance.account
        if(null == weChatMenuInstance.parent){
            WeChatMenu.where {
                parent == weChatMenuInstance
            }.deleteAll()
        }
        weChatMenuInstance.delete flush: true
        def weChatMenus = WeChatMenu.where {
            isNull("parent")
            eq("account", account)
            order("no")
        }
        weChatMenus.eachWithIndex { weChatMenu, i ->
            weChatMenu.no = i + 1
            weChatMenu.code = weChatMenu.no
            weChatMenu.save(failOnError: true)

            def subMenus = WeChatMenu.where {
                eq("parent", weChatMenu)
                order("no")
            }
            subMenus.eachWithIndex{ sunMenu, j ->
                sunMenu.no = weChatMenu.no * 10 + j + 1
                sunMenu.code = sunMenu.no
                sunMenu.save(failOnError: true)
            }
        }
        render status: NO_CONTENT
    }

    @Transactional
    def publish() {
        def accountId = params.long("accountId")
        def account = Account.get(accountId)
        def weChatMenuList = WeChatMenu.where {
            eq("account", account)
        }.order("no").findAll()

        def weChatMenus = []
        weChatMenuList.each { WeChatMenu menu ->
            if(null == menu.parent) {
                menu.subMenus = []
                weChatMenus << menu
            }
        }
        weChatMenuList.each { WeChatMenu menu ->
            if(null != menu.parent) {
                weChatMenus.each { parent ->
                    if(menu.parent.id == parent.id){
                        parent.subMenus << menu
                    }
                }
            }
        }
        if(weChatMenus && !weChatMenus.empty){
            def result = wechatMenuService.create(account, weChatMenus)
            if(result.isOk()){
                render status: CREATED
                return
            }
        }
        render status: NO_CONTENT
    }

    @Transactional
    def pull() {
        try {
            def accountId = params.long("accountId")
            def account = Account.get(accountId)
            def weChatMenuList = wechatMenuService.pull(account)
            if(weChatMenuList){
                WeChatMenu.where {
                    isNotNull("parent")
                    eq("account", account)
                }.deleteAll()

                WeChatMenu.where {
                    eq("account", account)
                }.deleteAll()
                weChatMenuList.each { menu ->
                    menu.account = account
                    menu.save(failOnError: true)
                    if(menu.subMenus) {
                        menu.subMenus.each { WeChatMenu subMenu ->
                            subMenu.parent = menu
                            subMenu.account = account
                            subMenu.save(failOnError: true)
                        }
                    }
                }
            }
        }
        catch (Exception e) {
//            e.printStackTrace()
            log.error(" pull error", e)
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }

}
