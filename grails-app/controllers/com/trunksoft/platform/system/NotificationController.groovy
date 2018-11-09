package com.trunksoft.platform.system

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class NotificationController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Notification.createCriteria().list(params) {
        }
        def notificationCount = result.totalCount
        def notificationList = result.collect {
            [id: it.id]
        }
        response.setHeader('totalItems', notificationCount as String)
        respond notificationList
    }

    def show(Notification notificationInstance) {
        respond notificationInstance
    }

    def create() {
        respond new Notification(params)
    }

    @Transactional
    def save(Notification notificationInstance) {
        if (notificationInstance == null) {
            notFound()
            return
        }
        if (notificationInstance.hasErrors()) {
            respond notificationInstance.errors, view: 'create'
            return
        }
        notificationInstance.save failOnError: true
        respond notificationInstance, [status: CREATED]
    }

    def edit(Notification notificationInstance) {
        respond notificationInstance
    }

    @Transactional
    def update(Notification notificationInstance) {
        if (notificationInstance == null) {
            notFound()
            return
        }
        if (notificationInstance.hasErrors()) {
            respond notificationInstance.errors, view: 'edit'
            return
        }
        notificationInstance.save flush: true
        respond notificationInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def notificationInstance = Notification.get(params.id)
        if (notificationInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}