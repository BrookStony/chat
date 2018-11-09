package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.type.PictureType
import com.trunksoft.chat.util.FileUtil
import grails.transaction.Transactional
import groovyx.gpars.GParsPool
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

class UploadImageService {

    static transactional = false

    def materialManageService
    def wechatMediaService
    def wechatMaterialService

    @Transactional
    List<Picture> upload(Account account, HttpServletRequest request) {
        def pictures = []
        if(request instanceof MultipartHttpServletRequest){
            def files = request.getFiles('picture')
            if(files != null && account){
                files.each { file ->
                    def picture = upload(account, file)
                    if(picture){
                        pictures << picture
                    }
                }
            }
        }
        return pictures
    }

    /**
     *
     * @param account
     * @param file
     * @return
     */
    @Transactional
    Picture upload(Account account, MultipartFile file) {
        try {
            if(account && file){
                def fileName = file.originalFilename
                def fileType = FileUtil.suffixType(fileName)
                def uuid = UUID.randomUUID().toString()
                def newFileName = FileUtil.createFileName(uuid, fileType)
                def materialType = MaterialType.PICTURE
                //创建文件目录,返回图片保存地址
                def completeFilePath = materialManageService.createCompleteFilePath(account, newFileName, materialType.dir)
                def filePath = materialManageService.createFilePath(account, newFileName, materialType.dir)
                def urlPath = materialManageService.createUrlPath(account, newFileName, materialType.dir)
                materialManageService.transferTo(file, completeFilePath)

                def picture = new Picture()
                picture.uuid = uuid
                picture.account = Account.proxy(account.id)
                picture.name = fileName
                picture.path = filePath
                picture.fileType = fileType
                picture.url = urlPath
                picture.type = PictureType.codeOf(fileType)
                picture.mediaSize = file.size
                picture.save(failOnError: true)

                //上传到微信公众号服务器
                uploadToWeChat(account, picture)

                return picture
            }
        }
        catch (Exception e) {
            log.error("<upload> error: " + e.message, e)
        }
        return null
    }

    def uploadToWeChat(Account account, Picture picture) {
        log.info("<uploadToWeChat> account[${account.name}] picture: " + picture.name)
        GParsPool.withPool() {
            Picture.withTransaction {
                //上传为临时素材
//                def result = wechatMediaService.upload(account, picture)
                //上传为永久素材
                def result = wechatMaterialService.upload(account, picture)
                picture.bindResult(result)
                picture.save(failOnError: true)
            }
        }
    }

}
