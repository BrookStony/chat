package com.seecent.chat.alert

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AlertTypeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = AlertType.createCriteria().list(params) {
        }
        def alertTypeCount = result.totalCount
        def alertTypeList = result.collect {
            [id: it.id]
        }
        response.setHeader('totalItems', alertTypeCount as String)
        respond alertTypeList
    }

    def show(AlertType alertTypeInstance) {
        respond alertTypeInstance
    }

    def create() {
        respond new AlertType(params)
    }

    @Transactional
    def save(AlertType alertTypeInstance) {
        if (alertTypeInstance == null) {
            notFound()
            return
        }
        if (alertTypeInstance.hasErrors()) {
            respond alertTypeInstance.errors, view: 'create'
            return
        }
        alertTypeInstance.save failOnError: true
        respond alertTypeInstance, [status: CREATED]
    }

    def edit(AlertType alertTypeInstance) {
        respond alertTypeInstance
    }

    @Transactional
    def update(AlertType alertTypeInstance) {
        if (alertTypeInstance == null) {
            notFound()
            return
        }
        if (alertTypeInstance.hasErrors()) {
            respond alertTypeInstance.errors, view: 'edit'
            return
        }
        alertTypeInstance.save flush: true
        respond alertTypeInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def alertTypeInstance = AlertType.get(params.id)
        if (alertTypeInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}