package com.seecent.platform.auth

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class PersonalController {
    def springSecurityService

    def index() {
        def user = getPersonalUser()
        if (user == null) {
            notFound()
            return
        }
        respond user
    }

    def roles() {
        def user = getPersonalUser()
        respond user.authorities.authority
    }

    def user() {
        def user = getPersonalUser()
        render ([initCompleted: user.initCompleted] as JSON)
    }

    @Transactional
    def update() {
        def data = request.JSON
        def userInstance = getPersonalUser() as User
        if (userInstance == null) {
            notFound()
            return
        }
        userInstance.username = data.username
        userInstance.email = data.email
        if (data.password) {
            userInstance.password = data.password
        }
        if (!userInstance.validate()) {
            respond userInstance.errors
            return
        }
        userInstance.save flush: true
        respond userInstance, [status: OK]
    }

    private getPersonalUser() {
        return springSecurityService.currentUser
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
