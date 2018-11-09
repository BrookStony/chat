package com.trunksoft.chat.wechat

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Article
import com.trunksoft.chat.assets.ArticleItem
import com.trunksoft.chat.assets.Audio
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.assets.Video
import com.trunksoft.chat.services.ChatMaterialService
import com.trunksoft.chat.services.MaterialResult
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.type.MaterialType
import com.trunksoft.chat.util.ArticleJsonUtil
import com.trunksoft.chat.util.CmdResult
import com.trunksoft.chat.util.CmdUtil
import com.trunksoft.chat.util.FileUtil
import com.trunksoft.platform.common.ErrorCode

import com.trunksoft.chat.util.HttpClientUtil

import org.apache.commons.lang.StringEscapeUtils

import grails.converters.JSON
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.text.DateFormat
import java.text.SimpleDateFormat

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * 永久素材接口
 */
class WechatMaterialService implements ChatMaterialService {

    static transactional = false

    private static final DateFormat DF = new SimpleDateFormat("yyyyMMdd")
    private static final MATERIAL_HOST = "api.weixin.qq.com"
    private static final MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material"

    def grailsApplication
    def wechatAccountService
    def materialManageService

    /**
     * 上传永久图片素材
     * @param account
     * @param picture
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult upload(Account account, Picture picture) throws ApiErrorException {
        MaterialResult result = new MaterialResult(ErrorCode.SUCCESS, "OK")
        try {
            long startTime = System.currentTimeMillis()
            String basePath = materialManageService.basePath()
            def filePath = materialManageService.appendPath(basePath, picture.path)
            def file = new File(filePath)
            wechatAccountService.refreshAccessToken(account)
            String url = MATERIAL_URL + "/add_material?access_token=" + account.refreshToken + "&type=image"
            String text = HttpClientUtil.upload(MATERIAL_HOST, url, "media", file, "image/jpeg", "UTF-8")
            log.info(" <upload> upload image[${picture.id}] result: " + text)
            if(null != text) {
                JsonParser jsonParser = new JsonParser()
                JsonObject json = jsonParser.parse(text).getAsJsonObject()
                if(null != json.get("media_id")) {
                    // 上传成功  {"media_id":"MEDIA_ID","url":URL}
                    String mediaId = json.get("media_id").getAsString()
                    String mediaUrl = json.get("url").getAsString().replaceAll("\\\\", "")
                    long costTime = System.currentTimeMillis() - startTime
                    result = new MaterialResult(0, "OK", mediaId, mediaUrl, costTime)
                }
                else {
                    // {"errcode":40004,"errmsg":"invalid media type"}
                    if(null != json.get("errcode") && null != json.get("errmsg")) {
                        int errcode = json.get("errcode").getAsInt()
                        String errmsg = json.get("errmsg").getAsString()
                        result = new MaterialResult(errcode, errmsg)
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" <upload> upload image[${picture.id}] error: " + e.message, e)
            result = new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
        return result
    }

    @Override
    MaterialResult upload(Account account, Audio audio) throws ApiErrorException {
        MaterialResult result = new MaterialResult(ErrorCode.SUCCESS, "OK")
        try {
            long startTime = System.currentTimeMillis()
            String basePath = materialManageService.basePath()
            def filePath = materialManageService.appendPath(basePath, audio.path)
            def file = new File(filePath)
            wechatAccountService.refreshAccessToken(account)
            String url = MATERIAL_URL + "/add_material?access_token=" + account.refreshToken + "&type=audio"
            String text = HttpClientUtil.upload(MATERIAL_HOST, url, "media", file, "audio/jpeg", "UTF-8")
            log.info(" <upload> audio image[${audio.id}] result: " + text)
            if(null != text) {
                JsonParser jsonParser = new JsonParser()
                JsonObject json = jsonParser.parse(text).getAsJsonObject()
                if(null != json.get("media_id")) {
                    // 上传成功  {"media_id":"MEDIA_ID","url":URL}
                    String mediaId = json.get("media_id").getAsString()
                    String mediaUrl = json.get("url").getAsString().replaceAll("\\\\", "")
                    long costTime = System.currentTimeMillis() - startTime
                    result = new MaterialResult(0, "OK", mediaId, mediaUrl, costTime)
                }
                else {
                    // {"errcode":40004,"errmsg":"invalid media type"}
                    if(null != json.get("errcode") && null != json.get("errmsg")) {
                        int errcode = json.get("errcode").getAsInt()
                        String errmsg = json.get("errmsg").getAsString()
                        result = new MaterialResult(errcode, errmsg)
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" <upload> upload audio[${audio.id}] error: " + e.message, e)
            result = new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
        return result
    }

    @Override
    MaterialResult upload(Account account, Video video) throws ApiErrorException {
        MaterialResult result = new MaterialResult(ErrorCode.SUCCESS, "OK")
        try {
            long startTime = System.currentTimeMillis()
            String basePath = materialManageService.basePath()
            def filePath = materialManageService.appendPath(basePath, video.path)
            def file = new File(filePath)
            wechatAccountService.refreshAccessToken(account)
            String url = MATERIAL_URL + "/add_material?access_token=" + account.refreshToken + "&type=video"
            String text = HttpClientUtil.upload(MATERIAL_HOST, url, "media", file, "video/jpeg", "UTF-8")
            log.info(" <upload> upload video[${video.id}] result: " + text)
            if(null != text) {
                JsonParser jsonParser = new JsonParser()
                JsonObject json = jsonParser.parse(text).getAsJsonObject()
                if(null != json.get("media_id")) {
                    // 上传成功  {"media_id":"MEDIA_ID","url":URL}
                    String mediaId = json.get("media_id").getAsString()
                    String mediaUrl = json.get("url").getAsString().replaceAll("\\\\", "")
                    long costTime = System.currentTimeMillis() - startTime
                    result = new MaterialResult(0, "OK", mediaId, mediaUrl, costTime)
                }
                else {
                    // {"errcode":40004,"errmsg":"invalid media type"}
                    if(null != json.get("errcode") && null != json.get("errmsg")) {
                        int errcode = json.get("errcode").getAsInt()
                        String errmsg = json.get("errmsg").getAsString()
                        result = new MaterialResult(errcode, errmsg)
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(" <upload> upload image[${video.id}] error: " + e.message, e)
            result = new MaterialResult(ErrorCode.EXCEPTION, e.message)
        }
        return result
    }

    /**
     * 删除永久素材
     * @param account
     * @param mediaId
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult delete(Account account, String mediaId) throws ApiErrorException {
        log.info(" <delete> account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: MATERIAL_URL) {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/del_material', query: params) {
                type "application/json"
                text "{\"media_id\":\"${mediaId}\"}".toString()
            }
            def text = response.text
            log.info(" <delete> mediaId: " + mediaId + ", text: " + text)
            if(text){
                def json = new JsonSlurper().parseText(text)
                return new MaterialResult(json.errcode as Integer, json.errmsg as String)
            }
            else {
                return new MaterialResult(4, "failure")
            }
        }
    }

    /**
     * 获取素材总素
     * @param account
     * @throws ApiErrorException
     */
    @Override
    def getMaterialCount(Account account) throws ApiErrorException {
        log.info(" <getMaterialCount> account[${account.name}]".toString())
        withRest(url: MATERIAL_URL) {
            def params = [access_token: account.refreshToken]
            def response = get(path: '/get_materialcount', query: params)
            def json = response.json
            log.info(" <getMaterialCount> json: " + json)
            return json
        }
    }

