package com.trunksoft.platform.system

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class MenuController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Menu.createCriteria().list(params) {
            if(params.filter) {
                or {
                    like("name", "%${params.filter}%".toString())
                    like("url", "%${params.filter}%".toString())
                }
            }
        }
        def menuCount = result.totalCount
        def menuList = result.collect {
            [id: it.id, code: it.code, name: it.name,
             cssicon: it.cssicon, sortno: it.sortno,
             url: it.url, level: it.level, enable: it.enable,
             parentName: it.parent?.name, parent: [id: it.parent?.id]]
        }
        response.setHeader('totalItems', menuCount as String)
        respond menuList
    }

    def searchMenus() {
        def level = params.int("level")
        def result = Menu.createCriteria().list() {
            eq("level", level)
            order("sortno", "asc")
        }
        respond result
    }

    def show(Menu menuInstance) {
        respond menuInstance
    }

    def create() {
        respond new Menu(params)
    }

    @Transactional
    def save(Menu menuInstance) {
        if (menuInstance == null) {
            notFound()
            return
        }
        if (menuInstance.hasErrors()) {
            respond menuInstance.errors, view: 'create'
            return
        }
        menuInstance.save failOnError: true
        respond menuInstance, [status: CREATED]
    }

    def edit(Menu menuInstance) {
        respond menuInstance
    }

    @Transactional
    def update(Menu menuInstance) {
        if (menuInstance == null) {
            notFound()
            return
        }
        if (menuInstance.hasErrors()) {
            respond menuInstance.errors, view: 'edit'
            return
        }
        menuInstance.save flush: true
        respond menuInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def menuInstance = Menu.get(params.id)
        if (menuInstance == null) {
            notFound()
            return
        }
        def menusCount = Menu.countByParent(menuInstance)
        if(0 == menusCount) {
            menuInstance.delete()
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}