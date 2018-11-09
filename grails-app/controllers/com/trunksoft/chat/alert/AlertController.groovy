package com.trunksoft.chat.alert

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AlertController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Alert.createCriteria().list(params) {
        }
        def alertCount = result.totalCount
        def alertList = result.collect {
            [id: it.id]
        }
        response.setHeader('totalItems', alertCount as String)
        respond alertList
    }

    def show(Alert alertInstance) {
        respond alertInstance
    }

    def create() {
        respond new Alert(params)
    }

    @Transactional
    def save(Alert alertInstance) {
        if (alertInstance == null) {
            notFound()
            return
        }
        if (alertInstance.hasErrors()) {
            respond alertInstance.errors, view: 'create'
            return
        }
        alertInstance.save failOnError: true
        respond alertInstance, [status: CREATED]
    }

    def edit(Alert alertInstance) {
        respond alertInstance
    }

    @Transactional
    def update(Alert alertInstance) {
        if (alertInstance == null) {
            notFound()
            return
        }
        if (alertInstance.hasErrors()) {
            respond alertInstance.errors, view: 'edit'
            return
        }
        alertInstance.save flush: true
        respond alertInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def alertInstance = Alert.get(params.id)
        if (alertInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
