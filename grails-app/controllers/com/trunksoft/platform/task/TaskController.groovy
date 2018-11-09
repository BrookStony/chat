package com.trunksoft.platform.task

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class TaskController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Task.createCriteria().list(params) {
        }
        def taskCount = result.totalCount
        def taskList = result.collect {
            [id: it.id]
        }
        response.setHeader('totalItems', taskCount as String)
        respond taskList
    }

    def show(Task taskInstance) {
        respond taskInstance
    }

    def create() {
        respond new Task(params)
    }

    @Transactional
    def save(Task taskInstance) {
        if (taskInstance == null) {
            notFound()
            return
        }
        if (taskInstance.hasErrors()) {
            respond taskInstance.errors, view: 'create'
            return
        }
        taskInstance.save failOnError: true
        respond taskInstance, [status: CREATED]
    }

    def edit(Task taskInstance) {
        respond taskInstance
    }

    @Transactional
    def update(Task taskInstance) {
        if (taskInstance == null) {
            notFound()
            return
        }
        if (taskInstance.hasErrors()) {
            respond taskInstance.errors, view: 'edit'
            return
        }
        taskInstance.save flush: true
        respond taskInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def taskInstance = Task.get(params.id)
        if (taskInstance == null) {
            notFound()
            return
        }
        render status: NO_CONTENT
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}