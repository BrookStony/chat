package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.util.MathUtil
import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class PictureController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", remove: "POST", removeAll: "POST"]

    def uploadImageService
    def pictureManageService
    def wechatMediaService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.order = params.order ?: "desc"
        def pictureGroupId = params.long("pictureGroupId")
        def pictureGroup = PictureGroup.get(pictureGroupId)
        def accountId = params.long("accountId")
        def permanent = params.boolean("permanent")
        def result = Picture.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
            if(permanent){
                isNotNull("mediaUrl")
            }
            if(pictureGroup){
                eq("pictureGroup", pictureGroup)
            } else {
                isNull("pictureGroup")
            }
            order("dateCreated", "desc")
        }
        def pictureInstanceCount = result.totalCount
        def pictureInstanceList = result.collect {
            toPicture(it)
        }
        response.setHeader("totalItems", pictureInstanceCount as String)
        respond pictureInstanceList
    }

    def findAllNotInGroup(){
        def accountId = params.long("accountId")
        def permanent = params.boolean("permanent")
        def result = Picture.createCriteria().list(params) {
            account {
                eq("id", accountId)
            }
            if(permanent){
                isNotNull("mediaUrl")
            }
            isNull("pictureGroup")
            order("dateCreated", "desc")
        }
        def pictureInstanceCount = result.totalCount
        def pictureInstanceList = result.collect {
            toPicture(it)
        }
        response.setHeader("totalItems", pictureInstanceCount as String)
        respond pictureInstanceList
    }

    protected def toPicture(Picture it){
        [id: it.id, name: it.name, pictureGroup: it.pictureGroup,
         size: MathUtil.format(it.mediaSize), path: it.path,
         mediaUrl: it.mediaUrl, article: it.article,
         url: pictureManageService.serverUrl(it.url),
         type: it.type, description: it.description]
    }

    @Transactional
    def upload() {
        def accountInstance = Account.get(params.accountId as long)
        if(null == accountInstance){
            notFound()
            return
        }
        def result = uploadImageService.upload(accountInstance, request)
        def pictureInstanceList = result.collect {
            toPicture(it)
        }
        render pictureInstanceList as JSON
    }

    def download() {
        def pictureId = params.id as long
        def name = params.fileName

        def pictureInstance = Picture.get(pictureId)
        if(pictureInstance){
            def filePath = pictureManageService.completeFilePath(pictureInstance.path)
            def file = new File(filePath)
            if(file.exists()) {
                response.setContentLength((int) file.length())
                response.setHeader("Content-Disposition", "attachment;filename=" + name)
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gbk"), "iso-8859-1"))
                file.withInputStream {
                    response.outputStream << it
                }
                return
            }
        }
        render status: NO_CONTENT
    }

    @Transactional
    def remove(Long id){
        def pictureInstance = Picture.get(id)
        if(null == pictureInstance){
            notFound()
            return
        }
        if(false == pictureInstance.article){
            pictureInstance.delete(flush: true)
            pictureManageService.deleteFile(pictureInstance.path)
            pictureManageService.deleteMaterialFile(pictureInstance)
        }
        render status: NO_CONTENT
    }

    @Transactional
    def removeAll(){
        def json = request.JSON
        def ids = json.ids as List
        ids.each {
            def pictureInstance = Picture.get(it)
            if(pictureInstance && pictureInstance.article == false){
                pictureInstance.delete(flush: true)
                pictureManageService.deleteFile(pictureInstance.path)
                pictureManageService.deleteMaterialFile(pictureInstance)
            }
        }
        render status: NO_CONTENT
    }

    @Transactional
    def updateName() {
        def json = request.JSON
        def id = json.id as Long
        def name = json.name as String
        def pictureInstance = Picture.get(id)

        if (pictureInstance == null) {
            notFound()
            return
        }

        if (pictureInstance.hasErrors()) {
            respond pictureInstance.errors, view: 'edit'
            return
        }
        pictureInstance.name = name
        pictureInstance.save flush: true

        respond pictureInstance, [status: OK]
    }

    @Transactional
    def moveToGroup() {
        def json = request.JSON
        def groupId = json.groupId as Long
        def pictureIds = json.ids as List
        def pictureGroup = PictureGroup.get(groupId)
        pictureIds.each {
            def picture = Picture.get(it as Long)
            if(pictureGroup){
                picture.pictureGroup = pictureGroup
                picture.save(flush: true)
            }else {
                picture.pictureGroup = null
                picture.save(flush: true)
            }

        }
        render status: OK
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
