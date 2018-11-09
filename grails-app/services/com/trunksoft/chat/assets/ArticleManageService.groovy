package com.trunksoft.chat.assets

import com.trunksoft.chat.Account
import com.trunksoft.chat.services.MaterialResult
import com.trunksoft.platform.auth.User

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * 图文素材管理
 */
class ArticleManageService {

    static transactional = false

    private static final DateFormat DF = new SimpleDateFormat("yyyyMMdd")

    def grailsApplication

    def wechatMaterialService

    /**
     * 从微信永久素材获取图文
     * @param account
     * @param mediaId
     * @return
     */
    Article getFromWeChat(Account account, String mediaId) {
        return wechatMaterialService.getArticle(account, mediaId)
    }

    /**
     * 添加到微信永久素材
     * @param article
     */
    MaterialResult addToWeChat(Article article) {
        def result = wechatMaterialService.addArticle(article.account, article)
        if(result.isOk() && result.mediaId) {
            article.mediaId = result.mediaId
        }
        return result
    }

    /**
     * 更新微信永久素材
     * @param article
     */
    MaterialResult updateToWeChat(Article article) {
        if(article.mediaId) {
            return wechatMaterialService.updateArticle(article.account, article)
        }
        return new MaterialResult(4, "MediaId is null!")
    }

    /**
     * 更新微信永久素材
     * @param articleItem
     */
    MaterialResult updateToWeChat(ArticleItem articleItem) {
        if(articleItem.article.mediaId) {
            return wechatMaterialService.updateArticleItem(articleItem.account, articleItem)
        }
        return new MaterialResult(4, "MediaId is null!")
    }

    /**
     * 删除微信永久素材
     * @param article
     */
    MaterialResult deleteWeChat(Article article) {
        if(article.mediaId) {
            return wechatMaterialService.deleteArticle(article.account, article.mediaId)
        }
        return new MaterialResult(4, "MediaId is null!")
    }

    /**
     * 保存图文内容到文件
     * @param article
     * @param account
     * @param content
     * @return
     */
    def saveContent(Article article, Account account, String content) {
        def uuid = article.uuid
        def filePath = article.path
        if(!uuid || !filePath){
            uuid = UUID.randomUUID().toString()
            def fileName = "${uuid}.html".toString()
            filePath = createCompleteFilePath(account, fileName)
            article.uuid = uuid
            article.path = createFilePath(account, fileName)
        }
        else {
            filePath = completeFilePath(filePath)
        }
        write(filePath, content)
    }

    /**
     * 保存多图文内容到文件
     * @param articleItem
     * @param account
     * @param content
     * @return
     */
    def saveContent(ArticleItem articleItem, Account account, String content) {
        def uuid = articleItem.uuid
        def filePath = articleItem.path
        if(!uuid || !filePath){
            uuid = UUID.randomUUID().toString()
            def fileName = "${uuid}.html".toString()
            filePath = createCompleteFilePath(account, fileName)
            articleItem.uuid = uuid
            articleItem.path = createFilePath(account, fileName)
        }
        else {
            filePath = completeFilePath(filePath)
        }
        write(filePath, content)
    }

    /**
     * 读取图文内容
     * @param article
     * @return
     */
    String readContent(Article article) {
        if(article.path){
            def filePath = completeFilePath(article.path)
            return read(filePath)
        }
        return ""
    }

    /**
     * 读取多图文文章内容
     * @param articleItem
     * @return
     */
    String readContent(ArticleItem articleItem) {
        if(articleItem.path){
            def filePath = completeFilePath(articleItem.path)
            return read(filePath)
        }
        return ""
    }

    /**
     * 从文件读取
     * @param filePath
     * @return
     */
    String read(String filePath) {
        try {
            if(filePath && filePath.endsWith(".html")){
                def file = new File(filePath)
                if(file.exists()){
                    return file.text
                }
            }
        } catch(Exception e){
            log.error("<read> error: " + e.message, e)
        }
        return ""
    }

    /**
     * 写入文件
     * @param filePath
     * @param content
     */
    void write(String filePath, String content) {
        try {
            def file = new File(filePath)
            file.write(content)
        } catch(Exception e){
            log.error("<write> error: " + e.message, e)
        }
    }

