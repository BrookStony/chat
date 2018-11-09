package com.trunksoft.chat.assets

import com.trunksoft.chat.Account

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PictureGroupController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        def accountId = params.long("accountId")
        def permanent = params.boolean("permanent")
        def result = PictureGroup.createCriteria().list {
            account {
                eq("id", accountId)
            }
        }
        def pictureGroupList = result.collect {
            toPictureGroup(it, permanent)
        }
        respond pictureGroupList
    }

    protected def toPictureGroup(PictureGroup it, Boolean permanent){
        int pictureCount = 0
        if(permanent) {
            pictureCount = Picture.countByPictureGroupAndMediaUrlIsNotNull(it)
        }
        else {
            pictureCount = Picture.countByPictureGroup(it)
        }
        return [id: it.id, name: it.name, size: pictureCount]
    }

    def show(PictureGroup pictureGroupInstance) {
        respond pictureGroupInstance
    }

    def create() {
        respond new PictureGroup(params)
    }

    @Transactional
    def save(PictureGroup pictureGroupInstance) {
        if (pictureGroupInstance == null) {
            notFound()
            return
        }

        if (pictureGroupInstance.hasErrors()) {
            respond pictureGroupInstance.errors, view: 'create'
            return
        }

        pictureGroupInstance.save flush: true

        respond pictureGroupInstance, [status: CREATED]
    }

    def edit(PictureGroup pictureGroupInstance) {
        respond pictureGroupInstance
    }

    @Transactional
    def update(PictureGroup pictureGroupInstance) {
        if (pictureGroupInstance == null) {
            notFound()
            return
        }

        if (pictureGroupInstance.hasErrors()) {
            respond pictureGroupInstance.errors, view: 'edit'
            return
        }

        pictureGroupInstance.save flush: true

        respond pictureGroupInstance, [status: OK]
        }

    @Transactional
    def delete(PictureGroup pictureGroupInstance) {

        if (pictureGroupInstance == null) {
            notFound()
            return
        }

        pictureGroupInstance.delete flush: true

        render status: NO_CONTENT
    }

    @Transactional
    def remove(){
        def json = request.JSON
        def id = json.id as Long
        def accountId = json.accountId as Long
        def accountInstance = Account.get(accountId)
        PictureGroup.withTransaction {
            def pictureGroupInstance = PictureGroup.findByAccountAndId(accountInstance, id)
            if(null == pictureGroupInstance){
                notFound()
                return
            }
            def pictureInstanceList = Picture.findAllByAccountAndPictureGroup(accountInstance, pictureGroupInstance)
            pictureInstanceList.each {
                it.pictureGroup = null
                it.save(flush: true)
            }
            pictureGroupInstance.delete()
        }
        render status: OK
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
