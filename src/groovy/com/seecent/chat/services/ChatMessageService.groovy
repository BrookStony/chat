package com.seecent.chat.services

import com.seecent.chat.message.ImageMessage
import com.seecent.chat.message.MusicMessage
import com.seecent.chat.message.NewsMessage
import com.seecent.chat.message.TextMessage
import com.seecent.chat.message.VideoMessage
import com.seecent.chat.message.VoiceMessage
import com.seecent.chat.message.TemplateMessage
import com.seecent.chat.services.exception.ApiErrorException
import com.seecent.chat.Account

public interface ChatMessageService {
    MessageResult send(Account account, TextMessage msg) throws ApiErrorException
    MessageResult send(Account account, ImageMessage msg) throws ApiErrorException
    MessageResult send(Account account, VoiceMessage msg) throws ApiErrorException
    MessageResult send(Account account, VideoMessage msg) throws ApiErrorException
    MessageResult send(Account account, MusicMessage msg) throws ApiErrorException
    MessageResult send(Account account, NewsMessage msg) throws ApiErrorException
    MessageResult send(Account account, TemplateMessage msg) throws ApiErrorException
}