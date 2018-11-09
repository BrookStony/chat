package com.seecent.chat.assets

import com.seecent.chat.Account
import groovyx.gpars.GParsPool
import org.springframework.web.multipart.MultipartFile

class MaterialService {

    static transactional = false

    def materialPathService
    def wechatMediaService

    boolean transferTo(MultipartFile file, String path) {
        boolean result = false
        try {
            def tofile = new File(path)
            if(!tofile.exists()){
                file.transferTo(new File(path))
            }
            result = true
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        return result
    }

    boolean deleteFile(String filePath) {
        boolean result = false
        try {
            if(filePath) {
                def path = materialPathService.appendPath(materialPathService.basePath(), filePath)
                def file = new File(path)
                if(file.exists()) {
                    file.delete()
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace()
        }
        return result
    }

    def upload(Account account, Picture picture) {
        log.info(" upload account[${account.name}] picture: " + picture.name)
//        GParsPool.withPool() {
//            Picture.withTransaction {
//                def result = wechatMediaService.upload(account, picture)
//                picture.bindResult(result)
//                picture.save(failOnError: true)
//            }
//        }
    }

    def upload(Account account, Audio audio) {
        log.info(" upload account[${account.name}] audio: " +  audio.name)
        GParsPool.withPool {

        }
    }

    def upload(Account account, Video video) {
        log.info(" upload account[${account.name}] video: " + video.name)
        GParsPool.withPool {

        }
    }
}
