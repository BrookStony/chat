package com.trunksoft.chat.services

import com.trunksoft.chat.Account
import com.trunksoft.chat.assets.Audio
import com.trunksoft.chat.assets.Picture
import com.trunksoft.chat.assets.Video
import com.trunksoft.chat.services.exception.ApiErrorException

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