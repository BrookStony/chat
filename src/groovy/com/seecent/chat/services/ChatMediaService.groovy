package com.seecent.chat.services

import com.seecent.chat.Account
import com.seecent.chat.assets.Audio
import com.seecent.chat.assets.Picture
import com.seecent.chat.assets.Video
import com.seecent.chat.services.exception.ApiErrorException

/**
 * 临时素材接口
 */
public interface ChatMediaService {

    MaterialResult upload(Account account, Picture picture) throws ApiErrorException
    MaterialResult upload(Account account, Audio audio) throws ApiErrorException
    MaterialResult upload(Account account, Video video) throws ApiErrorException
    MaterialResult download(Account account, Picture picture) throws ApiErrorException
    MaterialResult download(Account account, Audio audio) throws ApiErrorException
    MaterialResult download(Account account, Video video) throws ApiErrorException

}