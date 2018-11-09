package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.util.FileUtil
import grails.converters.JSON
import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartHttpServletRequest

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class MaterialController {

    def userService
    def materialService
    def materialManageService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = Material.createCriteria().list(params) {
        }
        def materialCount = result.totalCount
        def materialList = result.collect {
            toMateria(it)
        }
        response.setHeader('totalItems', materialCount as String)
        respond materialList
    }

    private def toMateria(Material it) {
        [id: it.id, name: it.name, path: it.path,
         fileType: it.fileType, type: it.materialType.name(),
         mediaSize: it.mediaSize, description: it.description,
         url: materialManageService.serverUrl(it.url),
         account: [id: it.account?.id, name: it.account?.name]]
    }

    def show(Material materialInstance) {
        respond materialInstance
    }

    def create() {
        respond new Material(params)
    }

    @Transactional
    def save(Material materialInstance) {
        if (materialInstance == null) {
            notFound()
            return
        }
        if (materialInstance.hasErrors()) {
            respond materialInstance.errors, view: 'create'
            return
        }
        materialInstance.save failOnError: true
        respond materialInstance, [status: CREATED]
    }

    def edit(Material materialInstance) {
        respond materialInstance
    }

    @Transactional
    def update(Material materialInstance) {
        if (materialInstance == null) {
            notFound()
            return
        }
        if (materialInstance.hasErrors()) {
            respond materialInstance.errors, view: 'edit'
            return
        }
        materialInstance.save flush: true
        respond materialInstance, [status: OK]
    }

    @Transactional
    def delete() {
        def materialInstance = Material.get(params.id)
        if (materialInstance == null) {
            notFound()
            return
        }
        def filePath = materialInstance.path
        materialInstance.delete()
        materialService.deleteFile(filePath)
        render status: NO_CONTENT
    }

    @Transactional
    def upload() {
        String fileId = params.fileId
        def accountId = params.long("accountId")
        def account = Account.get(accountId)
        Material materialInstance
        if(request instanceof MultipartHttpServletRequest) {
            def file = request.getFile(fileId)
            def fileName = file.originalFilename
            def fileType = FileUtil.suffixType(fileName)
            def type = MaterialType.suffixOf(fileType)
            def uuid = UUID.randomUUID().toString()
            def newFileName = FileUtil.createFileName(uuid, fileType)
            materialInstance = new Material(uuid: uuid, name: fileName, account: account, type: type, fileType: fileType)
            materialInstance.mediaSize = file.size
            if (account) {
                def completeFilePath = materialManageService.createCompleteFilePath(account, newFileName, type.dir)
                def filePath = materialManageService.createFilePath(account, newFileName, type.dir)
                def urlPath = materialManageService.createUrlPath(account, newFileName, type.dir)
                materialService.transferTo(file, completeFilePath)
                materialInstance.path = filePath
                materialInstance.url = materialManageService.serverUrl(urlPath)
            } else {
                def user = userService.currentUser()
                def completeFilePath = materialManageService.createCompleteFilePath(account, newFileName, type.dir)
                def filePath = materialManageService.createFilePath(user, newFileName, type.dir)
                def urlPath = materialManageService.createUrlPath(user, newFileName, type.dir)
                materialService.transferTo(file, completeFilePath)
                materialInstance.path = filePath
                materialInstance.url = materialManageService.serverUrl(urlPath)
            }
            materialInstance.save(failOnError: true)
        }
        if(null == materialInstance) {
            render status: NO_CONTENT
            return
        }
        render toMateria(materialInstance) as JSON
    }

    protected void notFound() {
        render status: NOT_FOUND
    }
}
