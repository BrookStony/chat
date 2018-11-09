package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.chat.type.AudioType
import com.seecent.chat.type.MaterialType
import com.seecent.chat.util.FileUtil
import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest


class UploadAudioService {

    static boolean transactional = false

    def materialManageService
    def wechatMediaService

    Audio upload(Account account, HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            def file = request.getFile('audio')
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
    Audio upload(Account account, MultipartFile file) {
        try {
            if (account && file) {
                def fileName = file.originalFilename
                def fileType = FileUtil.suffixType(fileName)
                def uuid = UUID.randomUUID().toString()
                def newFileName = FileUtil.createFileName(uuid, fileType)
                def materialType = MaterialType.AUDIO
                //创建文件目录,返回图片保存地址
                def completeFilePath = materialManageService.createCompleteFilePath(account, newFileName, materialType.dir)
                def filePath = materialManageService.createFilePath(account, newFileName, materialType.dir)
                def urlPath = materialManageService.createUrlPath(account, newFileName, materialType.dir)
                materialManageService.transferTo(file, completeFilePath)

                def audio = new Audio()
                audio.uuid = uuid
                audio.account = Account.proxy(account.id)
                audio.name = fileName
                audio.path = filePath
                audio.fileType = fileType
                audio.url = urlPath
                audio.type = AudioType.codeOf(fileType)
                audio.mediaSize = file.size
                audio.save(failOnError: true)

                uploadToWeChat(account, audio)

                return audio
            }
        } catch (Exception e) {
            log.error("<upload> error: " + e.message, e)
        }
        return null
    }

    def uploadToWeChat(Account account, Audio audio) {
        log.info("<uploadToWeChat> account[${account.name}] audio: " + audio.name)
//        GParsPool.withPool() {
//            Picture.withTransaction {
//                def result = wechatMediaService.upload(account, audio)
//                audio.bindResult(result)
//                audio.save(failOnError: true)
//            }
//        }
    }
}
