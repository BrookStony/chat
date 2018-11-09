package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.chat.type.MaterialType
import com.seecent.chat.type.VideoType
import com.seecent.chat.util.FileUtil
import grails.transaction.Transactional
import groovyx.gpars.GParsPool
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

class UploadVideoService {

    static boolean transactional = false

    def materialManageService
    def wechatMediaService

    Video upload(Account account, HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('video')
            if (!file.isEmpty()) {
                return upload(account, file)
            }
        }
        return null
    }

    /**
     *
     * @param account
     * @param file
     * @return
     */
    @Transactional
    Video upload(Account account, MultipartFile file) {
        try {
            if(account && file) {
                def fileName = file.originalFilename
                def fileType = FileUtil.suffixType(fileName)
                def uuid = UUID.randomUUID().toString()
                def newFileName = FileUtil.createFileName(uuid, fileType)
                def materialType = MaterialType.VIDEO
                //创建文件目录,返回图片保存地址
                def completeFilePath = materialManageService.createCompleteFilePath(account, newFileName, materialType.dir)
                def filePath = materialManageService.createFilePath(account, newFileName, materialType.dir)
                def urlPath = materialManageService.createUrlPath(account, newFileName, materialType.dir)
                materialManageService.transferTo(file, completeFilePath)

                def video = new Video()
                video.uuid = uuid
                video.account = Account.proxy(account.id)
                video.name = fileName
                video.path = filePath
                video.fileType = fileType
                video.url = urlPath
                video.type = VideoType.codeOf(fileType)
                video.mediaSize = file.size
                video.save(failOnError: true)

                uploadToWeChat(account, video)

                return video
            }
        } catch (Exception e) {
            log.error("<upload> error: " + e.message, e)
        }
        return null
    }

    def uploadToWeChat(Account account, Video video) {
        log.info("<uploadToWeChat> account[${account.name}] video: " + video.name)
//        GParsPool.withPool() {
//            Picture.withTransaction {
//                def result = wechatMediaService.upload(account, video)
//                video.bindResult(result)
//                video.save(failOnError: true)
//            }
//        }
    }
}
