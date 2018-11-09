package com.seecent.platform.auth

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class UserController {

    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def userIds
        def systemAdminRole = Role.findByAuthority(Role.ROLE_SYSTEM_ADMIN)
        if(systemAdminRole){
            if(!currentUserIsSuperAdmin(systemAdminRole)){
                userIds = UserRole.findAllByRole(systemAdminRole)*.user*.id
            }
        }

        def result = User.createCriteria().list(params) {
            if(params.filter) {
                or {
                    like("username", "%${params.filter}%".toString())
                    like("name", "%${params.filter}%".toString())
                }
            }
            if(userIds && userIds.size() > 0){
                not {
                    'in'("id", userIds)
                }
            }
        }
        def userCount = result.totalCount
        def userList = result
        response.setHeader('totalItems', userCount as String)
        respond userList
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {
        respond new User(params)
    }

    @Transactional
    def save() {
        def data = request.JSON
        def userInstance = new User()
        userInstance.properties = data
        if (!userInstance.validate()) {
            respond userInstance.errors
            return
        }
        userInstance.save flush: true
        data.roles.collect {
            return Role.get(it.id as Long)
        }.each { role ->
            if (role) {
                UserRole.create(userInstance, role)
            }
        }
        respond userInstance, [status: CREATED]
    }

    def edit(User userInstance) {
        respond userInstance
    }

    @Transactional
    def update() {
        def data = request.JSON
        def userInstance = User.get(data.id as Long)
        if (userInstance == null) {
            notFound()
            return
        }
        data.remove("dateCreated")
        data.remove("lastUpdated")
        userInstance.properties = data
        if (!userInstance.validate()) {
            respond userInstance.errors
            return
        }
        userInstance.save flush: true
        UserRole.where {
            user == userInstance
        }.deleteAll()
        data.roles.collect {
            return Role.get(it.id as Long)
        }.each { role ->
            if (role) {
                UserRole.create(userInstance, role)
            }
        }
        respond userInstance, [status: OK]
    }

    @Transactional
    def delete(User userInstance) {
        if (userInstance == null) {
            notFound()
            return
        }
        if (userInstance.id == springSecurityService.currentUser.id) {
            render status: NOT_ACCEPTABLE
            return
        }
        UserRole.where {
            user == userInstance
        }.deleteAll()
        userInstance.delete flush: true
        render status: NO_CONTENT
    }

    private boolean currentUserIsSuperAdmin(systemAdminRole) {
        def currentUser = springSecurityService.currentUser
        return currentUser.authorities.contains(systemAdminRole)
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