    /**
     * 删除图文内容文件
     * @param filePath
     * @return
     */
    boolean deleteFile(String filePath) {
        boolean result = false
        try {
            if(filePath) {
                def path = appendPath(basePath(), filePath)
                def file = new File(path)
                if(file.exists()) {
                    file.delete()
                }
            }
        }
        catch (Exception e) {
            log.error("<deleteFile> error: " + e.message, e)
        }
        return result
    }

    /**
     * 生成文件文件url
     * @param imagePath
     * @return
     */
    String serverUrl(String urlPath){
        return appendUrlPath(serverPath(), urlPath)
    }

    /**
     * 完整路径
     * @param filePath
     * @return
     */
    String completeFilePath(String filePath) {
        return appendPath(basePath(), filePath)
    }

    /**
     * 生成文件文件url
     * @param imagePath
     * @return
     */
    String subUrlPath(String url){
        def len = serverPath().length()
        return url?.substring(len)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @return
     */
    String createCompleteFilePath(User user, String fileName) {
        return createCompleteFilePath(createDirPath("user", user.id.toString(), "articles", true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @return
     */
    String createCompleteFilePath(Account account, String fileName) {
        return createCompleteFilePath(createDirPath("account", account.id.toString(), "articles", true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @return
     */
    String createFilePath(User user, String fileName) {
        return appendPath(createDirPath("user", user.id.toString(), "articles", true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @return
     */
    String createFilePath(Account account, String fileName) {
        return appendPath(createDirPath("account", account.id.toString(), "articles", true), fileName)
    }

    /**
     *
     * @param dirPath
     * @param fileName
     * @return
     */
    String createCompleteFilePath(String dirPath, String fileName) {
        try {
            String filePath = basePath()
            filePath = appendPath(filePath, dirPath)
            def file = new File(filePath)
            if(!file.exists()){
                file.mkdirs()
            }
            return appendPath(filePath, fileName)
        }
        catch (Exception e) {
            log.error("<createCompleteFilePath> error dirPath: ${dirPath}, fileName: ${fileName}".toString(), e)
        }
    }

    /**
     *
     * @param category
     * @param home
     * @param type
     * @param isDate
     * @return
     */
    String createDirPath(String category, String home, String type, boolean isDate) {
        def filePath = appendPath(category, home)
        filePath = appendPath(filePath, type)
        if(isDate) {
            filePath = appendPath(filePath, DF.format(new Date()))
        }
        return filePath
    }

    /**
     *
     * @param account
     * @param fileName
     * @return
     */
    String createUrlPath(User user, String fileName) {
        return createUrlPath("user", user.id.toString(), "articles", fileName, true)
    }

    /**
     *
     * @param account
     * @param fileName
     * @return
     */
    String createUrlPath(Account account, String fileName) {
        return createUrlPath("account", account.id.toString(), "articles", fileName, true)
    }

    /**
     *
     * @param category
     * @param home
     * @param type
     * @param fileName
     * @param isDate
     * @return
     */
    String createUrlPath(String category, String home, String type, String fileName, boolean isDate) {
        try {
            String filePath = appendUrlPath(category, home)
            filePath = appendUrlPath(filePath, type)
            if(isDate) {
                filePath = appendUrlPath(filePath, DF.format(new Date()))
            }
            return appendUrlPath(filePath, fileName)
        }
        catch (Exception e) {
            log.error("<createFilePath> error category: ${category}, home: ${home}, type: ${type}, fileName: ${fileName}".toString(), e)
            e.printStackTrace()
        }
    }

    /**
     * 读取配置文件中的素材文件根目录
     * @return
     */
    String basePath() {
        def config = grailsApplication.config.chat.assets
        return config.basePath
    }

    /**
     * 读取配置文件中的素材文件URL根目录
     * @return
     */
    String serverPath(){
        def config = grailsApplication.config.chat.assets
        return config.server
    }

    String appendPath(String basePath, String path) {
        if(path) {
            def config = grailsApplication.config.chat.assets
            String fileSeparator = config.fileSeparator?: "/"
            if(basePath.endsWith(fileSeparator) || path.startsWith(fileSeparator)){
                return basePath + path
            }
            else {
                return basePath + fileSeparator + path
            }
        }
        else {
            return basePath
        }
    }

    String appendUrlPath(String basePath, String path) {
        if(path) {
            String fileSeparator = "/"
            if(basePath.endsWith(fileSeparator) || path.startsWith(fileSeparator)){
                return basePath + path
            }
            else {
                return basePath + fileSeparator + path
            }
        }
        else {
            return basePath
        }
    }
}
