package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.assets.Article
import com.seecent.chat.assets.ArticleItem
import com.seecent.chat.assets.Audio
import com.seecent.chat.assets.Picture
import com.seecent.chat.assets.Video
import com.seecent.chat.services.exception.ApiErrorException

/**
 * 永久素材接口
 */
public interface ChatMaterialService {

    MaterialResult upload(Account account, Picture picture) throws ApiErrorException
    MaterialResult upload(Account account, Audio audio) throws ApiErrorException
    MaterialResult upload(Account account, Video video) throws ApiErrorException
    MaterialResult download(Account account, Picture picture) throws ApiErrorException
    MaterialResult download(Account account, Audio audio) throws ApiErrorException
    MaterialResult download(Account account, Video video) throws ApiErrorException
    MaterialResult delete(Account account, String mediaId) throws ApiErrorException
    def getMaterialCount(Account account) throws ApiErrorException
    def batchGetMaterial(Account account, String type, int offset, int count) throws ApiErrorException

    Article getArticle(Account account, String mediaId) throws ApiErrorException
    MaterialResult addArticle(Account account, Article article) throws ApiErrorException
    MaterialResult updateArticle(Account account, Article article) throws ApiErrorException
    MaterialResult updateArticleItem(Account account, ArticleItem articleItem) throws ApiErrorException
    MaterialResult deleteArticle(Account account, String mediaId) throws ApiErrorException
}