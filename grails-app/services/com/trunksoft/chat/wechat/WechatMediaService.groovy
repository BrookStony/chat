package com.trunksoft.chat.wechat

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Audio
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.assets.Video
import com.trunksoft.chat.services.ChatMediaService
import com.trunksoft.chat.services.MaterialResult
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.util.CmdResult
import com.trunksoft.chat.util.CmdUtil
import com.trunksoft.chat.util.FileUtil
import com.trunksoft.platform.common.ErrorCode

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * 临时素材接口
 */
class WechatMediaService implements ChatMediaService {

    static transactional = false

    private static final DateFormat DF = new SimpleDateFormat("yyyyMMdd")

    def grailsApplication
    def wechatAccountService
    def materialManageService

    @Override
    MaterialResult upload(Account account, Picture picture) throws ApiErrorException {
        try {
            int timeout = picture.mediaSize.intValue()
            timeout = timeout > 10000 ? timeout : 10000
            return curlUpload(account, picture.path, "image", timeout)
        }
        catch (Exception e) {
            log.error(" upload image error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    @Override
    MaterialResult upload(Account account, Audio audio) throws ApiErrorException {
        try {
            int timeout = audio.mediaSize.intValue()
            timeout = timeout > 10000 ? timeout : 10000
            return curlUpload(account, audio.path, "voice", timeout)
        }
        catch (Exception e) {
            log.error(" upload voice error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    @Override
    MaterialResult upload(Account account, Video video) throws ApiErrorException {
        try {
            int timeout = video.mediaSize.intValue()
            timeout = timeout > 10000 ? timeout : 10000
            return curlUpload(account, video.path, "video", timeout)
        }
        catch (Exception e) {
            log.error(" upload video error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    private MaterialResult curlUpload(Account account, String fileName, String type, int timeout) {

        def config = grailsApplication.config.chat.curl
        String basePath = materialManageService.basePath()
        String url = config.uploadMediaUrl
        String waitfor = config.waitfor
        String charset = config.charset
        if(!url)
            return new MaterialResult(ErrorCode.CONF_CURL_UPLOADURL_NULL, "config.chat.curl.uploadMediaUrl can not be null")

        if(!waitfor)
            return new MaterialResult(ErrorCode.CONF_CURL_WAITFOR_NULL, "config.chat.curl.waitfor can not be null")

        def filePath = materialManageService.appendPath(basePath, fileName)
        wechatAccountService.refreshAccessToken(account)
        String command = "curl -F media=@${filePath} \"${url}?access_token=${account.refreshToken}&type=${type}\"".toString()

        try {
            log.info(" curlUpload command: " + command + ", waitfor: " + waitfor + ", timeout: " + timeout * 3 + ", charset: " + charset)
            long startTime = System.currentTimeMillis()
            CmdResult cmdResult = CmdUtil.cmd(command, waitfor, ["media_id", "errcode"], timeout * 3, charset)
            long endTime = System.currentTimeMillis()
            log.info(" curlUpload result: " + cmdResult.toString())
            return curlUploadResult(cmdResult, (endTime - startTime))
        }
        catch (Exception e) {
            log.error(" curlUpload command[${command}] error".toString(), e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    private MaterialResult curlUploadResult(CmdResult cmdResult, long costTime) {
        if(cmdResult.result){
            if(-1 != cmdResult.result.indexOf("media_id")){
                def matcher = (cmdResult.result =~ /(\{"type":"(\w+)","media_id":"(.+)","created_at":(\d+)\})[\r|\n]*.*$/)
                if(matcher.matches() && matcher.groupCount() > 3){
                    return new MaterialResult(0, "ok", matcher.group(3), matcher.group(4) as Long, costTime)
                }
            }
            else if(-1 != cmdResult.result.indexOf("errcode")) {
                def matcher = (cmdResult.result =~ /(\{"errcode":(\d+),"errmsg":"(.+)"\})[\r|\n]*.*$/)
                if(matcher.matches() && matcher.groupCount() > 2){
                    return new MaterialResult(matcher.group(2) as Integer, matcher.group(3))
                }
            }
        }
        if(cmdResult.isTimeout()) {
            return new MaterialResult(ErrorCode.TIMEOUT, "timeout")
        }
        else {
            return new MaterialResult(ErrorCode.EXCEPTION, cmdResult.message)
        }
    }

    @Override
    MaterialResult download(Account account, Picture picture) throws ApiErrorException {
        try {
            if(picture.mediaId){
                int timeout = picture.mediaSize.intValue()
                timeout = timeout > 10000 ? timeout : 10000
                MaterialResult result = curlDownload(account, picture.mediaId, timeout)
                if(picture.picurl && result.isOk() && result.filename) {
                    picture.name = result.filename
                    picture.fileType = FileUtil.getFileType(result.filename)
                    picture.path = materialManageService.createFilePath(account, result.filename, MaterialType.PICTURE.dir)
                    picture.url =  materialManageService.createUrlPath(account, result.filename, MaterialType.PICTURE.dir)
                    picture.createTime = new Date()
                    def filePath = materialManageService.createCompleteFilePath(account, result.filename, MaterialType.PICTURE.dir)
                    MaterialResult fileResult = curlDownloadFile(filePath, picture.picurl, timeout)
                    if(fileResult.isOk()) {
                        def file = new File(filePath)
                        if(file.exists()) {
                            picture.mediaSize = file.size()
                        }
                    }
                }
                return result
            }
            else {
                return new MaterialResult(ErrorCode.MEDIAID_NULL, "mediaId can not be null")
            }
        }
        catch (Exception e) {
            log.error(" download image error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    @Override
    MaterialResult download(Account account, Audio audio) throws ApiErrorException {
        try {
            if(audio.mediaId){
                int timeout = audio.mediaSize.intValue()
                timeout = timeout > 10000 ? timeout : 10000
                return curlDownload(account, audio.mediaId, timeout)
            }
            else {
                return new MaterialResult(ErrorCode.MEDIAID_NULL, "mediaId can not be null")
            }
        }
        catch (Exception e) {
            log.error(" download audio error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    @Override
    MaterialResult download(Account account, Video video) throws ApiErrorException {
        try {
            if(video.mediaId){
                int timeout = video.mediaSize.intValue()
                timeout = timeout > 10000 ? timeout : 10000
                return curlDownload(account, video.mediaId, timeout)
            }
            else {
                return new MaterialResult(ErrorCode.MEDIAID_NULL, "mediaId can not be null")
            }
        }
        catch (Exception e) {
            log.error(" download video error", e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    private MaterialResult curlDownload(Account account, String mediaId, int timeout) {
        def config = grailsApplication.config.chat.curl
        String url = config.downloadMediaUrl
        String waitfor = config.waitfor
        String charset = config.charset
        if(!url)
            return new MaterialResult(ErrorCode.CONF_CURL_DOWNLOADURL_NULL, "config.chat.curl.downloadMediaUrl can not be null")

        if(!waitfor)
            return new MaterialResult(ErrorCode.CONF_CURL_WAITFOR_NULL, "config.chat.curl.waitfor can not be null")

        wechatAccountService.refreshAccessToken(account)
        String command = "curl -I -G \"${url}?access_token=${account.refreshToken}&media_id=${mediaId}\"".toString()
        try {
            log.info(" curlDownload command: " + command + ", timeout: " + timeout)
            long startTime = System.currentTimeMillis()
            CmdResult cmdResult = CmdUtil.cmd(command, waitfor, timeout, charset)
            long endTime = System.currentTimeMillis()
            log.info(" curlDownload result: " + cmdResult.toString())
            return curlDownloadResult(cmdResult, (endTime - startTime))
        }
        catch (Exception e) {
            log.error(" curlDownload command[${command}] error".toString(), e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }

    private MaterialResult curlDownloadResult(CmdResult cmdResult, long costTime) {
        if(cmdResult.result){
            if(-1 != cmdResult.result.indexOf("OK") && -1 != cmdResult.result.indexOf("filename")){
                String filename = null
                def lines = cmdResult.result.split("\n")
                for(String line : lines){
                    int index = line.indexOf("filename=")
                    if(-1 != index){
                        filename = line.substring(index + 10, line.length() - 2)
                        break
                    }
                }
                return new MaterialResult(0, "ok", filename, costTime)
            }
            else if(-1 != cmdResult.result.indexOf("errcode")) {
                def matcher = (cmdResult.result =~ /(\{"errcode":"(\d+)","errmsg":"(\w+)"\})[\r|\n]*\n.*$/)
                if(matcher.matches() && matcher.groupCount() > 2){
                    return new MaterialResult(matcher.group(2) as Integer, matcher.group(3))
                }
            }
        }
        if(cmdResult.isTimeout()) {
            return new MaterialResult(ErrorCode.TIMEOUT, "timeout")
        }
        else {
            return new MaterialResult(ErrorCode.EXCEPTION, cmdResult.message)
        }
    }

    private MaterialResult curlDownloadFile(String file, String url, int timeout) {
        def config = grailsApplication.config.chat.curl
        String waitfor = config.waitfor
        String charset = config.charset
        if(!url)
            return new MaterialResult(ErrorCode.CONF_CURL_DOWNLOADURL_NULL, "config.chat.curl.downloadMediaUrl can not be null")

        if(!waitfor)
            return new MaterialResult(ErrorCode.CONF_CURL_WAITFOR_NULL, "config.chat.curl.waitfor can not be null")

        String command = "curl -o ${file} ${url}".toString()
        try {
            log.info(" curlDownloadFile command: " + command + ", timeout: " + timeout)
            CmdResult cmdResult = CmdUtil.cmd(command, waitfor, timeout, charset)
            log.info(" curlDownloadFile result: " + cmdResult.toString())
            return new MaterialResult(cmdResult.code, cmdResult.message)
        }
        catch (Exception e) {
            log.error(" curlDownloadFile command[${command}] error".toString(), e)
            return new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
    }
}
