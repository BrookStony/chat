package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import grails.converters.JSON
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AudioController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def uploadAudioService
    def wechatMediaService

    def index(Integer max) {
        params.max = Math.min(max ?: 10 ,100)
        def audioCount = Audio.count()
        def audioList = Audio.list(params).collect {
            [id: it.id, name: it.name, path: it.path,
                    time: it.time, size: it.mediaSize, description: it.description,
                    size: it.mediaSize, type: it.type]
        }
        response.setHeader("totalItems", audioCount as String)
        respond audioList
    }

    @Transactional
    def upload() {
        def account = Account.get(1)
        def data = uploadAudioService.upload(account,request);
        render ([data: data] as JSON)
    }

    def download() {
        def account = Account.get(1)
        if(account){
            def audio = Audio.get(params.is as long)
            if(audio){
                def result = wechatMediaService.download(account, audio)
            }
        }
        def path = params.path
        def name = params.fileName
        def file = new File(path)
        response.setContentLength((int) file.length())
        response.setHeader("Content-Disposition", "attachment;filename=" + name)
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gbk"), "iso-8859-1"))
        file.withInputStream {
            response.outputStream << it
        }
    }
}
