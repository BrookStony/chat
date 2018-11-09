package com.seecent.chat.assets

import com.seecent.chat.Account
import com.seecent.platform.auth.User

import java.text.DateFormat
import java.text.SimpleDateFormat

class MaterialPathService {

    static transactional = false

    private static final DateFormat DF = new SimpleDateFormat("yyyyMMdd")

    def grailsApplication

    /**
     * 生成文件文件url
     * @param imagePath
     * @return
     */
    String serverUrl(String urlPath){
        return appendPath(serverPath(), urlPath)
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
     * @param type
     * @return
     */
    String createCompleteFilePath(User user, String fileName, String type) {
        return createCompleteFilePath(createDirPath("user", user.id.toString(), type, true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @param type
     * @return
     */
    String createCompleteFilePath(Account account, String fileName, String type) {
        return createCompleteFilePath(createDirPath("account", account.id.toString(), type, true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @param type
     * @return
     */
    String createFilePath(User user, String fileName, String type) {
        return appendPath(createDirPath("user", user.id.toString(), type, true), fileName)
    }

    /**
     * 创建用户上传文件目录
     * @param account
     * @param fileName
     * @param type
     * @return
     */
    String createFilePath(Account account, String fileName, String type) {
        return appendPath(createDirPath("account", account.id.toString(), type, true), fileName)
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
            log.error(" createFilePath error dirPath: ${dirPath}, fileName: ${fileName}".toString(), e)
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
     * @param type
     * @return
     */
    String createUrlPath(User user, String fileName, String type) {
        return createUrlPath("user", user.id.toString(), type, fileName, true)
    }

    /**
     *
     * @param account
     * @param fileName
     * @param type
     * @return
     */
    String createUrlPath(Account account, String fileName, String type) {
        return createUrlPath("account", account.id.toString(), type, fileName, true)
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
            log.error(" createFilePath error category: ${category}, home: ${home}, type: ${type}, fileName: ${fileName}".toString(), e)
            e.printStackTrace()
        }
    }

    String basePath() {
        def config = grailsApplication.config.chat.assets
        return config.basePath
    }

    String serverPath(){
        def config = grailsApplication.config.chat.assets
        return config.server
    }

    String appendPath(String basePath, String path) {
        if(path) {
            def config = grailsApplication.config.adms.assets
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