    /**
     * 获取素材列表
     * @param account
     * @param type
     * @param offset
     * @param count
     * @throws ApiErrorException
     */
    @Override
    def batchGetMaterial(Account account, String type, int offset, int count) throws ApiErrorException {
        log.info(" <batchGetMaterial> account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: MATERIAL_URL) {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/batchget_material', query: params) {
                type "application/json"
                text "{\"type\":\"${type}\", \"offset\":${offset}, \"count\":${count}}".toString()
            }
            def json = response.json
            log.info(" <batchGetMaterial> type: " + type + ", offset: " + offset +  ", count: " + count + ", json: " + json)
            return json
        }
    }

    private static def toArticleMap(Article it) {
        def coverImage = Picture.get(it.coverImageId)
        if(coverImage && coverImage.mediaId) {
            return [ "title": it.title,
                     "thumb_media_id": coverImage.mediaId,
                     "author": it.author,
                     "digest": it.description,
                     "show_cover_pic": it.coverDisplayInText ? 1 : 0,
                     "content": it.content,
                     "content_source_url": it.originalUrl]
        }
        return null
    }

    private static def toArticleItemMap(ArticleItem it) {
        def coverImage = Picture.get(it.coverImageId)
        if(coverImage && coverImage.mediaId) {
            return [ "title": it.title,
                     "thumb_media_id": coverImage.mediaId,
                     "author": it.author,
                     "digest": it.description,
                     "show_cover_pic": it.coverDisplayInText ? 1 : 0,
                     "content": it.content,
                     "content_source_url": it.originalUrl]
        }
        return null
    }

    /**
     * 获取图文
     * @param account
     * @param mediaId
     * @return
     * @throws ApiErrorException
     */
    @Override
    Article getArticle(Account account, String mediaId) throws ApiErrorException {
        log.info(" <getArticle> account[${account.name}]".toString())
        Article article = null
        wechatAccountService.refreshAccessToken(account)
        withRest(url: MATERIAL_URL) {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/get_material', query: params) {
                type "application/json"
                text "{\"media_id\":\"${mediaId}\"}".toString()
            }
            def json = response.json
            log.info(" <getArticle> mediaId: " + mediaId + ", text: " + response.text)
            log.info(" <getArticle> mediaId: " + mediaId + ", json: " + json)
            if(json){
                def newsItems = json.news_item
                if(newsItems && newsItems.size() > 0) {
                    if(1 == newsItems.size()) {

                    }
                    else {

                    }
                }
            }
        }
        return article
    }

    /**
     * 添加永久图文素材
     * @param account
     * @param article
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult addArticle(Account account, Article article) throws ApiErrorException {
        log.info(" <addArticle> account[${account.name}]".toString())
        MaterialResult result = new MaterialResult(4, "failure")
        def articles = []
//        def articleMap = toArticleMap(article)
//        if(articleMap) {
//            articles << articleMap
//            if(article.articleItems && article.articleItems.size() > 0) {
//                def articleItems = article.articleItems.sort{a,b-> a.no<=>b.no}
//                articleItems.each {
//                    def articleItemMap = toArticleItemMap(it)
//                    if(articleItemMap) {
//                        articles << articleItemMap
//                    }
//                }
//            }
//        }
        if(article.content && article.coverImageId) {
            articles << ArticleJsonUtil.toJson(article)
            if(article.articleItems && article.articleItems.size() > 0) {
                def articleItems = article.articleItems.sort{a,b-> a.no<=>b.no}
                articleItems.each {
                    if(it.content && it.coverImageId){
                        articles << ArticleJsonUtil.toJson(it)
                    }
                }
            }
        }
        if(articles.size() > 0) {
//            def jsonBuilder = new JsonBuilder()
//            jsonBuilder.call([articles: articles])
//            def dataText = jsonBuilder.toString()
//            def data = StringEscapeUtils.unescapeJava(dataText)
            def data = ArticleJsonUtil.toJson(articles)
            wechatAccountService.refreshAccessToken(account)
            withRest(url: MATERIAL_URL) {
                def params = [access_token: account.refreshToken]
                def response = post(path: '/add_news', query: params) {
                    type "application/json"
                    text data
                }
                def text = response.text
                log.info(" <addArticle> article[" + article.id + "] text: " + response.text)
                if(text){
                    def json = new JsonSlurper().parseText(text)
                    if(json.media_id) {
                        result = new MaterialResult(0, "OK", json.media_id as String)
                    }
                    else if(json.errcode && json.errmsg) {
                        result = new MaterialResult(json.errcode as Integer, json.errmsg as String)
                    }
                }
                else {
                    result = new MaterialResult(4, "failure")
                }
            }
        }
        else {
            result = new MaterialResult(4, "build articles json error!")
        }
        return result
    }

    /**
     * 修改永久图文素材
     * @param account
     * @param article
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult updateArticle(Account account, Article article) throws ApiErrorException {
        log.info(" <updateArticle> account[${account.name}]".toString())
        MaterialResult result = new MaterialResult(4, "failure")
        if(article.content && article.coverImageId) {
            def data = ArticleJsonUtil.toUpdateJson(article)
            println "data: " + data
            wechatAccountService.refreshAccessToken(account)
            withRest(url: MATERIAL_URL) {
                def params = [access_token: account.refreshToken]
                def response = post(path: '/update_news', query: params) {
                    type "application/json"
                    text data
                }
                def text = response.text
                log.info(" <updateArticle> article[" + article.id + "] text: " + text)
                if(text){
                    def json = new JsonSlurper().parseText(text)
                    if(json.errcode && json.errmsg) {
                        result = new MaterialResult(json.errcode as Integer, json.errmsg as String)
                    }
                }
                else {
                    result = new MaterialResult(4, "failure")
                }
            }
        }
        else {
            result = new MaterialResult(4, "build articles json error!")
        }
        return result
    }

    /**
     * 修改永久图文素材
     * @param account
     * @param articleItem
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult updateArticleItem(Account account, ArticleItem articleItem) throws ApiErrorException {
        log.info(" <updateArticleItem> account[${account.name}]".toString())
        MaterialResult result = new MaterialResult(4, "failure")
        if(articleItem.content && articleItem.coverImageId) {
            def data = ArticleJsonUtil.toUpdateJson(articleItem)
            wechatAccountService.refreshAccessToken(account)
            withRest(url: MATERIAL_URL) {
                def params = [access_token: account.refreshToken]
                def response = post(path: '/update_news', query: params) {
                    type "application/json"
                    text data
                }
                def text = response.text
                log.info(" <updateArticleItem> articleItem[" + articleItem.id + "] text: " + text)
                if(text){
                    def json = new JsonSlurper().parseText(text)
                    if(json.errcode && json.errmsg) {
                        result = new MaterialResult(json.errcode as Integer, json.errmsg as String)
                    }
                }
                else {
                    result = new MaterialResult(4, "failure")
                }
            }
        }
        else {
            result = new MaterialResult(4, "build articles json error!")
        }
        return result
    }

    /**
     * 删除永久图文素材
     * @param account
     * @param mediaId
     * @return
     * @throws ApiErrorException
     */
    @Override
    MaterialResult deleteArticle(Account account, String mediaId) throws ApiErrorException {
        log.info(" <deleteArticle> account[${account.name}]".toString())
        wechatAccountService.refreshAccessToken(account)
        withRest(url: MATERIAL_URL) {
            def params = [access_token: account.refreshToken]
            def response = post(path: '/del_material', query: params) {
                type "application/json"
                text "{\"media_id\":\"${mediaId}\"}".toString()
            }
            def text = response.text
            log.info(" <deleteArticle> mediaId: " + mediaId + ", text: " + text)
            if(text){
                def json = new JsonSlurper().parseText(text)
                return new MaterialResult(json.errcode as Integer, json.errmsg as String)
            }
            else {
                return new MaterialResult(4, "failure")
            }
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
        String url = config.materialUrl
        String waitfor = config.waitfor
        String charset = config.charset
        if(!url)
            return new MaterialResult(ErrorCode.CONF_CURL_DOWNLOADURL_NULL, "config.chat.curl.downloadUrl can not be null")

        if(!waitfor)
            return new MaterialResult(ErrorCode.CONF_CURL_WAITFOR_NULL, "config.chat.curl.waitfor can not be null")

        wechatAccountService.refreshAccessToken(account)
        String command = "curl -I -G \"${url}/get_material?access_token=${account.refreshToken}&media_id=${mediaId}\"".toString()
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
            return new MaterialResult(ErrorCode.CONF_CURL_DOWNLOADURL_NULL, "config.chat.curl.downloadUrl can not be null")

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
