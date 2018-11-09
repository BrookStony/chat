package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.util.DateUtil
import com.trunksoft.util.MathUtil
import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

class VideoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def uploadVideoService
    def wechatMediaService

    def index(Integer max) {
        params.max = Math.min(max ?: 10 , 100)
        params.order = params.order ?: "desc"
        def videoCount = Video.count()
        def config = grailsApplication.config.chat.assets
        String server = config.server
        def videoList = Video.listOrderByDateCreated(params).collect{
            [id: it.id, name: it.name, type: it.type,
                    description: it.description, title: it.title,
                    size: MathUtil.format(it.mediaSize),
                    dateCreated: DateUtil.format(it.dateCreated),
                    path: server+it.path, costTime: it.costTime]
        }
        response.setHeader("totalItems", videoCount as String)
        respond videoList
    }

    @Transactional
    def edit(){
        def json = request.JSON
        def config = grailsApplication.config.chat.assets
        String server = config.server
        def video = Video.get(json.id as long)
        def data = [id: video.id, title: video.title,
                type:video.type, name:video.name,
                path:server+video.path, mediaSize:video.mediaSize,
                description:video.description,account:video.account,
                materialType: video.materialType,dateCreated:video.dateCreated]
        respond data
    }

    @Transactional
    def upload(){
        def accountId = params.accountId as long
        def account = Account.get(accountId)
        def data = uploadVideoService.upload(account, request)
        def config = grailsApplication.config.chat.assets
        String server = config.server
        data.path = server+ data.path
        render (data as JSON)
    }

    @Transactional
    def saveVideo(){
        def json = request.JSON
        def video = json.video
        def account = Account.get(video.account.id as long)
        def path = video.path as String
        def videoPath = path.substring(path.indexOf(account.weixin))
        video.path = videoPath
        def videoInstance = new Video(video as Map)
        videoInstance.save(flush: true)
        def result = wechatMediaService.upload(account, videoInstance)
        videoInstance.bindResult(result)
        respond status: CREATED
    }

    @Transactional
    def updateVideo(){
        def json = request.JSON
        def video = json.video
        def account = Account.get(video.account.id as long)
        def path = video.path as String
        def videoPath = path.substring(path.indexOf(account.weixin))
        video.path = videoPath
        def videoInstance = Video.get(json.id as long)
        videoInstance.properties = video as Map
        videoInstance.save(flush: true)
        respond status: CREATED
    }

    @Transactional
    def remove(){
        def json = request.JSON
        if(json.id){
            Video.get(json.id as long).delete()
        }
        def accountId = json.accountId as long
        def account = Account.get(accountId)
        def path = json.path
        def config = grailsApplication.config.chat.assets
        String basePath = config.basePath
        def pathName = basePath+path.substring(path.indexOf(account.weixin))
        def file = new File(pathName)
        if(file.exists() && file.isFile()){
            file.delete()
        }
        render status: NO_CONTENT
    }

    def download(){
        def accountId = params.accountId as long
        def videoId = params.id as long
        def name = params.fileName as String
        def path = params.path as String

        def account = Account.get(accountId)
        def video = Video.get(videoId)
        if(account && video){
            def result = wechatMediaService.download(account, video)
            println 'result: ' + result.message
        }

        def config = grailsApplication.config.chat.assets
        String basePath = config.basePath
        def filePath = basePath+path.substring(path.indexOf(account.weixin))
        def file = new File(filePath)
        response.setContentLength((int) file.length())
        response.setHeader("Content-Disposition", "attachment;filename=" + name)
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gbk"), "iso-8859-1"))
        file.withInputStream {
            response.outputStream << it
        }
    }
}
