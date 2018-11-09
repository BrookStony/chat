package com.trunksoft.chat.services

import com.trunksoft.chat.message.ImageMessage
import com.trunksoft.chat.message.MusicMessage
import com.trunksoft.chat.message.NewsMessage
import com.trunksoft.chat.message.TextMessage
import com.trunksoft.chat.message.VideoMessage
import com.trunksoft.chat.message.VoiceMessage
import com.trunksoft.chat.message.TemplateMessage
import com.trunksoft.chat.services.exception.ApiErrorException
import com.trunksoft.chat.Account

public interface ChatMessageService {
    MessageResult send(Account account, TextMessage msg) throws ApiErrorException
    MessageResult send(Account account, ImageMessage msg) throws ApiErrorException
    MessageResult send(Account account, VoiceMessage msg) throws ApiErrorException
    MessageResult send(Account account, VideoMessage msg) throws ApiErrorException
    MessageResult send(Account account, MusicMessage msg) throws ApiErrorException
    MessageResult send(Account account, NewsMessage msg) throws ApiErrorException
    MessageResult send(Account account, TemplateMessage msg) throws ApiErrorException
}