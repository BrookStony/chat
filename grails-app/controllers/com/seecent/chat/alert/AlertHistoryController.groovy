package com.seecent.chat.alert

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AlertHistoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = AlertHistory.createCriteria().list(params) {
        }
        def alertHistoryCount = result.totalCount
        def alertHistoryList = result.collect {
            [id: it.id]
        }
        response.setHeader('totalItems', alertHistoryCount as String)
        respond alertHistoryList
    }

    def show(AlertHistory alertHistoryInstance) {
        respond alertHistoryInstance
    }

    def create() {
        respond new AlertHistory(params)
    }

    @Transactional
    def save(AlertHistory alertHistoryInstance) {
        if (alertHistoryInstance == null) {
            notFound()
            return
        }
        if (alertHistoryInstance.hasErrors()) {
            respond alertHistoryInstance.errors, view: 'create'
            return
        }
        alertHistoryInstance.save failOnError: true
        respond alertHistoryInstance, [status: CREATED]
    }

    def edit(AlertHistory alertHistoryInstance) {
        respond alertHistoryInstance
    }

    @Transactional
    def update(AlertHistory alertHistoryInstance) {
        if (alertHistoryInstance == null) {
            notFound()
            return
        }
        if (alertHistoryInstance.hasErrors()) {
            respond alertHistoryInstance.errors, view: 'edit'
            return
        }
        alertHistoryInstance.save flush: true
        respond alertHistoryInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def alertHistoryInstance = AlertHistory.get(params.id)
        if (alertHistoryInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}