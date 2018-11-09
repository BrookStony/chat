package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.type.PictureType
import com.trunksoft.chat.util.FileUtil
import com.trunksoft.chat.util.Uploader
import grails.transaction.Transactional
import groovyx.gpars.GParsPool
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

class UploadArticleService {

    static boolean transactional = false

    def materialManageService
    def wechatMediaService
    def wechatMaterialService

    @Transactional
    Picture upload(Account account, HttpServletRequest request) {
        if(request instanceof MultipartHttpServletRequest){
            def file = request.getFile('picture')
            return upload(account, file)
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
                picture.article = true
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
//        GParsPool.withPool() {
            Picture.withTransaction {
                def result = wechatMaterialService.upload(account, picture)
                picture.bindResult(result)
                picture.save(failOnError: true)
            }
//        }
    }

    def uploadUMEditorPicture(Account account, Uploader up, HttpServletRequest request){
        if(request instanceof MultipartHttpServletRequest){
            def file = request.getFile('upfile')
            if(!file.isEmpty()){
                def fileName = file.originalFilename
                up.originalName = file.originalFilename
                if(!up.checkFileType(up.originalName)){
                    up.state = up.errorInfo.get("TYPE");
                    return
                }
                up.size = file.size
                if(file.size>up.maxSize){
                    up.state = up.errorInfo.get("SIZE")
                    return
                }
                up.fileName = up.getName(up.originalName);
                up.type = up.getFileExt(up.fileName);

                def completeFilePath = materialManageService.createCompleteFilePath(account, fileName, MaterialType.PICTURE.dir)
                def urlPath = materialManageService.createUrlPath(account, fileName, MaterialType.PICTURE.dir)
                up.url = materialManageService.serverUrl(urlPath)
                File picture = new File(completeFilePath)
                try {
                    file.transferTo(picture)
                    up.state = up.errorInfo.get("SUCCESS")
                }catch (IOException e){
                    up.state = up.errorInfo.get("IO")
                    e.printStackTrace()
                }
            }else{
                up.state = up.errorInfo.get("NOFILE")
                return
            }
        }
    }
}
